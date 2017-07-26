package com.github.franciscozuccala.tasks

import org.gradle.api.Project
import org.gradle.internal.impldep.org.junit.Test
import org.gradle.testfixtures.ProjectBuilder

import static org.gradle.internal.impldep.org.junit.Assert.assertTrue

class InhibitorPluginTest {

    @Test
    public void inhibitorPluginAddsUploadDependenciesToGithubTaskToProject(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'com.github.franciscozuccala.inhibitor'

        assertTrue(project.tasks.uploadDependenciesToGithub instanceof UploadDependenciesToGithubTask)
    }
}
