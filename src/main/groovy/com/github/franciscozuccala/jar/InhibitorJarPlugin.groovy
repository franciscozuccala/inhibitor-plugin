package com.github.franciscozuccala.jar

import com.github.franciscozuccala.jar.tasks.UploadJarTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class InhibitorJarPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('uploadJar', type: UploadJarTask).dependsOn "build"
    }
}
