package com.github.franciscozuccala.inhibitor.tasks

import org.gradle.api.tasks.Input

class SaveNexusTask extends AbstractGithubTask {

    @Input
    String nexusRepository

    String nexusBranch = "master"

    @Override
    protected boolean haveToCloneInhibitor() {
        return false
    }

    @Override
    void exe(File inhibitorFolder) {
        println("Executing saveNexus task on $nexusRepository")
        File sonatypeWorkFolder = new File(inhibitorFolder, 'sonatype-work')
        File nexusFolder = new File(sonatypeWorkFolder, 'nexus')

        if (!sonatypeWorkFolder.exists() || !nexusFolder.exists()){
            throw new Exception("Nexus folder does not exist inside sonatype-work/ directory, " +
                    "please configure your nexus first")
        }

        if (!gitCheckout(nexusFolder, nexusBranch)){
            throw new Exception("The branch $nexusBranch does not exists")
        }

        gitAddAll(nexusFolder)

        if (gitCommit(nexusFolder, 'Saving changes for nexus')){
            gitPushToBranch(nexusFolder, generateAuthenticatedRepository(credentials, nexusRepository), nexusBranch)
        }
        println("Done executing saveNexus")
    }
}
