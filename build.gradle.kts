plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("net.kyori.indra") version "2.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0"
    java
}

group = "fi.fabianadrian"
version = "1.1.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    implementation("org.spongepowered:configurate-yaml:4.1.1")
}

indra {
    javaVersions().target(16)
}

tasks {
    shadowJar {
        minimize()
        sequenceOf(
            "org.spongepowered.configurate"
        ).forEach { pkg ->
            relocate(pkg, "${group}.${rootProject.name.toLowerCase()}.lib.$pkg")
        }
    }
    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    main = "fi.fabianadrian.speedlimiter.SpeedLimiter"
    name = rootProject.name
    apiVersion = "1.17"
    authors = listOf("FabianAdrian")
    commands {
        register("speedlimiter") {
            permission = "speedlimiter.admin"
            aliases = listOf("slim")
        }
    }
    permissions {
        register("speedlimiter.bypass") {
            description = "Bypass speedlimits."
        }
        register("speedlimiter.admin") {
            description = "Access /speedlimiter command."
        }
    }
}