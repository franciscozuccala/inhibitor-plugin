package com.github.franciscozuccala.android.library.tasks

import com.github.franciscozuccala.common.tasks.AbstractUploadDependenciesByGroupTask

class UploadDependenciesByGroupTask extends AbstractUploadDependenciesByGroupTask {

    @Override
    List<String> listVariants() {
        def variants = ['compile']

        project.android.libraryVariants.each { variant ->
            variants << "${variant.name}Compile"
        }
        return variants
    }

}
