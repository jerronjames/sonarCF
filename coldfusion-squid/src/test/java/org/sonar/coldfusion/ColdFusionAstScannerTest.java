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
package org.sonar.coldfusion;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import org.junit.Ignore;
import org.junit.Test;
import org.sonar.coldfusion.api.CFMetric;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class ColdFusionAstScannerTest {

  @Test
  public void files() {
    AstScanner<LexerlessGrammar> scanner = ColdFusionAstScanner.create(new CFConfiguration(Charsets.UTF_8));
    scanner.scanFiles(ImmutableList.of(new File("src/test/resources/metrics/Comments.cfc"),
      new File("src/test/resources/metrics/Functions.cfc")));
    SourceProject project = (SourceProject) scanner.getIndex().search(new QueryByType(SourceProject.class)).iterator().next();
    assertThat(project.getInt(CFMetric.FILES)).isEqualTo(2);
  }

  @Ignore("Need to fix AstScanner")
  @Test
  public void scriptBlocks() {
    SourceFile file = ColdFusionAstScanner.scanSingleFile(new File("src/test/resources/metrics/Functions.cfc"));
    assertThat(file.getInt(CFMetric.SCRIPT_BLOCKS)).isEqualTo(1);
  }

  @Ignore
  @Test
  public void functions() {
    SourceFile file = ColdFusionAstScanner.scanSingleFile(new File("src/test/resources/metrics/Functions.cfc"));
    assertThat(file.getInt(CFMetric.FUNCTIONS)).isEqualTo(4);
  }

  @Ignore
  @Test
  public void components() {
    SourceFile file = ColdFusionAstScanner.scanSingleFile(new File("src/test/resources/metrics/Functions.cfc"));
    assertThat(file.getInt(CFMetric.COMPONENTS)).isEqualTo(1);
  }

  @Test
  public void comments() {
    SourceFile file = ColdFusionAstScanner.scanSingleFile(new File("src/test/resources/metrics/Comments.cfc"));
    assertThat(file.getInt(CFMetric.COMMENT_LINES)).isEqualTo(13);
  }

  @Ignore
  @Test
  public void linesOfCode() {
    SourceFile file = ColdFusionAstScanner.scanSingleFile(new File("src/test/resources/metrics/Comments.cfc"));
    assertThat(file.getInt(CFMetric.LINES_OF_CODE)).isEqualTo(4);
    file = ColdFusionAstScanner.scanSingleFile(new File("src/test/resources/metrics/Complex.cfc"));
    assertThat(file.getInt(CFMetric.LINES_OF_CODE)).isEqualTo(13);
  }

  @Ignore
  @Test
  public void ifStatements() {
    SourceFile file = ColdFusionAstScanner.scanSingleFile(new File("src/test/resources/metrics/IfStatement.cfc"));
    assertThat(file.getInt(CFMetric.STATEMENTS)).isEqualTo(2);
  }
}
