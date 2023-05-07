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

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.diffplug.bazel.spotless.config.GenericConfig;
import com.diffplug.spotless.FormatExceptionPolicy;
import com.diffplug.spotless.FormatExceptionPolicyStrict;
import com.diffplug.spotless.Formatter;
import com.diffplug.spotless.FormatterStep;
import com.diffplug.spotless.LineEnding;
import com.diffplug.spotless.Provisioner;
import com.diffplug.spotless.generic.EndWithNewlineStep;
import com.diffplug.spotless.generic.IndentStep;

import static java.util.Objects.requireNonNull;

public class BaseFormatter {
	private final List<FormatterStep> steps;

	private FormatExceptionPolicy exceptionPolicy = new FormatExceptionPolicyStrict();
	private LineEnding.Policy lineEndingPolicy = LineEnding.PLATFORM_NATIVE.createPolicy();
	private String encoding = "UTF-8";

	protected final Provisioner provisioner;

	protected BaseFormatter(GenericConfig config) {
		steps = new ArrayList<>();
		provisioner = new BazelProvisioner();

		if (config.getEndWithNewline()) {
			addStep(EndWithNewlineStep.create());
		}
		if (config.hasIndentWithSpacesSpacesPerTab()) {
			addStep(IndentStep.Type.SPACE.create(config.getIndentWithSpacesSpacesPerTab()));
		}
		if (config.hasIndentWithTabsTabToSpaces()) {
			addStep(IndentStep.Type.TAB.create(config.getIndentWithTabsTabToSpaces()));
		}
	}

	protected void addStep(FormatterStep newStep) {
		requireNonNull(newStep);
		int existingIdx = getExistingStepIdx(newStep.getName());
		if (existingIdx != -1) {
			throw new RuntimeException("Multiple steps with name '" + newStep.getName() + "'");
		}
		steps.add(newStep);
	}

	private int getExistingStepIdx(String stepName) {
		for (int i = 0; i < steps.size(); ++i) {
			if (steps.get(i).getName().equals(stepName)) {
				return i;
			}
		}
		return -1;
	}

	public Formatter buildFormatter(Path rootDir) {
		return Formatter.builder()
				.lineEndingsPolicy(lineEndingPolicy)
				.encoding(Charset.forName(encoding))
				.rootDir(rootDir)
				.steps(steps)
				.exceptionPolicy(exceptionPolicy)
				.build();
	}

	public String getEncoding() {
		return encoding;
	}
}
