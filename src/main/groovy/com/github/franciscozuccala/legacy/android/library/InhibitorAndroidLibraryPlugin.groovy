package com.github.franciscozuccala.legacy.android.library

import com.github.franciscozuccala.legacy.common.root.InhibitorRootPlugin
import com.github.franciscozuccala.legacy.android.library.tasks.UploadAarTask
import com.github.franciscozuccala.legacy.android.library.tasks.UploadDependenciesByGroupTask
import org.gradle.api.Project

class InhibitorAndroidLibraryPlugin extends InhibitorRootPlugin {

    @Override
    void apply(Project project) {
        project.task('uploadAar', type: UploadAarTask).dependsOn "build"
        project.task('uploadDependenciesByGroup', type: UploadDependenciesByGroupTask).dependsOn "build"
        super.apply(project)
    }
}
