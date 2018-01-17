package com.github.franciscozuccala.library.tasks

import com.github.franciscozuccala.common.tasks.AbstractGithubTask
import groovy.io.FileType
import org.gradle.api.tasks.Input

import java.nio.file.Files

class UploadAarTask extends AbstractGithubTask {

    String defaultAarsDir

    @Input
    String groupId
    @Input
    String artifactId
    @Input
    String version

    @Override
    void exe(File gitFolder) {

        println("Created git folder: ${gitFolder.name}")

        println("ArtifactId: $artifactId, GroupId: $groupId, Version: $version")

        def aarsFolder = new File(gitFolder, "$groupId/$artifactId/$version")
        if (!aarsFolder.exists()) {
            aarsFolder.mkdirs()
        }

        println("Created folder ${aarsFolder.name} for aars")

        saveAarsInFolder(obtainAarsFromProject(), aarsFolder)

        println("Adding aars to repository in $gitFolder.name")

        gitAddAll(gitFolder)

        gitCommit("Upgraded aars for $artifactId, version: $version", gitFolder)

        gitPushToBranch("master", gitFolder)
    }

    private List<File> obtainAarsFromProject() {
        if (defaultAarsDir == null) {
            defaultAarsDir = "$project.name/build/outputs/aar"
        }
        println("Obtaining aars from $defaultAarsDir")
        List<File> listOfAars = []
        def outputsDir = new File(defaultAarsDir)
        if (outputsDir.exists()) {
            outputsDir.eachFileRecurse(FileType.FILES) { aar ->
                listOfAars << aar
            }
        }
        println("Finish obtaining aars")
        return listOfAars
    }

    private static void saveAarsInFolder(List<File> aars, File folder) {
        println("Coping aars to $folder.name ")
        aars.eachWithIndex { aar, index ->
            File newAar = new File(folder, "$aar.name")
            println("Adding file named: $aar.name to $folder.name")
            if (newAar.exists()) {
                if (newAar.name.contains("SNAPSHOT")){
                    println("Aar called: $newAar.name already exists in the repository")
                    return
                }
                newAar.delete()
            }

            Files.copy(aar.toPath(), newAar.toPath())
        }
        println("Finish coping aars")
    }
}