
load("@rules_jvm_external//:defs.bzl", "artifact")

def _common_setup(ctx):
    inputs = []
    outputs = []

    arguments = ctx.actions.args()

    # Sources
    # print(ctx.files.srcs)
    inputs.extend(ctx.files.srcs)
    #inputs.extend(ctx.files.config_file)

    #property_file = ctx.actions.declare_file("{}_spotless_properties.properties".format(ctx.label.name))
    #ctx.actions.write(property_file, "config_loc={}".format(ctx.files.config_file[0].dirname), is_executable = False)
    #inputs.append(property_file)

    # Report
    report_file = ctx.actions.declare_file("spotless_report/{name}_spotless_report.{extension}".format(
        name = ctx.label.name,
        extension = "html",
    ))
    outputs.append(report_file)

    arguments = ctx.actions.args()

    arguments.add(report_file)
    arguments.add(ctx.attr.package_name)
    arguments.add_all(ctx.files.srcs)

    return inputs, outputs, arguments

def _spotless_check_impl(ctx):
    inputs, outputs, arguments = _common_setup(ctx)

    # print(ctx.label)
    #print(ctx.path(ctx.attr.file_in_project).dirname)

    # Run
    ctx.actions.run(
        mnemonic = "SpotlessCheck",
        executable = ctx.executable._executable,
        inputs = inputs,
        outputs = outputs,
        arguments = [arguments],
    )

    return [DefaultInfo(files = depset(outputs))]


spotless_check = rule(
    implementation = _spotless_check_impl,
    attrs = {
        "_executable": attr.label(
            default = "//spotless/wrapper:RunCheck",
            #default = "@rules_spotless_dependencies//:com_puppycrawl_tools_spotless",
            executable = True,
            cfg = "exec",
        ),
        "srcs": attr.label_list(
            allow_files = True,
            doc = "Source code files.",
            mandatory = True,
            allow_empty = False,
        ),
        "package_name": attr.string(mandatory=True)
    },
    provides = [DefaultInfo],
)

def spotless_apply(name, srcs, package_name, **kwargs):
    native.java_binary(
        name = name,
        main_class = "rules_spotless.RunApply",
        runtime_deps = ["@rules_spotless//spotless/wrapper:RunApply"],
        data = srcs,
        args = ['x', 'y'] + [package_name + "/" + x for x in srcs],
    jvm_flags = [
        "--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
        "--add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
    ],
        **kwargs,
    )


def spotless(name, srcs, tags = [], **kwargs):
    package_name = native.package_name()
    # print(native.workspace_name)
    spotless_check(name = name + ".check", srcs=srcs, package_name = package_name, tags=['_spotless'], **kwargs)
    spotless_apply(name = name + ".apply", srcs=srcs, package_name = package_name, tags=['_spotless_apply'], **kwargs)