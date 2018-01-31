package com.github.franciscozuccala.legacy.android.application

import com.github.franciscozuccala.legacy.android.application.tasks.UploadDependenciesByGroupTask
import com.github.franciscozuccala.legacy.common.root.InhibitorRootPlugin
import org.gradle.api.Project

class InhibitorAndroidApplicationPlugin extends InhibitorRootPlugin {

    @Override
    void apply(Project project) {
        project.task('uploadDependenciesByGroup', type: UploadDependenciesByGroupTask).dependsOn "build"
        super.apply(project)
    }
}
