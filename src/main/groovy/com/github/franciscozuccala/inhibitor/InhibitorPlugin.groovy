package com.github.franciscozuccala.inhibitor

import com.github.franciscozuccala.inhibitor.tasks.ConfigureDownloadOnlyAndroidTask
import com.github.franciscozuccala.inhibitor.tasks.ConfigureNexusTask
import com.github.franciscozuccala.inhibitor.tasks.SaveNexusTask
import com.github.franciscozuccala.inhibitor.tasks.StartNexusTask
import com.github.franciscozuccala.inhibitor.tasks.StopNexusTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class InhibitorPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('configureNexus', type: ConfigureNexusTask)
        project.task('configureDownloadOnlyAndroid', type: ConfigureDownloadOnlyAndroidTask)
        project.task('saveNexus', type: SaveNexusTask)
        project.task('startNexus', type: StartNexusTask).dependsOn('configureNexus')
        project.task('stopNexus', type: StopNexusTask).dependsOn('configureNexus')

        project.afterEvaluate {
            if (project.ext.has('ENABLE_START_NEXUS') ? project.ext.ENABLE_START_NEXUS : false) {
                project.tasks.configureNexus.execute()
                project.tasks.startNexus.execute()
            }else if (project.ext.has('CONFIGURE_DOWNLOAD_ONLY_ANDROID') ? project.ext.CONFIGURE_DOWNLOAD_ONLY_ANDROID : false){
                project.tasks.configureDownloadOnlyAndroid.execute()
            }
        }
    }
}
