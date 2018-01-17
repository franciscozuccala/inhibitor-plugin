package com.github.franciscozuccala.application

import com.github.franciscozuccala.application.tasks.UploadDependenciesByGroupTask
import com.github.franciscozuccala.common.tasks.ImportDependenciesByGroupTask
import com.github.franciscozuccala.common.tasks.ImportDependenciesTask
import com.github.franciscozuccala.common.tasks.UploadAarTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class InhibitorApplicationPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('uploadAar', type: UploadAarTask).dependsOn "build"
        project.task('uploadDependenciesByGroup', type: UploadDependenciesByGroupTask).dependsOn "build"
        project.task('importDependencies', type: ImportDependenciesTask)
        project.task('importDependenciesByGroup', type: ImportDependenciesByGroupTask)

        project.afterEvaluate {
            if (project.ext.has('ENABLED_IMPORT_DEPENDENCIES_FROM_GITHUB') ? project.ext.ENABLED_IMPORT_DEPENDENCIES_FROM_GITHUB : false) {
                project.tasks.importDependencies.execute()
            }
        }
    }
}
