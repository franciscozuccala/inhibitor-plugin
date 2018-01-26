package com.github.franciscozuccala.gradle.nexus.tasks

import org.gradle.api.tasks.Input


class ConfigureNexusTask extends AbstractGithubTask{

    @Input
    String nexusRepository

    String nexusVersion = '2.14.5-02'

    private GithubCredentials credentials

    void authenticated(String usr, String pass = null) {
        credentials = new GithubCredentials(usr, pass)
    }

    @Override
    void exe(File inhibitorFolder) {
        def sonatypeWorkFolder = new File(inhibitorFolder, "sonatype-work")
        def nexusStorageFolder = new File(sonatypeWorkFolder, "nexus")
        def nexusCompiled = new File(inhibitorFolder, "nexus-${nexusVersion}.tar.bz2")
        def nexusFolder = new File(inhibitorFolder, "nexus-${nexusVersion}")

        String[] paths = nexusRepository.split('/')
        def repository = paths[paths.size() - 1].split("\\.")[0]
        def username = paths[paths.size() - 2]

        def authenticatedNexusRepository = nexusRepository

        def gitUser = credentials != null ? credentials.user : null
        def gitPass = credentials != null ? credentials.password : null


        if (gitUser != null && gitPass != null) {
            authenticatedNexusRepository = "https://$gitUser:$gitPass@github.com/$username/${repository}.git"
        }

        if (!nexusStorageFolder.exists()){
            nexusStorageFolder = createNexusStorageFolder(authenticatedNexusRepository, repository, sonatypeWorkFolder)
        }

        gitPullFromBranch(authenticatedNexusRepository, "master", nexusStorageFolder)
    }

    protected File createNexusStorageFolder(String authenticatedNexusRepository, String repository, File sonatypeWorkFolder) {

        gitClone(sonatypeWorkFolder, authenticatedNexusRepository)

        if (repository != "nexus"){
            project.exec {
                it.workingDir = sonatypeWorkFolder.absolutePath
                it.commandLine('mv', "${repository}/", "nexus/")
            }
        }

        return new File(sonatypeWorkFolder, "nexus")
    }
}
