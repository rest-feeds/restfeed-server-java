# Release

mvn clean deploy -P release

mvn versions:set -DnewVersion=0.0.1
mvn clean deploy -P release
replace version in README.md

git tag 0.0.1

mvn versions:set -DnewVersion=0.0.2-SNAPSHOT
