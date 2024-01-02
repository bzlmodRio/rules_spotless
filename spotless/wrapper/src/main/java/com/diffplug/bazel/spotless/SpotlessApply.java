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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import com.google.devtools.build.runfiles.Runfiles;

import com.google.devtools.build.runfiles.AutoBazelRepository;

@AutoBazelRepository
public class SpotlessApply {
	public static void main(String[] args) throws IOException {
		System.out.println(Arrays.toString(args));
		int argPtr = 0;

		Runfiles.Preloaded runfiles = Runfiles.preload();
		String configFile = args[argPtr++];
		configFile = runfiles.unmapped().rlocation(configFile);

		List<String> sourceFiles = Arrays.asList(Arrays.copyOfRange(args, argPtr, args.length));

		Path workspaceDir = new File(System.getenv("BUILD_WORKSPACE_DIRECTORY")).toPath();
		Path outputDirectory = new File(System.getenv("BUILD_WORKSPACE_DIRECTORY")).toPath();

		SpotlessFormatter formatter = new SpotlessFormatter(workspaceDir, configFile);
		List<File> failedFiles = formatter.processFiles(outputDirectory, sourceFiles);

		if (!failedFiles.isEmpty()) {
			System.out.println("Ran formatter on \n");
			for (File f : failedFiles) {
				System.out.println("  " + f);
			}
		}
	}
}
