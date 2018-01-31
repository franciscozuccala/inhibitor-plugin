package com.github.franciscozuccala.legacy.common.tasks

import groovy.io.FileType
import org.gradle.api.tasks.Input

import java.nio.file.Files

class ImportDependenciesByGroupTask extends AbstractGithubTask {

    @Input
    List<String> groupsId = []

    @Override
    void exe(File gitFolder) {
        groupsId.each {
            List<File> dependencies = []
            println("Getting dependencies for groupId: $it")
            def artifactsFolder = new File(gitFolder, it)
            if (artifactsFolder.exists()) {
                artifactsFolder.eachFileRecurse(FileType.FILES) { dependency ->
                    dependencies << dependency
                }

                File dependenciesFolder = project.rootProject.file("libs")
                if (!dependenciesFolder.exists()) {
                    dependenciesFolder.mkdirs()
                }

                println("Starting copying dependencies for groupId: $it to libs")

                dependencies.each {
                    println("Copying dependency: $it.name to libs")
                    File newDependency = new File(dependenciesFolder, it.name)
                    if (newDependency.exists()) {
                        newDependency.delete()
                    }

                    Files.copy(it.toPath(), newDependency.toPath())
                }

                println("Finish copying dependencies for groupId: $it")
            }
        }
    }
}
