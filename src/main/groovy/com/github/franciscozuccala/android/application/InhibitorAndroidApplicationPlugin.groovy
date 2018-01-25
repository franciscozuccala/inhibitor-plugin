package com.github.franciscozuccala.android.application

import com.github.franciscozuccala.android.application.tasks.UploadDependenciesByGroupTask
import com.github.franciscozuccala.common.root.InhibitorRootPlugin
import org.gradle.api.Project

class InhibitorAndroidApplicationPlugin extends InhibitorRootPlugin {

    @Override
    void apply(Project project) {
        project.task('uploadDependenciesByGroup', type: UploadDependenciesByGroupTask).dependsOn "build"
        super.apply(project)
    }
}
