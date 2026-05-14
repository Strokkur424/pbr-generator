plugins {
  id("java-library")
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
