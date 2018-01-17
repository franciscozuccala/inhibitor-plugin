package com.github.franciscozuccala.application.tasks

import com.github.franciscozuccala.common.tasks.AbstractUploadDependenciesByGroupTask

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
