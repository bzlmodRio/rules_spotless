package rules_spotless;


public class JavaFormatter {
	private final JavaExtension javaExtension;

	public JavaFormatter() {
		javaExtension = new JavaExtension();

		javaExtension.googleJavaFormat();
		javaExtension.removeUnusedImports();
		javaExtension.trimTrailingWhitespace();
		javaExtension.endWithNewline();
	}

	FormatExtension getExtension() { return javaExtension; }
}