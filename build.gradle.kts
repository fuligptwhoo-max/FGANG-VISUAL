plugins {
    id("java-library")
    id("eclipse")
    id("idea")
    id("maven-publish")
    id("net.neoforged.moddev") version("2.0.115")
}

tasks.named<Wrapper>("wrapper").configure {
    distributionType = Wrapper.DistributionType.BIN
}

version = "${property("mod_version")}"
group = "${property("mod_group_id")}"
base.archivesName = "${property("mod_id")}"

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = property("neo_version") as String

    validateAccessTransformers = true

    runs {
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }

        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", property("mod_id") as String)
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", property("mod_id") as String)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", property("mod_id") as String)
        }

        create("data") {
            data()
            programArguments.addAll("--mod", property("mod_id") as String, "--all", "--output", file("src/generated/resources/").absolutePath, "--existing", file("src/main/resources/").absolutePath)
        }
    }

    mods {
        create("fgangvisuals") {
            sourceSet(sourceSets["main"])
        }
    }
}

repositories {
    mavenCentral()
    maven("https://maven.neoforged.net/releases")
    maven("https://repo.spongepowered.org/maven")
}

dependencies {
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("io.github.llamalad7:mixinextras-common:0.5.0-beta.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0-beta.5")
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
    exclude(".cache")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:unchecked")
    options.compilerArgs.add("-Xlint:deprecation")
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mapOf(
        "minecraft_version" to (project.findProperty("minecraft_version") as? String ?: ""),
        "minecraft_version_range" to (project.findProperty("minecraft_version_range") as? String ?: ""),
        "neo_version" to (project.findProperty("neo_version") as? String ?: ""),
        "neo_version_range" to (project.findProperty("neo_version_range") as? String ?: ""),
        "loader_version_range" to (project.findProperty("loader_version_range") as? String ?: ""),
        "mod_id" to (project.findProperty("mod_id") as? String ?: ""),
        "mod_name" to (project.findProperty("mod_name") as? String ?: ""),
        "mod_license" to (project.findProperty("mod_license") as? String ?: ""),
        "mod_version" to (project.findProperty("mod_version") as? String ?: ""),
        "mod_group_id" to (project.findProperty("mod_group_id") as? String ?: ""),
        "mod_authors" to (project.findProperty("mod_authors") as? String ?: ""),
        "mod_description" to (project.findProperty("mod_description") as? String ?: "")
    )

    inputs.properties(replaceProperties)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file:///${project.projectDir}/mcmodsrepo")
        }
    }
}
