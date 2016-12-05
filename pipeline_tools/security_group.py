"""
Tool for managing access to security groups from Bitbucket Pipelines docker
images.

If "allow" is specified, add rules to allow all traffic from this host to
one of the specified instance's security groups.

If "disallow" is specified, remove the rules that this script would have
added from one of the specified instance's security groups.

usage: security_group.py [-h]
                         action instance_id access_key_id secret_access_key

positional arguments:
  action             Either "allow" or "disallow", this parameter determines
                     whether the script is allowing the host to access the
                     given instance, or disallowing it.
  instance_id        The instance id of the instance that this host needs to
                     access.
  access_key_id      The access key used to access AWS.
  secret_access_key  The secret access key used to access AWS.

optional arguments:
  -h, --help         show this help message and exit
"""

import argparse
import boto3
import json
import sys
import urllib2

__author__ = 'Kincaid Savoie'
__email__ = 'ksavoie@axis41.com'


def make_arg_parser():
    """
    Build the argument parser for this program.

    :return: Argument parser.
    """
    parser = argparse.ArgumentParser()
    parser.add_argument('action', help="Either \"allow\" or \"disallow\", " +
                        "this parameter determines whether the script is " +
                        "allowing the host to access the given instance, or " +
                        "disallowing it.")
    parser.add_argument('instance_id', help="The instance id of the " +
                        "instance that this host needs to access.")
    parser.add_argument('access_key_id', help="The access key used to " +
                        "access AWS.")
    parser.add_argument('secret_access_key', help="The secret access key " +
                        "used to access AWS.")
    return parser


def get_host_ip_address():
    """
    Get the public IP address of the current host.

    :return: The host's public IP address, as a string.
    """
    response = urllib2.urlopen('https://api.ipify.org/?format=json')
    return str(json.load(response)['ip'])


def get_regions(aws_session):
    """
    Get all regions available to the given AWS session.

    :param aws_session: Session used to access AWS.
    :return: List of names of available regions.
    """
    client = aws_session.client('ec2', region_name='us-east-1')
    response = client.describe_regions()
    return [x['RegionName'] for x in response['Regions']]


def get_instance_if_in_region(instance_id, aws_session, region):
    """
    Get the specified instance if it is present in the given region.

    :param instance_id: The id of the instance.
    :param aws_session: Session used to access AWS.
    :param region: The region in which to check.
    :return: Boto3 Instance object representing the specified instance if it is
    present in the region, or None if it is not.
    """
    resource = aws_session.resource('ec2', region_name=region)
    instance_filter = [
        {
            'Name': 'instance-id',
            'Values': [
                instance_id
            ]
        }
    ]
    instance_list = list(resource.instances.filter(Filters=instance_filter))
    if len(instance_list) == 1:
        return instance_list[0]
    else:
        return None


def get_instance_by_id(instance_id, aws_session):
    """
    Get the Boto3 Instance describing the instance with the given instance id.

    :param instance_id: The id of the desired instance.
    :param session: Boto3 Session used to access AWS.
    :return: Boto3 Instance object representing the instance.
    """
    regions = get_regions(aws_session)
    for region in regions:
        instance = get_instance_if_in_region(instance_id, aws_session, region)
        if instance is not None:
            return instance
    raise ValueError("Instance with the specified id does not exist.")


def get_instance_region(instance):
    """
    Get the region in which the given instance is placed.

    :param instance: Boto3 Instance object.
    :return: The name of the region in which the instance is placed.
    """
    avail_zone = instance.placement['AvailabilityZone']
    return avail_zone[:-1]


def add_ingress_rule(security_group, host_ip_address):
    """
    Add a rule allowing all traffic from the given host to the security group.

    :param security_group: The security group to add the rule to.
    :param host_ip_address: The address to add to the security group.
    """
    security_group.authorize_ingress(
        IpProtocol='tcp',
        FromPort=0,
        ToPort=65535,
        CidrIp=host_ip_address + '/32')
    security_group.authorize_ingress(
        IpProtocol='icmp',
        FromPort=-1,
        ToPort=-1,
        CidrIp=host_ip_address + '/32')
    security_group.authorize_ingress(
        IpProtocol='udp',
        FromPort=0,
        ToPort=65535,
        CidrIp=host_ip_address + '/32')


def allow_access_to_instance(instance, host_ip_address, aws_session):
    """
    Allow the given host to access the specified instance.

    :param instance: Instance we want to access.
    :param host_ip_address: The address of the host that needs to access the
    instance.
    :param aws_session: Session used to access AWS.
    """
    region = get_instance_region(instance)
    resource = aws_session.resource('ec2', region_name=region)
    security_group_ids = [x['GroupId'] for x in instance.security_groups]
    security_group = resource.SecurityGroup(security_group_ids[0])
    add_ingress_rule(security_group, host_ip_address)


def revoke_access(security_group, host_ip_address):
    """
    Revoke access to the given ip in the security group.

    :param security_group: The security group from which to revoke access.
    :param host_ip_address: The address to revoke access from.
    :return: True if the IP address was present in the security group and
    access was revoked, False otherwise.
    """
    # Check if the given IP is present in the security group.
    host_cidr = host_ip_address + '/32'
    ip_present = False
    for rule in security_group.ip_permissions:
        for ip in rule['IpRanges']:
            if ip['CidrIp'] == host_cidr:
                ip_present = True
    if ip_present:
        security_group.revoke_ingress(
            IpProtocol='tcp',
            FromPort=0,
            ToPort=65535,
            CidrIp=host_cidr)
        security_group.revoke_ingress(
            IpProtocol='udp',
            FromPort=0,
            ToPort=65535,
            CidrIp=host_cidr)
        security_group.revoke_ingress(
            IpProtocol='icmp',
            FromPort=-1,
            ToPort=-1,
            CidrIp=host_cidr)
        return True
    else:
        return False


def disallow_access_to_instance(instance, host_ip_address, aws_session):
    """
    Disallow access to the given instance from the specified host IP.

    :param instance: The instance to disallow access to.
    :param host_ip_address: The ip address to revoke from the given host.
    :param aws_session: The session to use to access AWS.
    """
    region = get_instance_region(instance)
    resource = aws_session.resource('ec2', region_name=region)
    security_group_ids = [x['GroupId'] for x in instance.security_groups]
    for sec_id in security_group_ids:
        curr_group = resource.SecurityGroup(sec_id)
        access_revoked = revoke_access(curr_group, host_ip_address)
        if access_revoked:
            return
    raise Exception("Unable to revoke access.")


def main():
    parser = make_arg_parser()
    args = parser.parse_args()
    host_ip = get_host_ip_address()

    aws_session = boto3.Session(aws_access_key_id=args.access_key_id,
                                aws_secret_access_key=args.secret_access_key)

    target_instance = get_instance_by_id(args.instance_id, aws_session)

    if args.action == 'allow':
        allow_access_to_instance(target_instance, host_ip, aws_session)
    elif args.action == 'disallow':
        disallow_access_to_instance(target_instance, host_ip, aws_session)
    else:
        print("Invalid action supplied. Action must be either \"allow\" or " +
              "\"disallow.\"")
        sys.exit(1)


if __name__ == '__main__':
    main()
