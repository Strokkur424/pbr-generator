plugins {
  id("java-library")
  id("maven-publish")
}

repositories {
  mavenCentral()
}

dependencies {
  api(libs.jspecify)
  api(libs.zero.allocation.hashing)
}

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
  withSourcesJar()
  withJavadocJar()
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
