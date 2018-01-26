package com.github.franciscozuccala.gradle.nexus.tasks

class StartNexusTask extends AbstractGithubTask{

    String nexusVersion = '2.14.5-02'

    @Override
    void exe(File inhibitorFolder) {
        startNexus(new File(inhibitorFolder, "nexus-${nexusVersion}"))
    }

    private void startNexus(File nexusFolder){
        project.exec {
            it.workingDir = nexusFolder.absolutePath
            it.commandLine("./bin/nexus", "start")
        }
    }
}
