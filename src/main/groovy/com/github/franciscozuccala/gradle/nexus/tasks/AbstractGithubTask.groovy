package com.github.franciscozuccala.gradle.nexus.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class AbstractGithubTask extends DefaultTask {

    String workspaceDir

    @TaskAction
    def setup() {
        def inhibitorFolder = createInhibitorFolder()
        if (haveToExecute()) {
            exe(inhibitorFolder)
        }
    }

    abstract void exe(File gitFolder)

    boolean haveToExecute() { return true }

    protected File createInhibitorFolder() {
        if (workspaceDir == null) {
            workspaceDir = "Workspace"
        }
        def folder = new File(workspaceDir)
        if (!folder.exists()) {
            folder.mkdirs()
            gitClone(folder, 'https://github.com/franciscozuccala/inhibitor.git')
            return new File(folder, "inhibitor")
        }

        def gitFolder = new File(folder, "inhibitor")
        if (gitFolder.exists()){
            if (gitPullFromBranch('https://github.com/franciscozuccala/inhibitor.git', "master", gitFolder)){
                return gitFolder
            }
            gitFolder.deleteDir()
        }
        gitClone(folder, 'https://github.com/franciscozuccala/inhibitor.git')

        return new File(folder, "inhibitor")
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

    protected Boolean gitClone(File gitFolder, String repository) {
        println("Clonning repository: $repository")
        def output = project.exec {
            it.workingDir = gitFolder.absolutePath
            it.commandLine('git', 'clone', repository)

            it.ignoreExitValue = true
        }

        return 0 == output.properties['exitValue']
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

    protected void gitAddAll(File gitFolder) {
        println("Adding all files to commit to repository")
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
