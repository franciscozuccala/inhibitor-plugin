package com.github.franciscozuccala.library


import com.github.franciscozuccala.common.tasks.ImportDependenciesByGroupTask
import com.github.franciscozuccala.common.tasks.ImportDependenciesTask
import com.github.franciscozuccala.library.tasks.UploadAarTask
import com.github.franciscozuccala.library.tasks.UploadDependenciesByGroupTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class InhibitorLibraryPlugin implements Plugin<Project> {

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
