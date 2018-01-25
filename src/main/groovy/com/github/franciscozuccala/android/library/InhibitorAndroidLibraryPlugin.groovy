package com.github.franciscozuccala.android.library

import com.github.franciscozuccala.common.root.InhibitorRootPlugin
import com.github.franciscozuccala.android.library.tasks.UploadAarTask
import com.github.franciscozuccala.android.library.tasks.UploadDependenciesByGroupTask
import org.gradle.api.Project

class InhibitorAndroidLibraryPlugin extends InhibitorRootPlugin {

    @Override
    void apply(Project project) {
        project.task('uploadAar', type: UploadAarTask).dependsOn "build"
        project.task('uploadDependenciesByGroup', type: UploadDependenciesByGroupTask).dependsOn "build"
        super.apply(project)
    }
}
