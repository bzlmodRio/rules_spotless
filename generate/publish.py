import os
from bazelrio_gentool.publish_module import publish_module
from get_spotless_group import get_spotless_group
from bazelrio_gentool.utils import TEMPLATE_BASE_DIR


def main():
    SCRIPT_DIR = os.environ["BUILD_WORKSPACE_DIRECTORY"]
    registry_location = os.path.join(
        SCRIPT_DIR, "..", "..", "..", "bazel-central-registry"
    )

    group = get_spotless_group()

    module_template = os.path.join(
        TEMPLATE_BASE_DIR, "styleguide", "MODULE.bazel.jinja2"
    )
    module_json_template = None

    os.chdir(SCRIPT_DIR)
    publish_module(
        registry_location,
        group,
        module_json_template=module_json_template,
        module_template=module_template,
    )


if __name__ == "__main__":
    """
    bazel run @bzlmodrio-allwpilib//generate_allwpilib:publish_allwpilib --enable_bzlmod
    """
    main()
