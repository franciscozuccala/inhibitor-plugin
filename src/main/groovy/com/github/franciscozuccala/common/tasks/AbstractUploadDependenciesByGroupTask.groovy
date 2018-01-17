package com.github.franciscozuccala.common.tasks

import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.tasks.Input

import java.nio.file.Files

abstract class AbstractUploadDependenciesByGroupTask extends AbstractGithubTask{

    abstract List<String> listVariants()

    @Input
    List<String> groupsId = []

    @Override
    void exe(File gitFolder) {
        println("Executing uploadDependenciesByGroup task ...")

        if (groupsId == null || groupsId.isEmpty()) {
            throw new Exception("No groups of artifacts added for filter dependencies aars, to add groupsId use " +
                    "the command 'groupsId = ['myGroup']'")
        }

        println("Resolving artifacts to upload ...")

        def variants = listVariants()

        println("Application variants: $variants")

        variants.each { variant ->
            project.configurations.getByName(variant).resolvedConfiguration.resolvedArtifacts.each { ResolvedArtifact artifact ->
                def id = artifact.moduleVersion.id

                if (groupsId.contains(id.group)) {
                    def aarFolder = new File(gitFolder, "$id.group/$id.name/$id.version")

                    println("Adding file named: $artifact.file.name to $aarFolder.name")

                    if (!aarFolder.exists()){
                        aarFolder.mkdirs()
                    }else{
                        if (!aarFolder.name.contains("SNAPSHOT")){
                            return
                        }
                    }

                    File newAar = new File(aarFolder, "$artifact.file.name")

                    if (newAar.exists()) {
                        newAar.delete()
                    }

                    Files.copy(artifact.file.toPath(), newAar.toPath())
                }
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
