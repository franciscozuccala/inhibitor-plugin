package com.github.franciscozuccala.jar

import com.github.franciscozuccala.common.tasks.ImportDependenciesByGroupTask
import com.github.franciscozuccala.common.tasks.ImportDependenciesTask
import com.github.franciscozuccala.jar.tasks.UploadJarTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class InhibitorJarPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('uploadJar', type: UploadJarTask).dependsOn "build"
        project.task('importDependencies', type: ImportDependenciesTask)
        project.task('importDependenciesByGroup', type: ImportDependenciesByGroupTask)

        project.afterEvaluate {
            if (project.ext.has('ENABLED_IMPORT_DEPENDENCIES') ? project.ext.ENABLED_IMPORT_DEPENDENCIES : false) {
                project.tasks.importDependencies.execute()
            }
            if (project.ext.has('ENABLED_IMPORT_DEPENDENCIES_BY_GROUP') ? project.ext.ENABLED_IMPORT_DEPENDENCIES_BY_GROUP : false){
                project.tasks.importDependenciesByGroup.execute()
            }
        }
    }
}
