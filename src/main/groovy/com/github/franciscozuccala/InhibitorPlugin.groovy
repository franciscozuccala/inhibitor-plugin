package com.github.franciscozuccala

import com.github.franciscozuccala.tasks.DownloadDependenciesFromGithubIntoLibsTask
import com.github.franciscozuccala.tasks.ImportDependenciesByGroupFromGithubTask
import com.github.franciscozuccala.tasks.ImportDependenciesFromGithubTask
import com.github.franciscozuccala.tasks.UploadAarToGithubTask
import com.github.franciscozuccala.tasks.UploadDependenciesToGithubTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class InhibitorPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('uploadAarToGithub', type: UploadAarToGithubTask).dependsOn "build"
        project.task('importDependenciesFromGithub', type: ImportDependenciesFromGithubTask)
        project.task('downloadDependenciesIntoLibs', type: DownloadDependenciesFromGithubIntoLibsTask).dependsOn "build"
        project.task('uploadDependenciesToGithub', type: UploadDependenciesToGithubTask).dependsOn "build"
        project.task('importDependenciesByGroupFromGithub', type: ImportDependenciesByGroupFromGithubTask)

        project.afterEvaluate {
            if (project.ext.has('ENABLED_IMPORT_DEPENDENCIES_FROM_GITHUB')?project.ext.ENABLED_IMPORT_DEPENDENCIES_FROM_GITHUB : false){
                project.tasks.importDependenciesFromGithub.execute()
            }
        }
    }
}
