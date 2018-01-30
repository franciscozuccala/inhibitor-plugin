package com.github.franciscozuccala.inhibitor.tasks

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

            it.ignoreExitValue = true
        }
        println("Done starting nexus, remember it is hosted on http://localhost:8081/nexus")
    }
}
