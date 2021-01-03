#!/bin/sh

set -x

GH_API="https://api.github.com"
GH_REPO="$GH_API/repos/$REPO_OWNER/$CIRCLE_PR_REPONAME"

curl -o /dev/null -s -H "$AUTH" $GH_REPO || { echo "Error: Invalid repo, token or network issue!";  exit 1; }

curl -H "$AUTH" -X POST https://api.github.com/repos/j256/simplelogging/check-runs -d '{"name":"name","head_sha":"head_sha"}'
