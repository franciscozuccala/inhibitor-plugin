package com.github.franciscozuccala.legacy.common.tasks

import org.gradle.api.tasks.Input

class ImportDependenciesTask extends AbstractGithubTask {

    @Input
    List<String> dependenciesCoordinates = []

    @Override
    boolean haveToExecute() {
        def mustGetDependencies = false
        def libsFolder = project.rootProject.file("libs")
        println("Getting dependencies from $libsFolder.absolutePath")
        dependenciesCoordinates.each {
            def dependencySplit = it.split(":")

            def listOfFiles = libsFolder.listFiles().findAll{ it.name.contains(dependencySplit[1]) && it.name.contains(dependencySplit[2]) }
            if (listOfFiles.isEmpty()){
                mustGetDependencies = true
                return
            }
        }
        return mustGetDependencies
    }

    @Override
    void exe(File gitFolder) {
        dependenciesCoordinates.each {
            println("Obtaining dependencies for $it")

            def dependencySplit = it.split(":")
            def dependenciesFolder = new File(gitFolder, "${dependencySplit[0]}/${dependencySplit[1]}/${dependencySplit[2]}")

            if (dependenciesFolder.exists()) {
                println("Obtaining dependencies from $dependenciesFolder.name")
                project.copy {
                    it.from dependenciesFolder.absolutePath
                    it.into "libs"
                }
            }
        }
    }

}
