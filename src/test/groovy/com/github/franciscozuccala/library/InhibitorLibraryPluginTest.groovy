package com.github.franciscozuccala.library

import com.github.franciscozuccala.library.tasks.UploadDependenciesByGroupTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.*

import static org.gradle.internal.impldep.org.junit.Assert.assertTrue

class InhibitorLibraryPluginTest {

    @Test public void inhibitorLibraryPluginAddsUploadDependenciesByGroupTaskToProject(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'inhibitor.library'

        assertTrue(project.tasks.uploadDependenciesByGroup instanceof UploadDependenciesByGroupTask)
    }
}
