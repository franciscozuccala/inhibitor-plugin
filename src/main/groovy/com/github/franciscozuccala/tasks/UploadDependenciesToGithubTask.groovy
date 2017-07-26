package com.github.franciscozuccala.tasks

import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.tasks.Input

import java.nio.file.Files

class UploadDependenciesToGithubTask extends AbstractGithubTask {

    @Input
    List<String> filterGroups = []

    @Override
    void exe(File gitFolder) {
        if (filterGroups == null || filterGroups.isEmpty()) {
            throw new Exception("No groups of artifacts added for filter dependencies aars, to add filterGroups use " +
                    "the command 'filterGroups = ['myGroup']'")
        }

        project.configurations.compile.resolvedConfiguration.resolvedArtifacts.each { ResolvedArtifact artifact ->
            def id = artifact.moduleVersion.id

            if (filterGroups.contains(id.group)) {
                def aarFolder = new File(gitFolder, "$id.group/$id.name/$id.version")
                if (!aarFolder.exists()) {
                    aarFolder.mkdirs()
                }
                File newAar = new File(aarFolder, "$artifact.file.name")
                println("Adding file named: $artifact.file.name to $aarFolder.name")
                if (newAar.exists()) {
                    newAar.delete()
                }

                Files.copy(artifact.file.toPath(), newAar.toPath())
            }
        }

        println(gitStatus(gitFolder))
        if (gitStatus(gitFolder) == null || gitStatus(gitFolder).isEmpty()) {
            return
        }

        println("Adding aars to repository in $gitFolder.name")

        gitAddAll(gitFolder)

        gitCommit("Upgraded dependencies for $project.name", gitFolder)

        gitPushToBranch("master", gitFolder)
    }
}
