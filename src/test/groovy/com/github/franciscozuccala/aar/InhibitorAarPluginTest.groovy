package com.github.franciscozuccala.aar

import com.github.franciscozuccala.aar.tasks.UploadDependenciesByGroupTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.*

import static org.gradle.internal.impldep.org.junit.Assert.assertTrue

class InhibitorAarPluginTest {

    @Test public void inhibitorAarPluginAddsUploadDependenciesByGroupTaskToProject(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'inhibitor.aar'

        assertTrue(project.tasks.uploadDependenciesByGroup instanceof UploadDependenciesByGroupTask)
    }
}
