package com.github.franciscozuccala.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class AbstractGithubTask extends DefaultTask {

    @Input
    String gitRepository

    String defaultFolderDir

    private GithubCredentials credentials

    private String authenticatedRepo

    void gitRepository(String repo) {
        gitRepository = repo
    }

    void authenticated(String usr, String pass = null) {
        credentials = new GithubCredentials(usr, pass)
    }

    protected String getAuthenticatedRepo() {
        return authenticatedRepo
    }

    @TaskAction
    def setup() {
        if (gitRepository == null) {
            throw new Exception("No github repository to publish aars")
        }
        def defaultFolder = createDefaultFolder()
        try {
            if (haveToExecute()) {
                exe(createGitFolder(defaultFolder))
            }
        } finally {
            defaultFolder.deleteDir()
        }
    }

    abstract void exe(File gitFolder)

    boolean haveToExecute() { return true }

    protected File createGitFolder(File folder) {
        def gitFolder = new File(folder, "repository")
        String[] paths = gitRepository.split('/')
        def repository = paths[paths.size() - 1].split("\\.")[0]
        def username = paths[paths.size() - 2]

        authenticatedRepo = gitRepository

        def gitUser = credentials != null ? credentials.user : null
        def gitPass = credentials != null ? credentials.password : null

        println("Creating folder: $gitFolder.name")
        gitFolder.mkdir()

        if (gitUser != null && gitPass != null) {
            authenticatedRepo = "https://$gitUser:$gitPass@github.com/$username/${repository}.git"
        }

        gitClone(gitFolder)

        return new File(gitFolder, repository)
    }

    protected File createDefaultFolder() {
        if (defaultFolderDir == null) {
            defaultFolderDir = "dependencies-manager"
        }

        def folder = new File(defaultFolderDir)
        if (folder.exists()) {
            folder.deleteDir()
        }
        folder.mkdirs()

        return folder
    }

    protected void gitPushToBranch(String branch, File gitFolder) {
        println("Pushing to branch $branch in $gitRepository")
        project.exec {
            it.workingDir = gitFolder.absolutePath

            it.commandLine('git', 'push', authenticatedRepo, branch)
        }
    }

    protected void gitPullFromBranch(String branch, File gitFolder) {
        println("Pulling from branch: $branch in $gitRepository")
        project.exec {
            it.workingDir = gitFolder.absolutePath

            it.commandLine('git', 'pull', authenticatedRepo, branch)
        }
    }

    protected void gitClone(File gitFolder) {
        println("Clonning repository: $gitRepository")
        project.exec {
            it.workingDir = gitFolder.absolutePath

            it.commandLine('git', 'clone', authenticatedRepo)
        }
    }

    protected void gitCommit(String message, File gitFolder) {
        println("Commiting changes to repository: $gitRepository, message: $message")
        project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'commit', '-m', message)
        }

    }

    protected void gitAddAll(File gitFolder) {
        println("Adding all files to commit to repository: $gitRepository")
        project.exec {
            it.workingDir = gitFolder.absolutePath

            it.commandLine('git', 'add', '-A')
        }
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

    class GithubCredentials {
        public def user
        public def password

        GithubCredentials(String usr, String pass) {
            user = usr
            password = pass
        }
    }
}
