# Releasing

Requirements:

Have a ~/.mw2/settings.xml

```
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>${Sonatype JIRA username}</username>
      <password>${Sonatype JIRA username}</password>
    </server>
  </servers>
  <profiles>
    <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.keyname>${GPG Key name}</gpg.keyname>
      </properties>
    </profile>
  </profiles>
</settings>
```


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

It takes around ~10 minutes, until the artifact is synced to maven central.
