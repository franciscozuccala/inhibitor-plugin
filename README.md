# Inhibitor-plugin

[![](https://jitpack.io/v/franciscozuccala/inhibitor-plugin.svg)](https://jitpack.io/#franciscozuccala/inhibitor-plugin)

Inhibitor-plugin is a gradle plugin, that allows your library or application to use a Github repository
as a Nexus and upload and use dependencies remotely

## How to use it
Add the repository and Jitpack classpath to rootProject's buildscript
```gradle
buildScript{
    ...
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
    ...
    dependencies{
        ...
        classpath 'com.github.franciscozuccala:inhibitor-plugin:X.Y.Z'
    }
}
```

Add the following lines to the module's build.gradle  ...

... in case of being an Android Library:
```gradle
apply plugin: 'inhibitor.android-library'
```

... an Android Application:
```gradle
apply plugin: 'inhibitor.android-application'
```

... a Gradle Plugin:
```gradle
apply plugin: 'inhibitor.gradle-plugin'
```

... or just need import dependencies in a build.gradle:
```gradle
apply plugin: 'inhibitor.root'
```

For the new version of inhibitor-plugin use:
```gradle
apply plugin: 'inhibitor'
```

Then add the local repository:
```gradle
repositories {
    ...
    flatDir { dirs "${rootProject.projectDir}/libs" }
}
```
In case of new inhibitor version use:
```gradle
repositories {
    ...
    maven {url 'http://localhost:8081/nexus/content/repositories/snapshots/'}
}
```

## Configure the tasks
Add the following lines to module's build.gradle for each task to configure, 
the password can be replaced by a Github token:

```gradle
importDependencies {
    gitRepository "https://github.com/user/repository.git"
    authenticated user, password

    dependenciesCoordinates = ["groupId:artifactId:version"]
}

importDependenciesByGroup {
    gitRepository "https://github.com/user/repository.git"
    authenticated user, password

    groupsId = ["groupId"]
}

uploadDependenciesByGroup{
    gitRepository "https://github.com/user/repository.git"
    authenticated user, password

    groupsId = ["groupId"]
}

uploadAar{
    gitRepository "https://github.com/user/repository.git"
    authenticated user, password

    groupId = "groupId"
    artifactId = "artifactId"
    version = "version"
}

uploadJar{
    gitRepository "https://github.com/user/repository.git"
    authenticated user, password

    groupId = "groupId"
    artifactId = "artifactId"
    version = "version"
}
```

In case of new version:
```gradle

configureNexus{
    nexusRepository = "https://github.com/user/repository-as-nexus.git"
    [authenticated "usser", "passwordOrKey"] // In case of beeing a private repository or need write access
    [nexusBranch = "master"] //By Default is master
}

saveNexus{
    nexusRepository = "https://github.com/user/repository-as-nexus.git"
    [authenticated "usser", "passwordOrKey"] // In case of beeing a private repository or need write access
    [nexusBranch = "master"] //By Default is master
}

```
To enable importDependencies or importDependenciesByGroup task to run in every build, add the following line
in module's build.gradle:
```gradle
ext.ENABLED_IMPORT_DEPENDENCIES = true

ext.ENABLED_IMPORT_DEPENDENCIES_BY_GROUP = true
```
In new version:
```gradle
ext.ENABLE_START_NEXUS = true
```
In case of need to get this dependencies before applying some plugin, define the task importDependencies/importDependenciesByGroup before aplying the plugin, example: 
![](docs/images/InhibitorExample01.png?raw=true)
