import net.kyori.indra.repository.sonatypeSnapshots

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("net.kyori.indra") version "2.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0"
    java
}

group = "fi.fabianadrian"
version = "1.2.0"
description = "Reduce chunk loading overhead by limiting how frequently players can use fireworks or riptide trident to boost their elytra speed."

repositories {
    mavenCentral()
    sonatypeSnapshots()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
}

indra {
    javaVersions().target(17)
}

tasks {
    shadowJar {
        minimize()
        sequenceOf(
            "net.kyori.adventure.text.minimessage",
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
    apiVersion = "1.18"
    authors = listOf("FabianAdrian")
    website = "https://github.com/fabianmakila/SpeedLimiter"
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