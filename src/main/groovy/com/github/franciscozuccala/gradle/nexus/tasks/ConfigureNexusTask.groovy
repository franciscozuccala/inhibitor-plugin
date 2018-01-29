package com.github.franciscozuccala.gradle.nexus.tasks

import org.gradle.api.tasks.Input


class ConfigureNexusTask extends AbstractGithubTask{

    @Input
    String nexusRepository

    String nexusVersion = '2.14.5-02'

    String nexusBranch = "master"

    @Override
    void exe(File inhibitorFolder) {
        println("Executing configureNexus task for $nexusRepository")
        def sonatypeWorkFolder = new File(inhibitorFolder, "sonatype-work")
        def nexusCompiledFolder = new File(inhibitorFolder, "nexus-${nexusVersion}.tar.bz2")
        def nexusFolder = new File(inhibitorFolder, "nexus-${nexusVersion}")

        createNexusStorageFolder(sonatypeWorkFolder)

        if (!nexusFolder.exists()){
            if (!nexusCompiledFolder.exists()){
                throw new Exception("The nexus version does not exist in the repository")
            }
            decompileFolder(inhibitorFolder, nexusCompiledFolder)
        }
        println("Done executing")
    }

    private File decompileFolder(File baseFolder, File compiledFolder){
        def name = compiledFolder.name

        project.exec {
            it.workingDir = baseFolder.absolutePath
            it.commandLine('tar', "-jxvf", "$name")
        }

        return new File(baseFolder, "nexus-${nexusVersion}")
    }

    private File createNexusStorageFolder(File sonatypeWorkFolder) {
        println("Creating nexus folder")
        def nexusStorageFolder = new File(sonatypeWorkFolder, "nexus")

        def authenticatedNexusRepository = generateAuthenticatedRepository(credentials, nexusRepository)

        if (!nexusStorageFolder.exists()) {
            String[] paths = nexusRepository.split('/')
            def repository = paths[paths.size() - 1].split("\\.")[0]

            gitClone(sonatypeWorkFolder, authenticatedNexusRepository)

            if (repository != "nexus") {
                project.exec {
                    it.workingDir = sonatypeWorkFolder.absolutePath
                    it.commandLine('mv', "${repository}/", "nexus")
                }
            }

            nexusStorageFolder =  new File(sonatypeWorkFolder, "nexus")
        }

        gitPullFromBranch(authenticatedNexusRepository, nexusBranch, nexusStorageFolder)

        println("Done creating nexus folder")
        return nexusStorageFolder
    }
}
