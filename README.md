# Inhibitor

Es un plugin para gradle, que permite administrar dependencias en un repositorio
remoto de Github y utilizarlo como Nexus

## Como utilizarlo
Agregar el repositorio de jitpack
```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

Agregar el classpath en el build.gradle del rootProject

```
classpath 'com.github.franciscozuccala:inhibitor:X.Y.Z'
```

Agregar las siguientes lineas en el build.gradle del proyecto root:
```
allprojects {
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
}
```

En el modulo agregar la linea:
```
apply plugin: 'com.github.franciscozuccala.inhibitor'
```

## Configurar las tasks
Agregar las siguientes lineas en el build.gradle del modulo para configurar las tasks
```
importDependenciesFromGithub {
    gitRepository "https://github.com/user/repository.git"
    authenticated user, password

    dependencies = ["groupId:artifactId:version"]
}

uploadDependenciesToGithub{
    gitRepository "https://github.com/user/repository.git"
    authenticated user, password

    filterGroups = ["groupId"]
}
```

Para habilitar la ejecucion de la task importDependenciesFromGithub en cada build,
agregar las siguientes lineas en el build.gradle del modulo:
```
ext.ENABLED_IMPORT_DEPENDENCIES_FROM_GITHUB = true
```
