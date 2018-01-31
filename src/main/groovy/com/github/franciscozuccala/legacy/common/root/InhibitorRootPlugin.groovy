package com.github.franciscozuccala.legacy.common.root

import com.github.franciscozuccala.legacy.common.tasks.ImportDependenciesByGroupTask
import com.github.franciscozuccala.legacy.common.tasks.ImportDependenciesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class InhibitorRootPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
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
