workspace(name = "rules_spotless")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# WPI Formatter
http_archive(
    name = "rules_wpiformat",
    sha256 = "3e5edc1516a8e2edb0c89d7e9e5b90f66e7fb6ec1dccd8c9bb2c12589a746176",
    url = "https://github.com/bzlmodRio/rules_wpiformat/releases/download/2022.30/rules_wpiformat-2022.30.tar.gz",
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
