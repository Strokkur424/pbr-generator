plugins {
  id("java-library")
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.junit.jupiter)
  testRuntimeOnly(libs.junit.platform.launcher)
}

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks.test {
  useJUnitPlatform()
}
