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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import java.io.File;
import java.io.FilenameFilter;

import com.diffplug.spotless.Provisioner;
import java.nio.file.Path;
import com.google.devtools.build.runfiles.Runfiles;

public class BazelProvisioner implements Provisioner {
	private String mavenToFilesystem(String mavenCoordinates) {
		String[] parts = mavenCoordinates.split(":");
		String output = "";
		output += parts[0].replace(".", "/");
		output += "/" + parts[1] + "/" + parts[2] + "/processed_" + parts[1] + "-" + parts[2] + ".jar";
		return output;
	}

	public Set<File> loadFilesFromManifest(Set<String> subpaths) {
		Set<File> output = new HashSet<>();
		Runfiles.Preloaded runfiles;
		try {
			runfiles = Runfiles.preload();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		String artifactBase;
		try(BufferedReader br = new BufferedReader(new FileReader(new File(System.getenv("RUNFILES_MANIFEST_FILE")))))
		{
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("guava")) {
					System.out.println(line);
					break;
				}
			}
			artifactBase = line.substring(0, line.indexOf("/v1/"));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		for (String subpath : subpaths) {
			String depPath = artifactBase + "/v1/https/repo1.maven.org/maven2/" + subpath;
			output.add(new File(runfiles.unmapped().rlocation(depPath)));
		}

		return output;
	}

	private Set<File> loadFilesWithoutManifest(Set<String> subpaths) {
		Set<File> output = new HashSet<>();

		String base = System.getenv("JAVA_RUNFILES");
		if (base.contains("~")) {
				File [] files = new File(base).listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.contains("rules_jvm_external");
				}
			});
			if (files.length != 1) {
				throw new RuntimeException("Could not figure out maven base");
			}
			base = files[0].toString();
		} else {
			base += "/rules_spotless_dependencies";
		}
		// Older versions of rules_jvm_external put the files here
		if (new File(base, "v1").exists()) {
			base += "/v1/https/repo1.maven.org/maven2";
		}

		for (String subpath : subpaths) {
			output.add(new File(base + "/" + subpath));
		}
		return output;
	}

	@Override
	public Set<File> provisionWithTransitives(boolean withTransitives, Collection<String> mavenCoordinates) {
		// System.out.println("Provisioner getting called: " + withTransitives + ", " + mavenCoordinates);

		Set<String> subpaths = new HashSet<>();
		subpaths.add(mavenToFilesystem("com.google.guava:guava:31.0.1-jre"));
		for (String maven : mavenCoordinates) {
			subpaths.add(mavenToFilesystem(maven));
		}

		if ("1".equals(System.getenv("RUNFILES_MANIFEST_ONLY"))) {
			return loadFilesFromManifest(subpaths);
		}

		return loadFilesWithoutManifest(subpaths);
	}
}
