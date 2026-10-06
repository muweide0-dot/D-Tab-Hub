plugins {
    id("fabric-loom") version "1.13.1"
}

version = "${project.property("mod_version")}+mc${project.property("minecraft_version")}"
group = project.property("maven_group") as String

repositories {
    maven { url = uri("https://maven.fabricmc.net/") }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
    modImplementation("io.github.llamalad7:mixinextras-fabric:0.5.4")
}

base {
    archivesName = project.property("archives_base_name") as String
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
