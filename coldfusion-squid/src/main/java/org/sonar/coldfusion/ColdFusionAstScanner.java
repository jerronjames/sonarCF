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
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.Parser;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.coldfusion.api.CFMetric;
import org.sonar.coldfusion.metrics.ComplexityVisitor;
import org.sonar.coldfusion.parser.CFGrammar;
import org.sonar.coldfusion.parser.CFParser;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.SourceCodeBuilderCallback;
import org.sonar.squidbridge.SourceCodeBuilderVisitor;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;
import org.sonar.squidbridge.metrics.CommentsVisitor;
import org.sonar.squidbridge.metrics.CounterVisitor;
import org.sonar.squidbridge.metrics.LinesVisitor;
import org.sonar.squidbridge.metrics.LinesOfCodeVisitor;

import java.io.File;
import java.util.Collection;

public final class ColdFusionAstScanner {

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
    return (SourceFile) sources.iterator().next();
  }

  /**
    * Create method
    */
  public static AstScanner<LexerlessGrammar> create(CFConfiguration conf, SquidAstVisitor<LexerlessGrammar>... visitors) {
    final SquidAstVisitorContextImpl<LexerlessGrammar> context = new SquidAstVisitorContextImpl<LexerlessGrammar>(new SourceProject("ColdFusion Project"));
    final Parser<LexerlessGrammar> parser = CFParser.create(conf);

    // create Builder to add to
    AstScanner.Builder<LexerlessGrammar> builder = AstScanner.<LexerlessGrammar>builder(context).setBaseParser(parser);

    builder.withMetrics(CFMetric.values()); // get all Metrics available
    builder.setCommentAnalyser(new CFCommentAnalyser()); // what to with comments
    builder.setFilesMetric(CFMetric.FILES);

    // Script blocks (CFML_STATEMENTS, SCRIPT_BLOCKS)
    builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<LexerlessGrammar>(new SourceCodeBuilderCallback() {
      private int seq = 0;

      @Override
      public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
        seq++;
        SourceClass cls = new SourceClass("script block:" + seq);
        cls.setStartAtLine(astNode.getTokenLine());
        return cls;
      }
    }, CFGrammar.SCRIPT_BLOCK, CFGrammar.CFML_STATEMENT));

    builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar>builder().setMetricDef(CFMetric.SCRIPT_BLOCKS)
      .subscribeTo(CFGrammar.SCRIPT_BLOCK, CFGrammar.CFML_STATEMENT).build());

    // END SCRIPT_BLOCKS

    // Components
    builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<LexerlessGrammar>(new SourceCodeBuilderCallback() {
      private int seq = 0;

      @Override
      public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
        seq++;
        SourceFunction function = new SourceFunction("component:" + seq);
        function.setStartAtLine(astNode.getTokenLine());
        return function;
      }
    }, CFGrammar.COMPONENT_DECLARATION));

    builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar>builder()
      .setMetricDef(CFMetric.COMPONENTS)
      .subscribeTo(CFGrammar.COMPONENT_DECLARATION)
      .build());
    // END COMPONENTS

    // Functions
    builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<LexerlessGrammar>(new SourceCodeBuilderCallback() {
      private int seq = 0;

      @Override
      public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
        seq++;
        SourceFunction function = new SourceFunction("function:" + seq);
        function.setStartAtLine(astNode.getTokenLine());
        return function;
      }
    }, CFGrammar.FUNCTION_DECLARATION));

    builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar>builder()
      .setMetricDef(CFMetric.FUNCTIONS)
      .subscribeTo(CFGrammar.FUNCTION_DECLARATION)
      .build());

    // END FUNCTIONS

    // Lines of code and comments
    builder.withSquidAstVisitor(new LinesVisitor<LexerlessGrammar>(CFMetric.LINES));
    builder.withSquidAstVisitor(new LinesOfCodeVisitor<LexerlessGrammar>(CFMetric.LINES_OF_CODE) {
      @Override
      public void visitToken(Token token) {
          super.visitToken(token);
      }
    });

    builder.withSquidAstVisitor(new ComplexityVisitor());
    builder.withSquidAstVisitor(CommentsVisitor.<LexerlessGrammar>builder().withCommentMetric(CFMetric.COMMENT_LINES)
      .withNoSonar(true)
      .withIgnoreHeaderComment(conf.getIgnoreHeaderComments())
      .build());

    // END LINES OF CODE

    // Statements
    builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar>builder()
      .setMetricDef(CFMetric.STATEMENTS)
      .subscribeTo(
          CFGrammar.IF_STATEMENT,
          CFGrammar.WHILE_STATEMENT,
          CFGrammar.FOR_STATEMENT,
          CFGrammar.DO_WHILE_STATEMENT,
          CFGrammar.SWITCH_STATEMENT,
          CFGrammar.TRY_CATCH_STATEMENT,
          CFGrammar.LOCK_STATEMENT,
          CFGrammar.THREAD_STATEMENT,
          CFGrammar.COMPOUND_STATEMENT,
          CFGrammar.RETURN_STATEMENT,
          CFGrammar.FINALLY_STATEMENT,
          CFGrammar.TAG_OPERATOR_STATEMENT,
          CFGrammar.INCLUDE_STATEMENT,
          CFGrammar.TRANSACTION_STATEMENT,
          CFGrammar.ABORT_STATEMENT,
          CFGrammar.THROW_STATEMENT,
          CFGrammar.EXIT_STATEMENT,
          CFGrammar.PARAM_STATEMENT,
          CFGrammar.PROPERTY_STATEMENT
        ).build());

    // END STATEMENTS

    /* External visitors (typically Check ones) */
    for (SquidAstVisitor<LexerlessGrammar> visitor : visitors) {
      if (visitor instanceof CharsetAwareVisitor) {
        ((CharsetAwareVisitor) visitor).setCharset(conf.getCharset());
      }
      builder.withSquidAstVisitor(visitor);
    }

    return builder.build();
  }

}
