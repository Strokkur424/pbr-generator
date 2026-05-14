plugins {
  id("java-library")
  id("maven-publish")
  id("com.gradleup.shadow") version "9.4.1"
}

repositories {
  mavenCentral()
}

dependencies {
  api(libs.jspecify)
  api(libs.zero.allocation.hashing)

  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.jupiter)
  testRuntimeOnly(libs.junit.platform.launcher)
}

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
  withSourcesJar()
  withJavadocJar()
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<Javadoc> {
  javadocTool.set(javaToolchains.javadocToolFor {
    this.languageVersion = JavaLanguageVersion.of(25)
  })
  options.encoding = Charsets.UTF_8.name()
}

publishing {
  repositories {
    maven {
      authentication {
        credentials(PasswordCredentials::class) {
          username = System.getenv("NEXUS_USERNAME")
          password = System.getenv("NEXUS_PASSWORD")
        }
      }

      name = "EldoNexus"
      if (version.toString().endsWith("-SNAPSHOT")) {
        setUrl("https://eldonexus.de/repository/maven-snapshots/")
      } else {
        setUrl("https://eldonexus.de/repository/maven-releases/")
      }
    }
  }

  publications.create<MavenPublication>("maven") {
    from(components["java"])
    withBuildIdentifier()
  }
}
