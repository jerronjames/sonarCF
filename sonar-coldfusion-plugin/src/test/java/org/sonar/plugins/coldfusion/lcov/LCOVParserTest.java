/*
 * SonarQube ColdFusion Plugin
 * Copyright (C) 2014 SonarSource and MC
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.coldfusion.lcov;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.measures.CoverageMeasuresBuilder;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.utils.SonarException;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LCOVParserTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private ProjectFileSystem projectFileSystem = mock(ProjectFileSystem.class);
  private LCOVParser parser = new LCOVParser(projectFileSystem);

  @Test
  public void test() {
    File file1 = new File("file1.js");
    when(projectFileSystem.resolvePath("file1.js")).thenReturn(file1);
    File file2 = new File("file2.js");
    when(projectFileSystem.resolvePath("./file2.js")).thenReturn(file2);

    Map<String, CoverageMeasuresBuilder> result = parser.parse(Arrays.asList(
        "SF:file1.js",
        "DA:1,1",
        "end_of_record",
        "SF:./file2.js",
        "FN:2,(anonymous_1)",
        "FNDA:2,(anonymous_1)",
        "DA:1,1",
        "DA:2,0",
        "BRDA:11,0,0,2",
        "BRDA:11,0,1,1",
        "BRDA:11,0,2,0",
        "BRDA:11,0,3,-",
        "end_of_record"));
    assertThat(result).hasSize(2);

    CoverageMeasuresBuilder fileCoverage = result.get(file1.getAbsolutePath());
    assertThat(fileCoverage.getLinesToCover()).isEqualTo(1);
    assertThat(fileCoverage.getCoveredLines()).isEqualTo(1);
    assertThat(fileCoverage.getConditions()).isEqualTo(0);
    assertThat(fileCoverage.getCoveredConditions()).isEqualTo(0);

    fileCoverage = result.get(file2.getAbsolutePath());
    assertThat(fileCoverage.getLinesToCover()).isEqualTo(2);
    assertThat(fileCoverage.getCoveredLines()).isEqualTo(1);
    assertThat(fileCoverage.getConditions()).isEqualTo(4);
    assertThat(fileCoverage.getCoveredConditions()).isEqualTo(2);
  }

  @Test
  public void merge() {
    File file = new File("file.js");
    when(projectFileSystem.resolvePath("file.js")).thenReturn(file);

    Map<String, CoverageMeasuresBuilder> result = parser.parse(Arrays.asList(
      "SF:file.js",
      "BRDA:1,0,0,-",
      "BRDA:1,0,1,1",
      "DA:2,1",
      "end_of_record",
      "SF:file.js",
      "BRDA:1,0,0,1",
      "BRDA:1,0,1,-",
      "DA:3,1",
      "end_of_record"));

    CoverageMeasuresBuilder fileCoverage = result.get(file.getAbsolutePath());
    assertThat(fileCoverage.getLinesToCover()).isEqualTo(2);
    assertThat(fileCoverage.getCoveredLines()).isEqualTo(2);
    assertThat(fileCoverage.getConditions()).isEqualTo(2);
    assertThat(fileCoverage.getCoveredConditions()).isEqualTo(2);
  }

  @Test
  public void unreadable_file() {
    thrown.expect(SonarException.class);
    thrown.expectMessage("Could not read content from file: not-found");
    parser.parseFile(new File("not-found"));
  }

}
