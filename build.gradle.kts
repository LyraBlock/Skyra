import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	id("net.fabricmc.fabric-loom")
	id("maven-publish")
	id("org.jetbrains.kotlin.jvm") version "2.3.0"
}

version = project.property("mod_version")!!
group = project.property("maven_group")!!

base {
	archivesName.set(project.property("archives_base_name") as String)
}

repositories {
	// Add repositories to retrieve artifacts from in here.
	// You should only use this when depending on other mods because
	// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
	// See https://docs.gradle.org/current/userguide/declaring_repositories.html
	// for more information about repositories.
}

dependencies {
	minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
	
	implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

	implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
	implementation("net.fabricmc:fabric-language-kotlin:${project.property("fabric_kotlin_version")}")
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand(mapOf("version" to inputs.properties["version"]))
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release.set(25)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions {
		jvmTarget.set(JvmTarget.JVM_25)
	}
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	inputs.property("archivesName", project.base.archivesName)

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archivesName"]}"}
	}
}

// configure the maven publication
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifactId = project.property("archives_base_name") as String
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}