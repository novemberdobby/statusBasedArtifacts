## Note

Superseded by artifact options in 2019.1+: https://www.jetbrains.com/help/teamcity/what-s-new-in-teamcity-2019-1.html#WhatsNewinTeamCity2019.1-Buildartifactspublishingoptions

# StatusBasedArtifacts - TeamCity plugin to optionally publish artifacts based on the build status
SBA supports "only on failure" and "only on success" of the target configuration. It was written in support of the use cases here: [TW-12194 - Option to prevent artifacts from being published for a failed build](https://youtrack.jetbrains.com/issue/TW-12194).

## Usage
Add the feature to any build config or template:
![build_feature](/images/build_feature.png)

## Missing artifacts
Any missing files are reported in the same way as standard publishes:
![missing_files](/images/missing_files.png)

## Note on archives
Publishes are subject to the same issues as [service messages](https://confluence.jetbrains.com/display/TCD18/Build+Script+Interaction+with+TeamCity#BuildScriptInteractionwithTeamCity-PublishingArtifactswhiletheBuildisStillinProgress). Most notably, publishes made in separate "passes" will overwrite archives (e.g .zip files) rather than updating them. A "pass" can be:
* Publishes from _general settings_ of the configuration (once on build finish)
* Publishes from service messages (as above, can happen at any time)
* Publishes from this plugin (once on build finish, regardless of how many features are present)

## Building
This plugin is built with Maven. To compile it, run the following in the root folder:

```
mvn package
```