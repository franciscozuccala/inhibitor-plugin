# Inhibitor

[![](https://jitpack.io/v/franciscozuccala/inhibitor.svg)](https://jitpack.io/#franciscozuccala/inhibitor)

Es un plugin para gradle, que permite administrar dependencias en un repositorio
remoto de Github y utilizarlo como Nexus

## Como utilizarlo
Agregar el repositorio y el classpath de jitpack en el buildscript del rootProject
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

Agregar las siguientes lineas en el build.gradle del modulo:
```gradle
apply plugin: 'com.github.franciscozuccala.inhibitor'
...
repositories {
    ...
    flatDir { dirs "${rootProject.projectDir}/libs" }
}
```

## Configurar las tasks
Agregar las siguientes lineas en el build.gradle del modulo para configurar las tasks
```gradle
importDependenciesFromGithub {
    gitRepository "https://github.com/user/repository.git"
    authenticated user, password

    dependenciesCoordinates = ["groupId:artifactId:version"]
}

uploadDependenciesByGroupToGithub{
    gitRepository "https://github.com/user/repository.git"
    authenticated user, password

    groupsId = ["groupId"]
}
```

Para habilitar la ejecucion de la task importDependenciesFromGithub en cada build,
agregar la siguiente linea en el build.gradle del modulo:
```gradle
ext.ENABLED_IMPORT_DEPENDENCIES_FROM_GITHUB = true
```
