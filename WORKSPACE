workspace(name = "rules_spotless")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# WPI Formatter
http_archive(
    name = "rules_wpiformat",
    sha256 = "0d77c34bf0283cebff00c6fb89c57318150944e4a5ec927df024cf5da7baf75a",
    url = "https://github.com/bzlmodRio/rules_wpiformat/releases/download/2024.45/rules_wpiformat-2024.45.tar.gz",
)

load("@rules_wpiformat//dependencies:load_rule_dependencies.bzl", "load_wpiformat_rule_dependencies")

load_wpiformat_rule_dependencies()

load("@rules_wpiformat//dependencies:load_transitive_dependencies.bzl", "load_wpiformat_transitive_dependencies")

load_wpiformat_transitive_dependencies()

load("@rules_wpiformat//dependencies:load_dependencies.bzl", "load_wpiformat_dependencies")

load_wpiformat_dependencies()

load("@rules_wpiformat_pip//:requirements.bzl", "install_deps")

install_deps()

# Rule Dependencies

http_archive(
    name = "rules_cc",
    sha256 = "712d77868b3152dd618c4d64faaddefcc5965f90f5de6e6dd1d5ddcd0be82d42",
    strip_prefix = "rules_cc-0.1.1",
    url = "https://github.com/bazelbuild/rules_cc/releases/download/0.1.1/rules_cc-0.1.1.tar.gz",
)

http_archive(
    name = "com_google_protobuf",
    # integrity = "sha256-PTKUDpdcStm4umlkDnj1UnB1uuM8ookCdb8muFPAliw=",
    strip_prefix = "protobuf-27.0",
    urls = [
        "https://github.com/protocolbuffers/protobuf/archive/v27.0.tar.gz",
    ],
)

http_archive(
    name = "rules_jvm_external",
    sha256 = "c18a69d784bcd851be95897ca0eca0b57dc86bb02e62402f15736df44160eb02",
    strip_prefix = "rules_jvm_external-6.3",
    url = "https://github.com/bazelbuild/rules_jvm_external/releases/download/6.3/rules_jvm_external-6.3.tar.gz",
)

http_archive(
    name = "rules_java",
    sha256 = "d31b6c69e479ffa45460b64dc9c7792a431cac721ef8d5219fc9f603fa2ff877",
    url = "https://github.com/bazelbuild/rules_java/releases/download/8.11.0/rules_java-8.11.0.tar.gz",
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

load("@rules_java//java:rules_java_deps.bzl", "rules_java_dependencies")

rules_java_dependencies()

load("@rules_java//java:repositories.bzl", "rules_java_toolchains")

rules_java_toolchains()

load("@rules_python//python:repositories.bzl", "py_repositories")

py_repositories()

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")

rules_jvm_external_setup()

# Transitive Dependencies
load("//dependencies:load_dependencies.bzl", "load_spotless_dependencies")

load_spotless_dependencies()

# Pin the maven install
load("@rules_spotless_dependencies//:defs.bzl", "pinned_maven_install")

pinned_maven_install()
