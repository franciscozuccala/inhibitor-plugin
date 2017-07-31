package com.github.franciscozuccala.tasks

import groovy.io.FileType
import org.gradle.api.tasks.Input

import java.nio.file.Files

class ImportDependenciesByGroupFromGithubTask extends AbstractGithubTask {

    @Input
    List<String> groupsId = []

    @Override
    void exe(File gitFolder) {
        List<File> aars = []
        groupsId.each {
            def artifactsFolder = new File(gitFolder, it)
            if (artifactsFolder.exists()) {
                artifactsFolder.eachFileRecurse(FileType.FILES) { aar ->
                    aars << aar
                }

                File aarsFolder = project.rootProject.file("libs")
                if (!aarsFolder.exists()) {
                    aarsFolder.mkdirs()
                }

                aars.each {
                    File newAar = new File(aarsFolder, it.name)
                    if (newAar.exists()) {
                        newAar.delete()
                    }

                    Files.copy(it.toPath(), newAar.toPath())
                }
            }
        }

    }
}
