# Release

```
# Do a snapshot deployment
mvn clean deploy -P release

# Do a release deployment
mvn versions:set -DremoveSnapshot=true -DgenerateBackupPoms=false
mvn clean deploy -P release
replace version in README.md

git tag v0.0.1
git push origin --tags

mvn versions:set -DnextSnapshot=true -DgenerateBackupPoms=false
git add pom.xml
git commit -m "bump version"
git push
```
