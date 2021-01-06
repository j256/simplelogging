#!/bin/sh

set -x

env
printenv

GH_API="https://api.github.com"
GH_REPO="$GH_API/repos/$REPO_OWNER/$CIRCLE_PR_REPONAME"
AUTH="Authorization: token $GITHUB_API_TOKEN"

curl -o /dev/null -s -H "$AUTH" $GH_REPO || { echo "Error: Invalid repo, token or network issue!";  exit 1; }

curl -H "$AUTH" -X POST $GH_REPO/check-runs -d '{"name":"name","head_sha":"head_sha"}'
