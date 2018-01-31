package com.github.franciscozuccala.inhibitor.tasks

import org.gradle.api.tasks.Input


class ConfigureDownloadOnlyAndroidTask extends AbstractGithubTask{

    @Input
    String nexusRepository

    String nexusBranch = "master"

    @Override
    protected boolean haveToCloneInhibitor() {
        return false
    }

    @Override
    void exe(File inhibitorFolder) {
        println("Executing configureDownloadOnlyAndroid task for $nexusRepository")
        def sonatypeWorkFolder = new File(inhibitorFolder, "sonatype-work")
        if (!sonatypeWorkFolder.exists()){
            sonatypeWorkFolder.mkdirs()
        }

        createNexusStorageFolder(sonatypeWorkFolder)

        println("Done executing, remember the artifacts are on ${workspaceDir}/inhibitor/sonatype-work/nexus/storage/content/groups/public")
    }

    private File createNexusStorageFolder(File sonatypeWorkFolder) {
        println("Creating nexus folder")
        def nexusStorageFolder = new File(sonatypeWorkFolder, "nexus")

        def authenticatedNexusRepository = generateAuthenticatedRepository(credentials, nexusRepository)

        if (!isGitRepository(nexusStorageFolder)){
            if (nexusStorageFolder.exists()){
                nexusStorageFolder.deleteDir()
            }

            nexusStorageFolder = cleanNexusStorageFolderInstall(sonatypeWorkFolder, authenticatedNexusRepository)
        }

        gitCheckout(nexusStorageFolder, nexusBranch)

        gitPullFromBranch(nexusStorageFolder, authenticatedNexusRepository, nexusBranch)

        println("Done creating nexus folder")
        return nexusStorageFolder
    }

    private File cleanNexusStorageFolderInstall(File sonatypeWorkFolder, String authenticatedNexusRepository) {
        String[] paths = nexusRepository.split('/')
        def repository = paths[paths.size() - 1].split("\\.")[0]

        gitClone(sonatypeWorkFolder, authenticatedNexusRepository)

        println("Checking name of repository")

        if (repository != "nexus") {
            println("Renaming repository")
            renameFolderToNexus(sonatypeWorkFolder, repository)
        }

        return new File(sonatypeWorkFolder, "nexus")
    }

    private void renameFolderToNexus(File sonatypeWorkFolder, String repositoryName){
        def nexusStorageFolder = new File(sonatypeWorkFolder, "nexus")

        if (nexusStorageFolder.exists()){
            nexusStorageFolder.deleteDir()
        }

        project.exec {
            it.workingDir = sonatypeWorkFolder.absolutePath
            it.commandLine('mv', "${repositoryName}/", "nexus")
        }
    }
}
