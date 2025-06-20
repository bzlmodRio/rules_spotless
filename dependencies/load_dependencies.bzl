load("@rules_jvm_external//:defs.bzl", "maven_install")

def load_spotless_dependencies():
    maven_install(
        name = "rules_spotless_dependencies",
        artifacts = [
            "com.diffplug.spotless:spotless-lib:2.40.0",
            "com.diffplug.spotless:spotless-lib-extra:2.40.0",
            "com.google.code.findbugs:findbugs-annotations:3.0.1",
            "com.google.googlejavaformat:google-java-format:1.17.0",
            "com.google.protobuf:protobuf-java:3.21.7",
            "org.eclipse.jdt:org.eclipse.jdt.core:3.32.0",
            "org.eclipse.platform:org.eclipse.equinox.common:3.17.0",
            "org.slf4j:slf4j-simple:2.0.3",
        ],
        repositories = [
            "https://repo1.maven.org/maven2",
            "https://repo.maven.apache.org/maven2/",
        ],
        maven_install_json = "@rules_spotless//:rules_spotless_dependencies_install.json",
    )
