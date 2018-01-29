package com.github.franciscozuccala.gradle.nexus.tasks

class StopNexusTask extends AbstractGithubTask{

    String nexusVersion = '2.14.5-02'

    @Override
    void exe(File inhibitorFolder) {
        startNexus(new File(inhibitorFolder, "nexus-${nexusVersion}"))
    }

    private void startNexus(File nexusFolder){
        println("Stopping nexus")
        project.exec {
            it.workingDir = nexusFolder.absolutePath
            it.commandLine("./bin/nexus", "stop")
        }
        println("Nexus stopped")
    }
}
