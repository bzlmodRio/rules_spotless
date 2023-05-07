load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")
load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")

def load_spotless_dependencies():
    rules_proto_dependencies()
    rules_proto_toolchains()

    rules_jvm_external_deps()
    maven_install(
        name = "rules_spotless_dependencies",
        artifacts = [
            "com.diffplug.spotless:spotless-lib:2.34.0",
            "com.diffplug.spotless:spotless-lib-extra:2.34.0",
            "com.google.googlejavaformat:google-java-format:1.15.0",
            "com.google.protobuf:protobuf-java:3.21.7",
            "org.eclipse.jdt:org.eclipse.jdt.core:3.27.0",
            "org.eclipse.platform:org.eclipse.equinox.common:3.15.0",
            "org.slf4j:slf4j-simple:2.0.0",
        ],
        repositories = [
            "https://repo1.maven.org/maven2",
            "https://repo.maven.apache.org/maven2/",
        ],
        maven_install_json = "@rules_spotless//:rules_spotless_dependencies_install.json",
    )
