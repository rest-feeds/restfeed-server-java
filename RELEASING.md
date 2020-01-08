# Release

mvn clean deploy

mvn versions:set -DnewVersion=0.0.2 -DgenerateBackupPoms=false
replace version in README.md
mvn clean deploy

git tag 0.0.2


mvn versions:set -DnewVersion=0.0.3-SNAPSHOT -DgenerateBackupPoms=false
