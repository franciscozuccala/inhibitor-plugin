package com.github.franciscozuccala.common.tasks

import groovy.io.FileType
import org.gradle.api.tasks.Input

import java.nio.file.Files

class ImportDependenciesByGroupTask extends AbstractGithubTask {

    @Input
    List<String> groupsId = []

    @Override
    void exe(File gitFolder) {
        groupsId.each {
            List<File> aars = []
            println("Getting dependencies for groupId: $it")
            def artifactsFolder = new File(gitFolder, it)
            if (artifactsFolder.exists()) {
                artifactsFolder.eachFileRecurse(FileType.FILES) { aar ->
                    aars << aar
                }

                File aarsFolder = project.rootProject.file("libs")
                if (!aarsFolder.exists()) {
                    aarsFolder.mkdirs()
                }

                println("Starting copying aars for groupId: $it to libs")

                aars.each {
                    println("Copying aar: $it.name to libs")
                    File newAar = new File(aarsFolder, it.name)
                    if (newAar.exists()) {
                        newAar.delete()
                    }

                    Files.copy(it.toPath(), newAar.toPath())
                }

                println("Finish copying aars for groupId: $it")
            }
        }
    }
}
