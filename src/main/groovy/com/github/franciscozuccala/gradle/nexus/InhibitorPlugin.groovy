package com.github.franciscozuccala.gradle.nexus

import com.github.franciscozuccala.gradle.nexus.tasks.ConfigureNexusTask
import com.github.franciscozuccala.gradle.nexus.tasks.SaveNexusTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class InhibitorPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('configureNexus', type: ConfigureNexusTask)
        project.task('saveNexus', type: SaveNexusTask)

    }
}
