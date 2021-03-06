package com.github.franciscozuccala.inhibitor

import com.github.franciscozuccala.inhibitor.tasks.ConfigureLocalMavenRepositoryTask
import com.github.franciscozuccala.inhibitor.tasks.ConfigureNexusTask
import com.github.franciscozuccala.inhibitor.tasks.SaveNexusTask
import com.github.franciscozuccala.inhibitor.tasks.StartNexusTask
import com.github.franciscozuccala.inhibitor.tasks.StopNexusTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskValidationException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.*

import static org.gradle.internal.impldep.org.junit.Assert.assertTrue

class InhibitorPluginTest {

    @Test void inhibitorPluginAddsAllTasksToProject(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'inhibitor'

        assertTrue(project.tasks.configureLocalMavenRepository instanceof ConfigureLocalMavenRepositoryTask)
        assertTrue(project.tasks.configureNexus instanceof ConfigureNexusTask)
        assertTrue(project.tasks.saveNexus instanceof SaveNexusTask)
        assertTrue(project.tasks.startNexus instanceof StartNexusTask)
        assertTrue(project.tasks.stopNexus instanceof StopNexusTask)

    }

    @Test(expected = TaskValidationException.class) void inhibitorPluginRunsConfigureLocalMavenRepositoryThrowsNexusRepositoryNotDefined(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'inhibitor'

        project.tasks.configureLocalMavenRepository.execute()

    }

    @Test(expected = TaskValidationException.class) void inhibitorPluginRunsConfigureLocalMavenRepositoryConfiguredOk(){
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'inhibitor'

        project.tasks.configureLocalMavenRepository{
            it.nexusRepository=""
        }
    }
}
