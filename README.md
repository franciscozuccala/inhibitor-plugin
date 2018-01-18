# Inhibitor

[![](https://jitpack.io/v/franciscozuccala/inhibitor.svg)](https://jitpack.io/#franciscozuccala/inhibitor)

Inhibitor is a gradle plugin, that allows your library or application to use a Github repository
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
        classpath 'com.github.franciscozuccala:inhibitor:X.Y.Z'
    }
}
```

Add the following lines to the module's build.gradle  ...

... in case of being a library:
```gradle
apply plugin: 'inhibitor.library'
```

... otherwise:
```gradle
apply plugin: 'inhibitor.application'
```
Then add the local repository:
```
repositories {
    ...
    flatDir { dirs "${rootProject.projectDir}/libs" }
}
```

## Configure the tasks
Add the following lines to module's build.gradle for each task to configure:

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
```
To enable importDependencies or importDependenciesByGroup task to run in every build, add the following line
in module's build.gradle:
```gradle
ext.ENABLED_IMPORT_DEPENDENCIES = true

ext.ENABLED_IMPORT_DEPENDENCIES_BY_GROUP = true
```
