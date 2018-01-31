package com.github.franciscozuccala.legacy.gradle.plugin

import com.github.franciscozuccala.legacy.common.root.InhibitorRootPlugin
import com.github.franciscozuccala.legacy.gradle.plugin.tasks.UploadJarTask
import org.gradle.api.Project

class InhibitorGradlePlugin extends InhibitorRootPlugin{

    @Override
    void apply(Project project) {
        project.task('uploadJar', type: UploadJarTask).dependsOn "build"
        super.apply(project)
    }
}
