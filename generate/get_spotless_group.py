from bazelrio_gentool.generate_styleguide_rule import StyleguideGroup


def get_spotless_group():
    version = "2.40.0"  # corresponds to gradle/6.20.0
    patch = ".bcr1"
    group = StyleguideGroup(
        short_name="spotless",
        is_java=True,
        has_protobuf=True,
        repo_name="rules_spotless",
        version=version,
        year=1,
        maven_url="",
        patch=patch,
    )

    group.create_java_dependency(
        name="Placeholder",
        group_id="Placeholder",
        parent_folder="Placeholder",
        maven_deps=[
            ("com.diffplug.spotless:spotless-lib", version),
            ("com.diffplug.spotless:spotless-lib-extra", version),
            ("org.slf4j:slf4j-simple", "2.0.3"),
            ("com.google.googlejavaformat:google-java-format", "1.17.0"),
            ("com.google.protobuf:protobuf-java", "3.21.7"),
            ("org.eclipse.jdt:org.eclipse.jdt.core", "3.32.0"),
            ("org.eclipse.platform:org.eclipse.equinox.common", "3.17.0"),
            ("com.google.code.findbugs:findbugs-annotations", "3.0.1"),
        ],
    )

    group.executable_tools = True

    return group
