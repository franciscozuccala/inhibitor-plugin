package com.github.franciscozuccala.gradle.nexus

import com.github.franciscozuccala.gradle.nexus.tasks.ConfigureNexusTask
import com.github.franciscozuccala.gradle.nexus.tasks.SaveNexusTask
import com.github.franciscozuccala.gradle.nexus.tasks.StartNexusTask
import com.github.franciscozuccala.gradle.nexus.tasks.StopNexusTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class InhibitorPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('configureNexus', type: ConfigureNexusTask)
        project.task('saveNexus', type: SaveNexusTask)
        project.task('startNexus', type: StartNexusTask).dependsOn('configureNexus')
        project.task('stopNexus', type: StopNexusTask).dependsOn('configureNexus')

        project.afterEvaluate {
            if (project.ext.has('ENABLE_START_NEXUS') ? project.ext.ENABLE_START_NEXUS : false) {
                project.tasks.startNexus.execute()
            }
        }
    }
}
