import org.gradle.api.attributes.Attribute
import com.hypherionmc.modpublisher.properties.CurseEnvironment
import com.hypherionmc.modpublisher.properties.ModLoader
import com.hypherionmc.modpublisher.properties.ReleaseType


plugins {
    id("multiloader-loader")
    id("net.neoforged.moddev")
    id("com.hypherionmc.modutils.modpublisher") version "2.2.1"
}

val neoforge_version: String by project
val mod_id: String by project

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)

neoForge {
    version = neoforge_version
    // Automatically enable neoforge AccessTransformers if the file exists
    val at = project(":common").file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        create("neo_client") {
            client()
        }
        create("neo_server") {
            server()
        }
        create("neo_server_data") {
            serverData()
            programArguments.addAll(
                "--mod",
                mod_id,
                "--all",
                "--output",
                project.project(":common").file("src/generated/resources/").absolutePath,
                "--existing",
                file("src/main/resources/").absolutePath
            )
        }
    }
}

sourceSets.named("main") {
    resources.srcDir(project(":common").file("src/generated/resources/").absolutePath)
}

// Implement mcgradleconventions loader attribute
listOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements").forEach { variant ->
    configurations.named(variant) {
        attributes {
            attribute(loaderAttribute, "neoforge")
        }
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName, getTaskName(null, "jarJar")).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "neoforge")
            }
        }
    }
}

publisher {
    apiKeys {
        curseforge(getPublishingCredentials().first)
        modrinth(getPublishingCredentials().second)
        github(project.properties["github_token"].toString())
    }

    curseID.set(project.properties["curseforge_id"].toString())
    modrinthID.set(project.properties["modrinth_id"].toString())
    githubRepo.set("https://github.com/CorgiTaco/Data-Anchor/")
    setReleaseType(ReleaseType.RELEASE)
    projectVersion.set("${project.version}-neoforge")
    displayName.set(project.base.archivesName)
    changelog.set(projectDir.toPath().parent.resolve("CHANGELOG.md").toFile().readText())
    artifact.set(tasks.jar.get().archiveFile.get().asFile)
    setGameVersions("${project.properties["minecraft_version"]}")
    setLoaders(ModLoader.NEOFORGE)
    setCurseEnvironment(CurseEnvironment.BOTH)
    setJavaVersions(JavaVersion.VERSION_25)
}

private fun getPublishingCredentials(): Pair<String?, String?> {
    val curseForgeToken = (project.findProperty("curseforge_key") ?: System.getenv("CURSEFORGE_KEY") ?: "") as String?
    val modrinthToken = (project.findProperty("modrinth_key") ?: System.getenv("MODRINTH_KEY") ?: "") as String?
    return Pair(curseForgeToken, modrinthToken)
}