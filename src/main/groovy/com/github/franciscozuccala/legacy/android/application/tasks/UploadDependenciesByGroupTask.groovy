package com.github.franciscozuccala.legacy.android.application.tasks

import com.github.franciscozuccala.legacy.common.tasks.AbstractUploadDependenciesByGroupTask

class UploadDependenciesByGroupTask extends AbstractUploadDependenciesByGroupTask {

    @Override
    List<String> listVariants() {
        def variants = ['compile']

        project.android.applicationVariants.each { variant ->
            variants << "${variant.name}Compile"
        }
        return variants
    }

}
