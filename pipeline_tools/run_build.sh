#!/bin/bash
#
# Run the Bitbucket Pipelines build.
#
# Usage:
#    pipeline_tools/run_build.sh [maven command] [instance id] [access key] [instance access key]

# Trip the e flag so the script will close up the security group
# even if the build fails.
set +e

# The desired Maven command will be the first command line argument.
mvn_command=$1
# The instance to build to will be the second.
target_instance=$2
# The access key will be the third.
access_key=$3
# The secret access key.
secret_access_key=$4

# Allow access to the target instance.
echo "Allowing access to target instance..."
python pipeline_tools/security_group.py allow "$target_instance" "$access_key" "$secret_access_key"

# Make sure the command succeeded.
access_allowed=$?
if [ "$access_allowed" -ne 0 ]; then
    echo "ERROR: Was not able to allow access to the target instance."
else
    echo "Successfully allowed access."
fi

# Run the build.
echo "Running supplied Maven command..."
$mvn_command

# Capture the exit code.
build_success=$?

# Disallow access to the target instance.
echo "Disallowing access to the target instance..."
python pipeline_tools/security_group.py disallow "$target_instance" "$access_key" "$secret_access_key"

# Make sure access was disallowed.
access_disallowed=$?
if [ "$access_disallowed" -ne 0 ]; then
    echo "ERROR: Was not able to disallow access to the target instance."
else
    echo "Successfully disallowed access."
fi

# Return.
exit $build_success

    
