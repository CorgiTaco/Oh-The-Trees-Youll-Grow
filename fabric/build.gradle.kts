import org.gradle.api.attributes.Attribute
import com.hypherionmc.modpublisher.properties.CurseEnvironment
import com.hypherionmc.modpublisher.properties.ModLoader
import com.hypherionmc.modpublisher.properties.ReleaseType

plugins {
    id("multiloader-loader")
    id("net.fabricmc.fabric-loom")
    id("com.hypherionmc.modutils.modpublisher") version "2.2.1"
}

val minecraft_version: String by project
val fabric_loader_version: String by project
val fabric_version: String by project
val mod_id: String by project

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)

dependencies {
    add("minecraft", "com.mojang:minecraft:$minecraft_version")
    add("implementation", "net.fabricmc:fabric-loader:$fabric_loader_version")
    add("implementation", "net.fabricmc.fabric-api:fabric-api:$fabric_version")
}

loom {
    val aw = project(":common").file("src/main/resources/${mod_id}.accesswidener")
    if (aw.exists()) {
        accessWidenerPath.set(aw)
    }
}

tasks.named("publishMod") {
    dependsOn(tasks.named("build"))
}

// Implement mcgradleconventions loader attribute
listOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements", "includeInternal", "modCompileClasspath").forEach { variant ->
    configurations.named(variant) {
        attributes {
            attribute(loaderAttribute, "fabric")
        }
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "fabric")
            }
        }
    }
}

sourceSets.named("main") {
    resources.srcDir(project(":common").file("src/generated/resources/").absolutePath)
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
    projectVersion.set("${project.version}-fabric")
    displayName.set(project.base.archivesName)
    changelog.set(projectDir.toPath().parent.resolve("CHANGELOG.md").toFile().readText())
    artifact.set(tasks.jar.get().archiveFile.get().asFile)
    setGameVersions("${project.properties["minecraft_version"]}")
    setLoaders(ModLoader.FABRIC, ModLoader.QUILT)
    setCurseEnvironment(CurseEnvironment.SERVER)
    setJavaVersions(JavaVersion.VERSION_25)
    val depends = mutableListOf("fabric-api")
    curseDepends.required.set(depends)
    modrinthDepends.required.set(depends)
}

private fun getPublishingCredentials(): Pair<String?, String?> {
    val curseForgeToken = (project.findProperty("curseforge_key") ?: System.getenv("CURSEFORGE_KEY") ?: "") as String?
    val modrinthToken = (project.findProperty("modrinth_key") ?: System.getenv("MODRINTH_KEY") ?: "") as String?
    return Pair(curseForgeToken, modrinthToken)
}

