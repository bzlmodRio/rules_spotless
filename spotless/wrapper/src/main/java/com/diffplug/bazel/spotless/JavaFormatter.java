/*
 * Copyright 2023 DiffPlug
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diffplug.bazel.spotless;

import com.diffplug.bazel.spotless.config.EclipseConfig;
import com.diffplug.bazel.spotless.config.FormatAnnotationsConfig;
import com.diffplug.bazel.spotless.config.GenericConfig;
import com.diffplug.bazel.spotless.config.GoogleJavaFormatConfig;
import com.diffplug.bazel.spotless.config.ImportOrderConfig;
import com.diffplug.bazel.spotless.config.JavaConfig;
import com.diffplug.bazel.spotless.config.PalantirJavaFormatConfig;
import com.diffplug.spotless.extra.EclipseBasedStepBuilder;
import com.diffplug.spotless.extra.java.EclipseJdtFormatterStep;
import com.diffplug.spotless.java.FormatAnnotationsStep;
import com.diffplug.spotless.java.GoogleJavaFormatStep;
import com.diffplug.spotless.java.ImportOrderStep;
import com.diffplug.spotless.java.PalantirJavaFormatStep;
import com.diffplug.spotless.java.RemoveUnusedImportsStep;

public class JavaFormatter extends BaseFormatter {
	public JavaFormatter(GenericConfig genericConfig, JavaConfig config) {
		super(genericConfig);

		if (config.hasImportOrder()) {
			createImportOrderStep(config.getImportOrder());
		}

		if (config.hasRemoveUnusedImports()) {
			addStep(RemoveUnusedImportsStep.create(provisioner));
		}

		if (config.hasGoogleJavaFormat()) {
			createGoogleJavaFormatStep(config.getGoogleJavaFormat());
		}

		if (config.hasPalantirJavaFormat()) {
			createPalantirJavaFormatStep(config.getPalantirJavaFormat());
		}

		if (config.hasEclipse()) {
			createEclipseJavaFormatStep(config.getEclipse());
		}

		if (config.hasFormatAnnotations()) {
			createFormatAnnotationFormatStep(config.getFormatAnnotations());
		}
	}

	private void createFormatAnnotationFormatStep(FormatAnnotationsConfig config) {
		addStep(FormatAnnotationsStep.create(
				config.getAddedTypeAnnotationsList(),
				config.getRemovedTypeAnnotationsList()));
	}

	private void createPalantirJavaFormatStep(PalantirJavaFormatConfig config) {
		addStep(PalantirJavaFormatStep.create(config.getVersion(), provisioner));
	}

	private void createEclipseJavaFormatStep(EclipseConfig config) {
		final String version = config.hasVersion() ? config.getVersion() : EclipseJdtFormatterStep.defaultVersion();
		EclipseBasedStepBuilder builder = EclipseJdtFormatterStep.createBuilder(provisioner);
		builder.setVersion(version);
		addStep(builder.build());
	}

	private void createGoogleJavaFormatStep(GoogleJavaFormatConfig config) {
		// System.out.println("Creating google");
		final String version = config.hasVersion() ? config.getVersion() : GoogleJavaFormatStep.defaultVersion();
		String groupArtifact = config.hasGroupArtifact() ? config.getGroupArtifact() : GoogleJavaFormatStep.defaultGroupArtifact();
		String style = config.hasStyle() ? config.getStyle() : GoogleJavaFormatStep.defaultStyle();
		boolean reflowLongStrings = config.hasReflowLongStrings() ? config.getReflowLongStrings() : GoogleJavaFormatStep.defaultReflowLongStrings();

		addStep(GoogleJavaFormatStep.create(
				groupArtifact,
				version,
				style,
				provisioner,
				reflowLongStrings));
	}

	private void createImportOrderStep(ImportOrderConfig config) {
		boolean wildcardsLast = config.hasWildcardsLast() && config.getWildcardsLast();

		if (config.hasImportOrderFile() && !config.getImportOrderList().isEmpty()) {
			throw new IllegalArgumentException("Cannot specify both import order and import order file");
		} else if (config.hasImportOrderFile()) {
			addStep(ImportOrderStep.forJava().createFrom(wildcardsLast, config.getImportOrderFile()));
		} else if (!config.getImportOrderList().isEmpty()) {
			addStep(ImportOrderStep.forJava().createFrom(wildcardsLast, config.getImportOrderList().toArray(new String[]{})));
		}
	}
}
