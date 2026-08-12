import org.gradle.api.attributes.Attribute
import org.gradle.api.tasks.compile.JavaCompile
import com.hypherionmc.modpublisher.properties.CurseEnvironment
import com.hypherionmc.modpublisher.properties.ModLoader
import com.hypherionmc.modpublisher.properties.ReleaseType

// This is a minimal setup of a Forge mod workspace.
// For complex examples (including AccessTransformers, Jar-in-Jar, and Mixin), see our examples repository.
// https://github.com/MinecraftForge/MDKExamples

plugins {
    id("multiloader-loader")
    id("java")
    id("idea")
    id("eclipse")
    id("net.minecraftforge.gradle") version "[7.0.17,8)"
    id("com.hypherionmc.modutils.modpublisher") version "2.2.1"
}

val forge_version: String by project
val mod_id: String by project
val minecraft_version: String by project

val java_version: String by project

val mixinConfigs = listOf(
    "${mod_id}.mixins.json",
    "${mod_id}-forge.mixins.json"
)


java.toolchain.languageVersion.set(JavaLanguageVersion.of(java_version.toInt()))

// Include generated resources
sourceSets.named("main") {
    resources.srcDir(project(":common").file("src/generated/resources/").absolutePath)
}

sourceSets.forEach { it
    val dir = layout.buildDirectory.dir("sourcesSets/$it.name")
    it.output.setResourcesDir(dir.get().asFile)
    it.java.destinationDirectory = dir
}

minecraft {
    mixinConfigs.forEach {}

    runs {
        configureEach {
            workingDir = layout.projectDirectory.dir("run")

            systemProperty("eventbus.api.strictRuntimeChecks", "true")
            systemProperty("forge.enabledGameTestNamespaces", mod_id)
            mixinConfigs.forEach { config ->
                args("--mixin.config=$config")
            }
        }

        register("client")

        register("server") {
            args("--nogui")
        }

        register("gameTestServer")

        register("data") {
            workingDir = layout.projectDirectory.dir("run-data")

            args(
                "--mod", mod_id, "--all",
                "--output", layout.projectDirectory.dir("src/generated/resources"),
                "--existing", layout.projectDirectory.dir("src/main/resources")
            )
        }
    }

    val at = project(":common").file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at)
    }
}

repositories {
    minecraft.mavenizer(this)
    maven(fg.forgeMaven)
    maven(fg.minecraftLibsMaven)
    mavenCentral()
}

dependencies {
    implementation(minecraft.dependency("net.minecraftforge:forge:${minecraft_version}-${forge_version}"))
    annotationProcessor("net.minecraftforge:eventbus-validator:7.0.1")
}

tasks.withType<JavaCompile>().configureEach {
    // Use the UTF-8 charset for Java compilation
    // This is done by default in Java 18+, but this ensures it no matter the Java or Gradle version
    options.encoding = "UTF-8"
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["MixinConfigs"] = "${mod_id}.mixins.json,${mod_id}-forge.mixins.json"
    }
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
listOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements").forEach { variant ->
    configurations.findByName(variant)?.attributes {
        attribute(loaderAttribute, "forge")
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "forge")
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
    githubRepo.set("https://github.com/CorgiTaco/Oh-The-Trees-Youll-Grow")
    setReleaseType(ReleaseType.RELEASE)
    projectVersion.set("$minecraft_version-${project.version}-Forge")
    displayName.set("${project.properties["mod_name"]}-Forge-$minecraft_version-${project.version}")
    changelog.set(projectDir.toPath().parent.resolve("CHANGELOG.md").toFile().readText())
    artifact.set(tasks.jar)
    setGameVersions(minecraft_version)
    setLoaders(ModLoader.FORGE)
    setCurseEnvironment(CurseEnvironment.SERVER)
    setJavaVersions(JavaVersion.VERSION_25)
}

private fun getPublishingCredentials(): Pair<String?, String?> {
    val curseForgeToken = (project.findProperty("curseforge_key") ?: System.getenv("CURSEFORGE_KEY") ?: "") as String?
    val modrinthToken = (project.findProperty("modrinth_key") ?: System.getenv("MODRINTH_KEY") ?: "") as String?
    return Pair(curseForgeToken, modrinthToken)
}