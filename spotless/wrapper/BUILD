load("@rules_java//java:defs.bzl", "java_binary")
load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "wrapper-lib",
    srcs = [
            "src/main/java/rules_checkstyle/FormatExecutor.java",
            "src/main/java/rules_checkstyle/FormatExtension.java", 
            "src/main/java/rules_checkstyle/JavaExtension.java", 
            "src/main/java/rules_checkstyle/JavaFormatter.java", 
        ],
    deps = [
        artifact(
            "com.diffplug.spotless:spotless-lib",
            repository_name = "rules_spotless_dependencies",
        ),
        artifact(
            "com.diffplug.spotless:spotless-lib-extra",
            repository_name = "rules_spotless_dependencies",
        ),
        artifact(
            "com.diffplug.durian:durian-core",
            repository_name = "rules_spotless_dependencies",
        ),
    ],
    data = [
        artifact(
            "com.google.googlejavaformat:google-java-format",
            repository_name = "rules_spotless_dependencies",
        ),
    ],
    runtime_deps = [
        artifact(
            "org.slf4j:slf4j-simple",
            repository_name = "rules_spotless_dependencies",
        ),
        artifact(
            "com.google.googlejavaformat:google-java-format",
            repository_name = "rules_spotless_dependencies",
        ),
    ],
)

java_binary(
    name = "RunCheck",
    srcs = glob(["src/main/java/rules_checkstyle/RunCheck.java"]),
    main_class = "rules_spotless.RunCheck",
    tags = ["no-sandbox"],
    visibility = ["//visibility:public"],
    deps = [
        ":wrapper-lib",
        artifact(
            "com.diffplug.spotless:spotless-lib-extra",
            repository_name = "rules_spotless_dependencies",
        ),
    ],
    jvm_flags = [
        "--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
    ],
)

java_library(
    name = "RunApply",
    srcs = glob(["src/main/java/rules_checkstyle/RunApply.java"]),
    deps = [
        ":wrapper-lib",
    ],
    tags = ["no-sandbox"],
    visibility = ["//visibility:public"],
)
