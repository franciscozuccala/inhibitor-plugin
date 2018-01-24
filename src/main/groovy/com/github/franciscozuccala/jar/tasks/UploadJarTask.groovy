package com.github.franciscozuccala.jar.tasks

import com.github.franciscozuccala.common.tasks.AbstractGithubTask
import groovy.io.FileType
import org.gradle.api.tasks.Input

import java.nio.file.Files

class UploadJarTask  extends AbstractGithubTask {

    String defaultJarsDir

    @Input
    String groupId
    @Input
    String artifactId
    @Input
    String version

    @Override
    void exe(File gitFolder) {

        println("Created git folder: ${gitFolder.name}")

        println("GroupId: $groupId, ArtifactId: $artifactId, Version: $version")

        def jarsFolder = new File(gitFolder, "$groupId/$artifactId/$version")
        if (!jarsFolder.exists()) {
            jarsFolder.mkdirs()
        }

        println("Created folder ${jarsFolder.name} for jars")

        saveJarsInFolder(obtainJarsFromProject(), jarsFolder)

        println("Adding jars to repository in $gitFolder.name")

        gitAddAll(gitFolder)

        if (gitCommit("Uploaded jars for $artifactId, version: $version", gitFolder)){
            gitPushToBranch("master", gitFolder)
        }
    }

    private List<File> obtainJarsFromProject() {
        if (defaultJarsDir == null) {
            defaultJarsDir = "${project.buildDir.absolutePath}/libs"
        }
        List<File> listOfJars = []
        def outputsDir = new File(defaultJarsDir)
        if (outputsDir.exists()) {
            println("Obtaining jars from $defaultJarsDir")

            outputsDir.eachFileRecurse(FileType.FILES) { jar ->
                println("Jar called: $jar")
                listOfJars << jar
            }
        }
        println("Finish obtaining jars")
        return listOfJars
    }

    private static void saveJarsInFolder(List<File> jars, File folder) {
        println("Coping jars to $folder.name ")
        jars.eachWithIndex { jar, index ->
            File newJar = new File(folder, "$jar.name")
            println("Adding file named: $jar.name to $folder.name")
            if (newJar.exists()) {
                if (!newJar.name.contains("SNAPSHOT")) {
                    println("Aar called: $newJar.name already exists in the repository")
                    return
                }
                newJar.delete()
            }

            Files.copy(jar.toPath(), newJar.toPath())
        }
        println("Finish coping aars")
    }
}