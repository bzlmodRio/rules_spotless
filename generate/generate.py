from bazelrio_gentool.generate_styleguide_rule import generate_styleguide_rule
from bazelrio_gentool.clean_existing_version import clean_existing_version
from get_spotless_group import get_spotless_group
from bazelrio_gentool.cli import add_generic_cli, GenericCliArgs
import os
import argparse
from bazelrio_gentool.generate_module_project_files import (
    create_default_mandatory_settings,
)


def main():
    SCRIPT_DIR = os.environ["BUILD_WORKSPACE_DIRECTORY"]
    REPO_DIR = os.path.join(SCRIPT_DIR, "..")

    parser = argparse.ArgumentParser()
    add_generic_cli(parser)
    args = parser.parse_args()

    clean_existing_version(
        REPO_DIR,
        extra_dir_blacklist=["spotless"],
        file_blacklist=["rules_spotless_dependencies_install.json"],
    )

    group = get_spotless_group()
    mandatory_dependencies = create_default_mandatory_settings(GenericCliArgs(args))
    generate_styleguide_rule(REPO_DIR, group, mandatory_dependencies)


if __name__ == "__main__":
    """
    bazel run @allwpilib//generate
    """
    main()
