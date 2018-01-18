package com.github.franciscozuccala.aar


import com.github.franciscozuccala.common.tasks.ImportDependenciesByGroupTask
import com.github.franciscozuccala.common.tasks.ImportDependenciesTask
import com.github.franciscozuccala.aar.tasks.UploadAarTask
import com.github.franciscozuccala.aar.tasks.UploadDependenciesByGroupTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class InhibitorAarPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('uploadAar', type: UploadAarTask).dependsOn "build"
        project.task('uploadDependenciesByGroup', type: UploadDependenciesByGroupTask).dependsOn "build"
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
