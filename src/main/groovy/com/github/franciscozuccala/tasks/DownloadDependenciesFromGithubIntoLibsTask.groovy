package com.github.franciscozuccala.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files

class DownloadDependenciesFromGithubIntoLibsTask extends DefaultTask {

    @TaskAction
    void exe() {
        List<File> dependencies = []

        project.configurations.compile.resolvedConfiguration.resolvedArtifacts.each { ResolvedArtifact artifact ->
            def id = artifact.moduleVersion.id
            println "$artifact.file, $id.group, $id.name, $id.version"
            dependencies << artifact.file
        }

        File libs = new File("$project.name/libs")
        libs.mkdirs()

        dependencies.each {
            File newAar = new File(libs, it.name)
            println("Adding file named: $it.name to libs")
            if (newAar.exists()) {
                newAar.delete()
            }

            Files.copy(it.toPath(), newAar.toPath())
        }

    }
}
