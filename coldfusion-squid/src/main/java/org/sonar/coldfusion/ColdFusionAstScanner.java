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

import org.sonar.coldfusion.CharsetAwareVisitor;
import org.sonar.coldfusion.api.CFMetric;
import org.sonar.coldfusion.metrics.ComplexityVisitor;
import org.sonar.coldfusion.parser.CFGrammar;
import org.sonar.coldfusion.parser.CFParser;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.sslr.parser.LexerlessGrammar;
import com.sonar.sslr.impl.Parser;
import org.sonar.squidbridge.CommentAnalyser;
import org.sonar.squidbridge.SourceCodeBuilderCallback;
import org.sonar.squidbridge.SourceCodeBuilderVisitor;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.indexer.QueryByType;

import java.io.File;
import java.util.Collection;

public final class ColdFusionAstScanner {

  private static class CFCommentAnalyser extends CommentAnalyser {
     @Override
    public boolean isBlank(String line) {
      for (int i = 0; i < line.length(); i++) {
        if (Character.isLetterOrDigit(line.charAt(i))) {
          return false;
        }
      }
      return true;
    }

    @Override
    public String getContents(String comment) {
      if (comment.startsWith("//")) {
        return comment.substring(2);
      } else if (comment.startsWith("/*")) {
        return comment.substring(2, comment.length() - 2);
      } else if (comment.startsWith("<!--")) {
        if (comment.endsWith("-->")) {
          return comment.substring(4, comment.length() - 3);
        }
        return comment.substring(4);
      } else {
        throw new IllegalArgumentException();
      }
    }
  }

  private ColdFusionAstScanner() {
  }

  /**
   * Helper method for testing checks without having to deploy them on a Sonar instance.
   */
  public static SourceFile scanSingleFile(File file, SquidAstVisitor<LexerlessGrammar>... visitors) {
    if (!file.isFile()) {
      throw new IllegalArgumentException("File '" + file + "' not found.");
    }
    AstScanner<LexerlessGrammar> scanner = create(new CFConfiguration(Charsets.UTF_8), visitors);
    scanner.scanFile(file);
    Collection<SourceCode> sources = scanner.getIndex().search(new QueryByType(SourceFile.class));
    if (sources.size() != 1) {
      throw new IllegalStateException("Only one SourceFile was expected whereas " + sources.size() + " has been returned.");
    }
    System.out.println("TESTING " + (SourceFile) sources.iterator().next());
    return (SourceFile) sources.iterator().next();
  }

  public static AstScanner<LexerlessGrammar> create(CFConfiguration conf, SquidAstVisitor<LexerlessGrammar>... visitors) {
    final SquidAstVisitorContextImpl<LexerlessGrammar> context = new SquidAstVisitorContextImpl<LexerlessGrammar>(new SourceProject("ColdFusion Project"));
    final Parser<LexerlessGrammar> parser = CFParser.create(conf);

    AstScanner.Builder<LexerlessGrammar> builder = AstScanner.<LexerlessGrammar>builder(context).setBaseParser(parser);


    return builder.build();
  }

}
