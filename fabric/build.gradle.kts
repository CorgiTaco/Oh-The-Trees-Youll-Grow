import com.hypherionmc.modpublisher.properties.CurseEnvironment
import com.hypherionmc.modpublisher.properties.ModLoader
import com.hypherionmc.modpublisher.properties.ReleaseType

plugins {
    id("com.gradleup.shadow")
    id("com.hypherionmc.modutils.modpublisher") version "2.2.1"
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val minecraftVersion = providers.gradleProperty("minecraft_version").get()

configurations {
    create("common")
    "common" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    create("shadowBundle")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentFabric").extendsFrom(configurations["common"])
    "shadowBundle" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

loom.accessWidenerPath.set(project(":common").loom.accessWidenerPath)

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${providers.gradleProperty("fabric_loader_version").get()}")
    modApi("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}+$minecraftVersion")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowBundle"(project(":common", "transformProductionFabric"))
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }

    shadowJar {
        exclude("architectury.common.json")
        configurations = listOf(project.configurations.getByName("shadowBundle"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }
}

publisher {
    apiKeys {
        curseforge(getPublishingCredentials().first)
        modrinth(getPublishingCredentials().second)
        github(providers.gradleProperty("github_token").orNull)
    }

    curseID.set(providers.gradleProperty("curseforge_id").get())
    modrinthID.set(providers.gradleProperty("modrinth_id").get())
    githubRepo.set("https://github.com/CorgiTaco/Oh-The-Trees-Youll-Grow")
    setReleaseType(ReleaseType.RELEASE)
    projectVersion.set("$minecraftVersion-${project.version}-Fabric")
    displayName.set("${providers.gradleProperty("mod_name").get()}-Fabric-$minecraftVersion-${project.version}")
    changelog.set(projectDir.toPath().parent.resolve("CHANGELOG.md").toFile().readText())
    artifact.set(tasks.remapJar)
    setGameVersions(minecraftVersion)
    setLoaders(ModLoader.FABRIC, ModLoader.QUILT)
    setCurseEnvironment(CurseEnvironment.SERVER)
    setJavaVersions(JavaVersion.VERSION_21, JavaVersion.VERSION_22, JavaVersion.VERSION_25)
    val depends = mutableListOf("fabric-api")
    curseDepends.required.set(depends)
    modrinthDepends.required.set(depends)
}

private fun getPublishingCredentials(): Pair<String?, String?> {
    val curseForgeToken = (project.findProperty("curseforge_key") ?: System.getenv("CURSEFORGE_KEY") ?: "") as String?
    val modrinthToken = (project.findProperty("modrinth_key") ?: System.getenv("MODRINTH_KEY") ?: "") as String?
    return Pair(curseForgeToken, modrinthToken)
}