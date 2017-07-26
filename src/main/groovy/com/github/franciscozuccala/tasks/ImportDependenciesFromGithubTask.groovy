package com.github.franciscozuccala.tasks

import org.gradle.api.tasks.Input

class ImportDependenciesFromGithubTask extends AbstractGithubTask {

    @Input
    List<String> dependencies = []

    @Override
    boolean haveToExecute() {
        def mustGetDependencies= false
        def libsFolder = project.file("libs")
        println("Getting dependencies from $libsFolder.absolutePath")
        dependencies.each {
            def dependencySplit = it.split(":")

            def listOfFiles = libsFolder.listFiles().findAll{ it.name.contains(dependencySplit[1]) && it.name.contains(dependencySplit[2]) }
            if (listOfFiles.isEmpty()){
                mustGetDependencies = true
                return true
            }
        }
        return mustGetDependencies
    }

    @Override
    void exe(File gitFolder) {
        dependencies.each {
            println("Obtaining aars for $it")

            def dependencySplit = it.split(":")
            def aarsFolder = new File(gitFolder, "${dependencySplit[0]}/${dependencySplit[1]}/${dependencySplit[2]}")

            println("Obtaining aars from $aarsFolder.name")

            if (aarsFolder.exists()) {
                println(aarsFolder)
                project.copy {
                    it.from aarsFolder.absolutePath
                    it.into "libs"
                }
            }
        }
    }

}
