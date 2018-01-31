package com.github.franciscozuccala.inhibitor.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class AbstractGithubTask extends DefaultTask {

    private static final String INHIBITOR_REPOSITORY = 'https://github.com/franciscozuccala/inhibitor.git'

    String workspaceDir = "InhibitorWS"

    String inhibitorBranch = "master"

    protected GithubCredentials credentials

    void authenticated(String usr, String pass = null) {
        credentials = new GithubCredentials(usr, pass)
    }

    @TaskAction
    def setup() {

        def rootDir = (project.rootDir.path.contains('buildSrc'))? project.rootDir.parentFile : project.rootDir

        def inhibitorWorkspaceFolder = new File(rootDir, workspaceDir)
        if (!inhibitorWorkspaceFolder.exists()){
            inhibitorWorkspaceFolder.mkdirs()
        }
        def inhibitorFolder = createInhibitorFolder(inhibitorWorkspaceFolder)

        if (haveToExecute(inhibitorFolder)) {
            exe(inhibitorFolder)
        }

    }

    abstract void exe(File inhibitorFolder)

    protected boolean haveToCloneInhibitor(){ return true }

    protected boolean haveToExecute(File inhibitorFolder) { return true }

    private File createInhibitorFolder(File workspaceFolder) {
        println("Configuring inhibitor folder")
        def inhibitorGitFolder = new File(workspaceFolder, "inhibitor")
        if (!inhibitorGitFolder.exists()){
            if (haveToCloneInhibitor()){
                gitClone(workspaceFolder, INHIBITOR_REPOSITORY)
                gitCheckout(inhibitorGitFolder, inhibitorBranch)
            }else{
                inhibitorGitFolder.mkdirs()
            }
        }else{
            if (isGitRepository(inhibitorGitFolder) && !(gitCurrentBranch(inhibitorGitFolder) == inhibitorBranch)){
                gitCheckout(inhibitorGitFolder, inhibitorBranch)
                gitPullFromBranch(inhibitorGitFolder, INHIBITOR_REPOSITORY, inhibitorBranch)
            }
        }

        return new File(workspaceFolder, "inhibitor")
    }

    protected Boolean isGitRepository(File possibleGitFolder){
        def gitFolder = new File(possibleGitFolder, '.git')
        return gitFolder.exists()
    }

    /**
     * This method allows to clone a github repository and return True if it was successful
     * or False otherwise
     * @param gitFolder
     * @param repository
     * @return Boolean
     */
    protected Boolean gitClone(File gitFolder, String repository) {
        println("Clonning repository: $repository")
        def output = project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'clone', repository)

            it.ignoreExitValue = true
        }

        return 0 == output.properties['exitValue']
    }

    protected void gitPushToBranch(File gitFolder, String repository, String branch) {
        println("Pushing changes")
        project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'push', repository, branch)
        }
    }

    protected Boolean gitPullFromBranch(File gitFolder, String repository, String branch) {
        println("Pulling changes")
        def output = project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'pull', repository, branch)

            it.ignoreExitValue = true
        }

        return 0 == output.properties['exitValue']
    }

    protected String gitStatus(File gitFolder) {
        def gitStatusOutput = new ByteArrayOutputStream()

        project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'status', '--porcelain')

            it.standardOutput = gitStatusOutput
        }
        return gitStatusOutput.toString()
    }

    protected String gitCurrentBranch(File gitFolder) {
        def gitStatusOutput = new ByteArrayOutputStream()

        project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'branch', '|', 'grep','\\*')

            it.standardOutput = gitStatusOutput
        }
        return gitStatusOutput.toString()
    }

    protected Boolean gitCheckout(File gitFolder, String branch) {
        println("Changing to branch $branch from ${gitFolder.toString()}")
        def output = project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'checkout', branch)

            it.ignoreExitValue = true
        }

        return 0 == output.properties['exitValue']
    }

    protected Boolean gitSubmoduleAdd(File gitFolder, String repository) {
        println("Adding git submodule")
        def output = project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'submodule', 'add', repository)

            it.ignoreExitValue = true
        }

        return 0 == output.properties['exitValue']
    }

    protected void gitAddAll(File gitFolder) {
        println("Adding all files to commit to repository")
        project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'add', '-A')
        }
    }

    protected void gitAddFiles(File gitFolder, String ... fileNames) {
        println("Adding files to commit to repository")
        fileNames.each { name ->
            project.exec {
                it.workingDir = gitFolder.absolutePath
                it.commandLine('git', 'add', "$name")
            }
        }
    }

    protected void gitRmFile(File gitFolder, String pathToDelete) {
        println("Deleting files in $pathToDelete")
        project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'rm','-rf', '--cached', "$pathToDelete")
        }
    }

    protected Boolean gitCommit(File gitFolder, String message) {
        println("Commiting changes to repository, message: $message")
        def output = project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'commit', '-m', message)

            it.ignoreExitValue = true
        }

        return 0 == output.properties['exitValue']
    }

    class GithubCredentials {
        public def user
        public def password

        GithubCredentials(String usr, String pass) {
            user = usr
            password = pass
        }
    }

    protected String generateAuthenticatedRepository(GithubCredentials credentials, String repositoryUrl){
        String[] paths = repositoryUrl.split('/')
        def repository = paths[paths.size() - 1].split("\\.")[0]
        def username = paths[paths.size() - 2]

        def gitUser = credentials != null ? credentials.user : null
        def gitPass = credentials != null ? credentials.password : null


        if (gitUser != null && gitPass != null) {
            println('Generating Authenticated repository with credentials')
            return  "https://$gitUser:$gitPass@github.com/$username/${repository}.git"
        }

        println('Generating Authenticated repository without credentials')
        return repositoryUrl
    }
}
