package com.github.franciscozuccala.gradle.plugin

import com.github.franciscozuccala.common.root.InhibitorRootPlugin
import com.github.franciscozuccala.gradle.plugin.tasks.UploadJarTask
import org.gradle.api.Project

class InhibitorGradlePlugin extends InhibitorRootPlugin{

    @Override
    void apply(Project project) {
        project.task('uploadJar', type: UploadJarTask).dependsOn "build"
        super.apply(project)
    }
}
