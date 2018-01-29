package com.github.franciscozuccala.gradle.nexus.tasks

class StartNexusTask extends AbstractGithubTask{

    String nexusVersion = '2.14.5-02'

    @Override
    void exe(File inhibitorFolder) {
        startNexus(new File(inhibitorFolder, "nexus-${nexusVersion}"))
    }

    private void startNexus(File nexusFolder){
        println("Starting nexus")
        project.exec {
            it.workingDir = nexusFolder.absolutePath
            it.commandLine("./bin/nexus", "start")
        }
        println("Nexus started")
        println("Remember it is hosted on http://localhost:8081/nexus")
    }
}
