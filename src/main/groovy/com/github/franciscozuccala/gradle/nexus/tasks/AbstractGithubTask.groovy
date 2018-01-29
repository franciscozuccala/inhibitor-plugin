package com.github.franciscozuccala.gradle.nexus.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class AbstractGithubTask extends DefaultTask {

    String workspaceDir = "InhibitorWS"

    String inhibitorBranch = "master"

    protected GithubCredentials credentials

    void authenticated(String usr, String pass = null) {
        credentials = new GithubCredentials(usr, pass)
    }

    @TaskAction
    def setup() {
        def inhibitorFolder = createInhibitorFolder()
        if (haveToExecute(inhibitorFolder)) {
            exe(inhibitorFolder)
        }
    }

    abstract void exe(File inhibitorFolder)

    boolean haveToExecute(File inhibitorFolder) { return true }

    private File createInhibitorFolder() {
        def folder = new File(workspaceDir)
        if (!folder.exists()) {
            folder.mkdirs()
            gitClone(folder, 'https://github.com/franciscozuccala/inhibitor.git')
            return new File(folder, "inhibitor")
        }

        def gitFolder = new File(folder, "inhibitor")
        if (gitFolder.exists()){
            if (gitPullFromBranch('https://github.com/franciscozuccala/inhibitor.git', inhibitorBranch, gitFolder)){
                return gitFolder
            }
            gitFolder.deleteDir()
        }
        gitClone(folder, 'https://github.com/franciscozuccala/inhibitor.git')

        return new File(folder, "inhibitor")
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

    protected void gitPushToBranch(String repository, String branch, File gitFolder) {
        println("Pushing changes")
        project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'push', repository, branch)
        }
    }

    protected Boolean gitPullFromBranch(String repository, String branch, File gitFolder) {
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

    protected Boolean gitCheckout(File gitFolder, String branch) {
        println("Changing to branch $branch")
        def output = project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'checkout', branch)

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

    protected Boolean gitCommit(String message, File gitFolder) {
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
