package com.github.franciscozuccala.aar

import com.github.franciscozuccala.legacy.android.library.tasks.UploadAarTask
import com.github.franciscozuccala.legacy.android.library.tasks.UploadDependenciesByGroupTask
import com.github.franciscozuccala.legacy.common.tasks.ImportDependenciesByGroupTask
import com.github.franciscozuccala.legacy.common.tasks.ImportDependenciesTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.*

import static org.gradle.internal.impldep.org.junit.Assert.assertTrue

class InhibitorAndroidLibraryPluginTest {

    @Test void inhibitorAarPluginAddsUploadDependenciesByGroupTaskToProject(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'inhibitor.android-library'

        assertTrue(project.tasks.uploadDependenciesByGroup instanceof UploadDependenciesByGroupTask)
        assertTrue(project.tasks.uploadAar instanceof UploadAarTask)
        assertTrue(project.tasks.importDependencies instanceof ImportDependenciesTask)
        assertTrue(project.tasks.importDependenciesByGroup instanceof ImportDependenciesByGroupTask)
    }
}
