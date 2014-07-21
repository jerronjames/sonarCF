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
package org.sonar.coldfusion.model;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.impl.Parser;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.junit.Test;

import org.sonar.coldfusion.CFConfiguration;
import org.sonar.coldfusion.api.CFKeyword;
import org.sonar.coldfusion.api.CFPunctuator;
import org.sonar.coldfusion.api.CFTokenType;
import org.sonar.coldfusion.api.CFMetric;
import org.sonar.coldfusion.parser.CFGrammar;
import org.sonar.coldfusion.parser.CFParser;
import org.sonar.coldfusion.ColdFusionAstScanner;

import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class ASTMakerTest {

  @Test
  public void files() {
    AstScanner<LexerlessGrammar> scanner = ColdFusionAstScanner.create(new CFConfiguration(Charsets.UTF_8));
    scanner.scanFiles(ImmutableList.of(new File("src/test/resources/metrics/Comments.cfc")));
    SourceProject project = (SourceProject) scanner.getIndex().search(new QueryByType(SourceProject.class)).iterator().next();
    assertThat(project.getInt(CFMetric.FILES)).isEqualTo(1);
  }

}
