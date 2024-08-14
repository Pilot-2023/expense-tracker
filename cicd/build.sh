#!/bin/bash

set -e # exit when an error occurs

ssh_run() {
  local cmd="$1"
  sshpass -p "$VM_PASSWORD" ssh -o StrictHostKeyChecking=no "ssh://$VM_USER@$VM_IP" "$cmd"
}

get_maven_property() {
  local property_name="$1"
  local pom_path="${2:-pom.xml}" # Default POM path (can be overridden by second argument)

  # Check if the pom.xml file exists
  if [ ! -f "$pom_path" ]; then
    echo "POM file not found @ $pom_path"
    exit 1
  fi

  # Use Maven Help Plugin to evaluate the property
  value=$(mvn -q -f "$pom_path" help:evaluate -DforceStdout -Dexpression="$property_name" 2>/dev/null)

  # Check for error or empty value
  if [[ $? -ne 0 || -z "$value" ]]; then
    echo "Error retrieving property '$prope rty_name' or property not found."
    exit 1
  fi

  # Return the evaluated property value
  echo "$value"
}

stage() {
  local stage_name="$1"
  echo
  echo
  echo "######################################################"
  echo "#### stage: $stage_name"
  echo "######################################################"
}

stage "Init" #################################################

BRANCH=$CODEBUILD_GIT_BRANCH
export SERVICE=$(get_maven_property "project.artifactId") # export is needed to be used in secrets.sh
VERSION=$(get_maven_property "project.version")
ECR_URI="$CODEBUILD_ACCOUNT_ID".dkr.ecr."$REGION".amazonaws.com
DOCKER_IMAGE_NAME="$ECR_URI/$SERVICE:$VERSION"

echo "GIT BRANCH = $BRANCH"
echo "SERVICE = $SERVICE"
echo "VERSION = $VERSION"
echo "DOCKER IMAGE = $DOCKER_IMAGE_NAME"

stage "Validate version" #####################################

VERSION_REGEX="[0-9]+\.[0-9]+\.[0-9]+"
SNAPSHOT_REGEX="^$VERSION_REGEX-SNAPSHOT$"
RELEASE_REGEX="^$VERSION_REGEX-RC$"
PRODUCTION_REGEX="^$VERSION_REGEX$"

if [[ "$BRANCH" =~ feature/* ]]; then
  DO_DEPLOY=false

  if [[ ! "$VERSION" =~ $SNAPSHOT_REGEX ]]; then
    echo "Invalid SNAPSHOT version"
    exit 1
  fi
  echo "Version $VERSION is a valid SNAPSHOT version"

elif [[ "$BRANCH" == develop ]]; then
  DO_DEPLOY=true
  export ENV=dev # export is needed to be used in secrets.sh

  if [[ ! "$VERSION" =~ $SNAPSHOT_REGEX ]]; then
    echo "Invalid SNAPSHOT version"
    exit 1
  fi
  echo "Version $VERSION is a valid SNAPSHOT version"

elif [[ "$BRANCH" =~ release/$VERSION_REGEX ]]; then
  DO_DEPLOY=true
  export ENV=pre # export is needed to be used in secrets.sh

  if [[ ! "$VERSION" =~ $RELEASE_REGEX ]]; then
    echo "Invalid RELEASE version"
    exit 1
  fi
  echo "Version $VERSION is a valid RELEASE version"

elif [[ "$BRANCH" =~ main|hotfix/$VERSION_REGEX ]]; then
  DO_DEPLOY=true
  export ENV=prod # export is needed to be used in secrets.sh

  if [[ ! "$VERSION" =~ $PRODUCTION_REGEX ]]; then
    echo "Invalid PRODUCTION version"
    exit 1
  fi
  echo "Version $VERSION is a valid PRODUCTION version"

else
  echo "Invalid branch name $BRANCH"
  exit 1
fi

stage "Set secrets" ##########################################

chmod +x cicd/secrets.sh
bash cicd/secrets.sh

stage "Compile"

mvn clean install

# The following stages are only applicable when there is a deployment
if [[ "$DO_DEPLOY" == false ]]; then
  exit 0
fi

stage "Build Docker image" ###################################

DOCKER_FILE_PATH="$SERVICE-application"
docker build -t "$DOCKER_IMAGE_NAME" "$DOCKER_FILE_PATH"

stage "Push Docker image to ECR" #############################

# Login to ECR
aws ecr get-login-password --region "$REGION" | docker login --username AWS --password-stdin "$ECR_URI"
docker push "$DOCKER_IMAGE_NAME"

stage "Deploy" ###############################################

if [[ "$ENV" == dev ]]; then
  HOST_PORT=59597
elif [[ "$ENV" == pre ]]; then
  HOST_PORT=36511
elif [[ "$ENV" == prod ]]; then
  HOST_PORT=61916
else
  echo "Invalid environment $ENV"
  exit 1
fi

echo "Service will be deployed to $ENV environment"

CONTAINER_PORT=8080

# Login to ECR (inside VM)
ssh_run "aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ECR_URI"

ssh_run "docker pull $DOCKER_IMAGE_NAME"
ssh_run "docker stop $SERVICE-$ENV || true"
ssh_run "docker rm $SERVICE-$ENV || true"
ssh_run "docker run -d --name $SERVICE-$ENV -p $HOST_PORT:$CONTAINER_PORT $DOCKER_IMAGE_NAME"
