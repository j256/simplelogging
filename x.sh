#!/bin/sh

set -x

env

GH_API="https://api.github.com"
GH_REPO="$GH_API/repos/$CIRCLE_PROJECT_USERNAME/$CIRCLE_PROJECT_REPONAME"
AUTH="$CIRCLE_PROJECT_USERNAME:$MY_KEY_CC"

curl -o /dev/null -s -u "$AUTH" $GH_REPO || { echo "Error: Invalid repo, token or network issue!";  exit 1; }

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

curl -u "$AUTH" -X POST -d @post.json $GH_REPO/check-runs
