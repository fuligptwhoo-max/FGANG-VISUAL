plugins {
    id("java-library")
    id("eclipse")
    id("idea")
    id("maven-publish")
    id("net.neoforged.moddev") version("2.0.78")
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

configurations {
    runtimeClasspath {
        extendsFrom(configurations["localRuntime"])
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
    val replaceProperties = listOf(
        "minecraft_version",
        "minecraft_version_range",
        "neo_version",
        "neo_version_range",
        "loader_version_range",
        "mod_id",
        "mod_name",
        "mod_license",
        "mod_version",
        "mod_group_id",
        "mod_authors",
        "mod_description"
    )

    inputs.properties(
        replaceProperties.associateWith { property(it) as String }
    )

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(
            replaceProperties.associateWith { property(it) as String }
        )
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
