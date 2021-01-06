#!/bin/sh

set -x

env

CIRCLE_WORKFLOW_JOB_ID=7a9c3cac-d77c-44bf-9a01-310c628fedd3
CIRCLE_PULL_REQUESTS=https://github.com/j256/simplelogging/pull/7
CIRCLE_REPOSITORY_URL=git@github.com:j256/simplelogging.git
CIRCLE_WORKING_DIRECTORY=~/project
CIRCLE_INTERNAL_CONFIG=/.circleci-runner-config.json
CIRCLECI=true
CI_PULL_REQUESTS=https://github.com/j256/simplelogging/pull/7
CIRCLE_PROJECT_REPONAME=simplelogging
CIRCLE_WORKFLOW_UPSTREAM_JOB_IDS=
CIRCLE_WORKFLOW_ID=467058ba-1816-43a8-8bd5-3138d91ffe58
CIRCLE_PULL_REQUEST=https://github.com/j256/simplelogging/pull/7
CIRCLE_USERNAME=j256
CIRCLE_BRANCH=gw-testing-check-publishing
CIRCLE_PROJECT_USERNAME=j256
CIRCLE_BUILD_NUM=62
CIRCLE_NODE_TOTAL=1
CIRCLE_SHA1=4c884f7adccb828d0881882854ec69eda602a6e7
CI_PULL_REQUEST=https://github.com/j256/simplelogging/pull/7
CIRCLE_BUILD_URL=https://circleci.com/gh/j256/simplelogging/62
CIRCLE_NODE_INDEX=0
CIRCLE_COMPARE_URL=https://github.com/j256/simplelogging/compare/c36a36c0adc6923a3b30efdafc30aff0589bcc8a...4c884f7adccb828d0881882854ec69eda602a6e7
CIRCLE_PREVIOUS_BUILD_NUM=61
CIRCLE_WORKFLOW_WORKSPACE_ID=467058ba-1816-43a8-8bd5-3138d91ffe58
CIRCLE_STAGE=build
CIRCLE_JOB=build
MY_KEY_CC=****************************************
CIRCLE_SHELL_ENV=/tmp/.bash_env-5ff500d1b69add1c4bbd1290-0-build
CIRCLE_INTERNAL_SCRATCH=/tmp/circleci-181492629
CI=true
CIRCLE_INTERNAL_TASK_DATA=/tmp/.circleci-task-data-5ff500d1b69add1c4bbd1290-0-build



GH_API="https://api.github.com"
GH_REPO="$GH_API/repos/$CIRCLE_PROJECT_USERNAME/$CIRCLE_PROJECT_REPONAME"
AUTH="Authorization: token $MY_KEY_CC"

curl -o /dev/null -s -H "$AUTH" $GH_REPO || { echo "Error: Invalid repo, token or network issue!";  exit 1; }

cat >post.json<<EOF
{
	"name" : "code-coverage",
	"head_sha" : "$CIRCLE_SHA1",
	"output" : {
		"title" : "stuff",
		"summary" : "Summary *here*",
		"annotations" : [
			{
				"path" : "src/main/java/com/j256/simplelogging/LogBackend.java"
				"start_line" : "13",
				"end_line" : "13",
				"annotation_level" : "warning",
				"message", "Something happened here",
				"raw_details", "Some more details here"
			}
		]
	}
}
EOF

curl -H "$AUTH" -X POST -d @post.json $GH_REPO/check-runs
