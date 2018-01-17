package com.github.franciscozuccala.common.tasks

import com.github.franciscozuccala.library.tasks.UploadDependenciesByGroupTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.*

import static org.gradle.internal.impldep.org.junit.Assert.assertTrue

class InhibitorLibraryPluginTest {

    @Test public void inhibitorApplicationPluginAddsUploadDependenciesByGroupTaskToProject(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'com.github.franciscozuccala.inhibitor'

        assertTrue(project.tasks.uploadDependenciesByGroup instanceof UploadDependenciesByGroupTask)
    }
}
