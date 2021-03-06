# Inhibitor-plugin

[![](https://jitpack.io/v/franciscozuccala/inhibitor-plugin.svg)](https://jitpack.io/#franciscozuccala/inhibitor-plugin)

Inhibitor-plugin is a gradle plugin, that allows your library or application to use a Github repository
as a Nexus and upload and use dependencies remotely

## How to use it
Add the repository and Jitpack classpath to ROOTPROJECT's buildscript
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

apply plugin: 'inhibitor'

```

Add the following lines to the module's build.gradle to add the local repository:
```gradle
repositories {
    ...
    maven {url 'http://localhost:8081/nexus/content/repositories/snapshots/'}
    
    //In case of using nexus for download only
    maven { url 'InhibitorWS/inhibitor/sonatype-work/nexus/storage/public'}
}
```

## Configure the tasks
Add the following lines to ROOTPROJECT's build.gradle for each task to configure, 
the password can be replaced by a Github token:

```gradle

configureLocalMavenRepository{
    nexusRepository = "https://github.com/user/repository-as-nexus.git"
    [authenticated "user", "passwordOrKey"] // In case of beeing a private repository or need write access
    [nexusBranch = "master"] //By Default is master
}

configureNexus{
    nexusRepository = "https://github.com/user/repository-as-nexus.git"
    [authenticated "user", "passwordOrKey"] // In case of beeing a private repository or need write access
    [nexusBranch = "master"] //By Default is master
}

saveNexus{
    nexusRepository = "https://github.com/user/repository-as-nexus.git"
    [authenticated "user", "passwordOrKey"] // In case of beeing a private repository or need write access
    [nexusBranch = "master"] //By Default is master
}

```
To enable startNexus task to run in every build, add the following line
in ROOTPROJECT's build.gradle:
```gradle
ext.ENABLE_START_NEXUS = true
```

To configure nexus to be used as a local maven repository
```gradle
ext.CONFIGURE_LOCAL_MAVEN_REPOSITORY = true
```

any of this two properties can be configured as an environment variable
