def _spotless_check_impl(ctx):
    inputs = []
    outputs = []

    arguments = ctx.actions.args()

    # Sources
    inputs.extend(ctx.files.config_file)
    inputs.extend(ctx.files.srcs)

    # Report
    report_file = ctx.actions.declare_file("spotless_report/{name}_spotless_report.{extension}".format(
        name = ctx.label.name,
        extension = "txt",
    ))
    outputs.append(report_file)

    arguments = ctx.actions.args()

    arguments.add(ctx.files.config_file[0])
    arguments.add(report_file)
    arguments.add(ctx.attr.package_name)
    arguments.add(ctx.attr.target_name)
    arguments.add_all(ctx.files.srcs)

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
            # default = "//spotless/wrapper:RunCheck",
            default = "//spotless/wrapper/src/main/java/com/diffplug/bazel/spotless:SpotlessCheck",
            executable = True,
            cfg = "exec",
        ),
        "srcs": attr.label_list(
            allow_files = True,
            mandatory = True,
            allow_empty = False,
        ),
        "config_file": attr.label(
            # allow_files = True,
            mandatory = True,
            allow_single_file = True,
        ),
        "target_name": attr.string(mandatory = True),
        "package_name": attr.string(mandatory = True),
    },
    provides = [DefaultInfo],
)

def spotless_apply(name, srcs, config_file, package_name, **kwargs):
    native.java_binary(
        name = name,
        main_class = "com.diffplug.bazel.spotless.SpotlessApply",
        runtime_deps = ["@rules_spotless//spotless/wrapper/src/main/java/com/diffplug/bazel/spotless:SpotlessApply"],
        data = [config_file] + srcs,
        args = ["$(rlocationpath " + config_file + ")"] + [package_name + "/" + x for x in srcs],
        jvm_flags = [
            "--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
            "--add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
        ],
        **kwargs
    )

def spotless(name, srcs, config_file, tags = [], **kwargs):
    package_name = native.package_name()

    # print(native.workspace_name)
    spotless_check(name = name + ".check", srcs = srcs, config_file = config_file, package_name = package_name, target_name = name, tags = ["_spotless"], **kwargs)
    spotless_apply(name = name + ".apply", srcs = srcs, config_file = config_file, package_name = package_name, tags = ["_spotless_apply"], **kwargs)
    # print(name)
