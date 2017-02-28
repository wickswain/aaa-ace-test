# Bitbucket Pipelines AWS Integration

Because Bitbucket Pipelines spins up Docker images within a range of IP addresses shared by everyone using the service, simply whitelisting the range of addresses that Pipelines uses allows anyone using the service to access our instances. As such, we must allow only instances currently running builds to access our instances. This directory contains a script called `run_build.sh` that can be used to wrap build commands to accomplish this.

## Usage

To use this tool, first create a user that has permissions to allow and revoke access to EC2 security groups, as well as permission to describe EC2 instances. Below is an IAM policy with the given permissions:

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Stmt1474046090000",
            "Effect": "Allow",
            "Action": [
                "ec2:AuthorizeSecurityGroupIngress",
                "ec2:RevokeSecurityGroupIngress",
                "ec2:DescribeInstances",
                "ec2:DescribeSecurityGroups",
                "ec2:DescribeRegions"
            ],
            "Resource": [
                "*"
            ]
        }
    ]
}
```

After you create this user, put the credentials for it in secured environment variables in the repository's settings (be sure to check the "secured" box). After that, copy the `pipeline_tools` folder to the root of the git repository that will be using Pipelines. Then, in the `bitbucket_pipelines.yml` file, call the `run_build.sh` wrapper to execute your build command, like so:

```{bash}
pipeline_tools/run_build.sh [build command] [target instance id] [AWS access key variable] [AWS secret access key variable]
```

The arguments are:

- build command: The command that will actually execute the Maven build.
- target instance id: The id of the instance where the artifacts will be deployed.
- AWS access key: The access key for the user you created earlier.
- AWS secret access key: The secret access key for the user you created earlier.

Once you get the yaml file set up, you're good to go!

## Components

The AWS integration is split into two parts. The first, `security_group.py`, is a Python script that allows and revokes access to target instances from the host it is run on. The second, `run_build.sh`, is a bash script that wraps whatever build command it is supplied with the appropriate calls to `security_group.py`.

test
