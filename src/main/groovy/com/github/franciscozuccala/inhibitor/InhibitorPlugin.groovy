package com.github.franciscozuccala.inhibitor

import com.github.franciscozuccala.inhibitor.tasks.ConfigureLocalMavenRepositoryTask
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
        project.task('configureLocalMavenRepository', type: ConfigureLocalMavenRepositoryTask)
        project.task('saveNexus', type: SaveNexusTask)
        project.task('startNexus', type: StartNexusTask).dependsOn('configureNexus')
        project.task('stopNexus', type: StopNexusTask).dependsOn('configureNexus')

        project.afterEvaluate {
            if (getBooleanEnvironmentProperty(project, 'ENABLE_START_NEXUS', false)) {
                project.tasks.configureNexus.execute()
                project.tasks.startNexus.execute()
            }else if (getBooleanEnvironmentProperty(project, 'CONFIGURE_LOCAL_MAVEN_REPOSITORY', false)){
                project.tasks.configureLocalMavenRepository.execute()
            }
        }
    }

    private static Boolean getBooleanEnvironmentProperty(Project project, String propertyName, Boolean defaultValue){
        try {
            if (project.hasProperty(propertyName)){
                return project.ext.get(propertyName)
            }
            return (System.getenv(propertyName) != null) ? Boolean.valueOf(System.getenv(propertyName)) : defaultValue
        }catch (ClassCastException e){
            return defaultValue
        }

    }
}
