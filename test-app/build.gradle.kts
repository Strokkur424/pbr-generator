plugins {
  id("java")
  id("application")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(rootProject)
}

application {
  mainClass = "net.strokkur.pbr.test.TestApp"
}

java {
  toolchain.languageVersion = JavaLanguageVersion.of(25)
}
