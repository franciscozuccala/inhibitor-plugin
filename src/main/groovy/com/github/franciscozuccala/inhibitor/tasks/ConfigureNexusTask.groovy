package com.github.franciscozuccala.inhibitor.tasks

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

            if (!gitSubmoduleAdd(sonatypeWorkFolder, authenticatedNexusRepository)){
                deleteSubmodule(sonatypeWorkFolder.parentFile, repository)
                gitSubmoduleAdd(sonatypeWorkFolder, authenticatedNexusRepository)
            }

            if (repository != "nexus") {
                renameFolderToNexus(sonatypeWorkFolder.parentFile, repository)
            }

            nexusStorageFolder =  new File(sonatypeWorkFolder, "nexus")
        }

        gitCheckout(nexusStorageFolder, nexusBranch)

        gitPullFromBranch(authenticatedNexusRepository, nexusBranch, nexusStorageFolder)

        println("Done creating nexus folder")
        return nexusStorageFolder
    }

    private void deleteSubmodule(File inhibitorFile, String repositoryName){
        println("Deleting git submodule")

        def gitFolder = new File(inhibitorFile, '.git')

        def gitModulesFile = new File(inhibitorFile, '.gitmodules')
        if (gitModulesFile.exists()){
            gitModulesFile.delete()
            gitAddFiles(inhibitorFile, '.gitmodules')
        }

        def gitConfigFile = new File(gitFolder, 'config')
        def tempGitConfigFile = new File(gitFolder, 'tempConfigFile')

        BufferedReader reader = new BufferedReader(new FileReader(gitConfigFile))
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempGitConfigFile))

        String lineToRemove = "[submodule \"sonatype-work/${repositoryName}\"]"
        Integer lineCountDeleted = 2
        String currentLine

        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim()
            if(trimmedLine.equals(lineToRemove) || (lineCountDeleted<2 && lineCountDeleted>=0)){
                lineCountDeleted--
                continue
            }
            writer.write(currentLine + System.getProperty("line.separator"))
        }
        writer.close()
        reader.close()
        tempGitConfigFile.renameTo(gitConfigFile)

        gitRmFile(inhibitorFile, "sonatype-work/$repositoryName")

        def gitModuleFolder = new File(gitFolder, "modules/sonatype-work")
        if (gitModuleFolder.exists()){
            gitModuleFolder.deleteDir()
        }
    }

    private void renameFolderToNexus(File inhibitorFolder, String repositoryName){
        def sonatypeWorkFolder = new File(inhibitorFolder, 'sonatype-work')

        project.exec {
            it.workingDir = sonatypeWorkFolder.absolutePath
            it.commandLine('mv', "${repositoryName}/", "nexus")
        }

        def gitConfigFile = new File(sonatypeWorkFolder, 'nexus/.git')
        def tempGitConfigFile = new File(sonatypeWorkFolder, 'nexus/.tempGit')

        BufferedReader gitConfigFileReader = new BufferedReader(new FileReader(gitConfigFile))
        BufferedWriter gitConfigFileWriter = new BufferedWriter(new FileWriter(tempGitConfigFile))

        String currentLine

        while((currentLine = gitConfigFileReader.readLine()) != null) {
            String trimmedLine = currentLine.trim()
            if(trimmedLine == "gitdir: ../../.git/modules/sonatype-work/$repositoryName"){
                currentLine = currentLine.replace("$repositoryName","nexus")
            }
            gitConfigFileWriter.write(currentLine + System.getProperty("line.separator"))
        }
        gitConfigFileWriter.close()
        gitConfigFileReader.close()
        tempGitConfigFile.renameTo(gitConfigFile)

        def gitSonatypeWorkFolder = new File(inhibitorFolder, '.git/modules/sonatype-work')
        project.exec {
            it.workingDir = gitSonatypeWorkFolder.absolutePath
            it.commandLine('mv', "${repositoryName}/", "nexus")
        }

        def moduleConfigFile = new File(gitSonatypeWorkFolder, 'nexus/config')
        def tempModuleConfigFile = new File(gitSonatypeWorkFolder, 'nexus/tempConfig')

        BufferedReader reader = new BufferedReader(new FileReader(moduleConfigFile))
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempModuleConfigFile))

        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim()
            if(trimmedLine == "worktree = ../../../../sonatype-work/$repositoryName"){
                currentLine = currentLine.replace("$repositoryName","nexus")
            }
            writer.write(currentLine + System.getProperty("line.separator"))
        }
        writer.close()
        reader.close()
        tempModuleConfigFile.renameTo(moduleConfigFile)
    }
}
