import com.hypherionmc.modpublisher.properties.CurseEnvironment
import com.hypherionmc.modpublisher.properties.ModLoader
import com.hypherionmc.modpublisher.properties.ReleaseType

plugins {
    id("com.gradleup.shadow")
    id("com.hypherionmc.modutils.modpublisher") version "2.2.1"
}

architectury {
    platformSetupLoomIde()
    forge()
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
    getByName("developmentForge").extendsFrom(configurations["common"])
    "shadowBundle" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)

    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)

        useCustomMixin.set(false)
        mixinConfig("ohthetreesyoullgrow.mixins.json")
    }
}

dependencies {
    forge("net.minecraftforge:forge:$minecraftVersion-${providers.gradleProperty("forge_version").get()}")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowBundle"(project(":common", "transformProductionForge"))
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand(mapOf("version" to project.version))
        }
    }

    shadowJar {
        exclude("architectury.common.json", ".cache/**")
        configurations = listOf(project.configurations.getByName("shadowBundle"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
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
    projectVersion.set("$minecraftVersion-${project.version}-Forge")
    displayName.set("${providers.gradleProperty("mod_name").get()}-Forge-$minecraftVersion-${project.version}")
    changelog.set(projectDir.toPath().parent.resolve("CHANGELOG.md").toFile().readText())
    artifact.set(tasks.remapJar)
    setGameVersions(minecraftVersion, "1.21.9", "1.21.8", "1.21.7", "1.21.6")
    setLoaders(ModLoader.FORGE)
    setCurseEnvironment(CurseEnvironment.SERVER)
    setJavaVersions(JavaVersion.VERSION_21, JavaVersion.VERSION_22, JavaVersion.VERSION_25)
}

private fun getPublishingCredentials(): Pair<String?, String?> {
    val curseForgeToken = (project.findProperty("curseforge_key") ?: System.getenv("CURSEFORGE_KEY") ?: "") as String?
    val modrinthToken = (project.findProperty("modrinth_key") ?: System.getenv("MODRINTH_KEY") ?: "") as String?
    return Pair(curseForgeToken, modrinthToken)
}