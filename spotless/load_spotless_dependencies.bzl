load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_jvm_external//:specs.bzl", "maven")

def load_spotless_dependencies():
    maven_install(
        name = "rules_spotless_dependencies",
        artifacts = [
            maven.artifact("com.diffplug.spotless", "spotless-lib", "2.34.0"),
            maven.artifact("com.diffplug.spotless", "spotless-lib-extra", "2.34.0"),
            maven.artifact("org.slf4j", "slf4j-simple", "2.0.0"),
            "com.google.googlejavaformat:google-java-format:1.15.0",
        ],
        repositories = [
            "https://repo1.maven.org/maven2",
            "https://repo.maven.apache.org/maven2/",
        ],
    )
