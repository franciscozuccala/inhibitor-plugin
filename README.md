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

... in case of being a Library:
```gradle
apply plugin: 'inhibitor.aar'
```

... an Application:
```gradle
apply plugin: 'inhibitor.apk'
```

... a Plugin:
```gradle
apply plugin: 'inhibitor.jar'
```

Then add the local repository:
```
repositories {
    ...
    flatDir { dirs "${rootProject.projectDir}/libs" }
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
To enable importDependencies or importDependenciesByGroup task to run in every build, add the following line
in module's build.gradle:
```gradle
ext.ENABLED_IMPORT_DEPENDENCIES = true

ext.ENABLED_IMPORT_DEPENDENCIES_BY_GROUP = true
```
In case of need to get this dependencies before applying some plugin, define the task importDependencies/importDependenciesByGroup before aplying the plugin, example: 
![](docs/images/InhibitorExample01.png?raw=true)
