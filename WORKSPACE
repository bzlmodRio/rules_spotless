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
load("//dependencies:load_rule_dependencies.bzl", "load_spotless_rule_dependencies")

load_spotless_rule_dependencies()

# Transitive Dependencies
load("//dependencies:load_dependencies.bzl", "load_spotless_dependencies")

load_spotless_dependencies()

# Pin the maven install
load("@rules_spotless_dependencies//:defs.bzl", "pinned_maven_install")

pinned_maven_install()
