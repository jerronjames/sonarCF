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
package org.sonar.coldfusion.metrics;

import com.sonar.sslr.api.AstNode;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.coldfusion.api.CFMetric;
import org.sonar.coldfusion.api.CFPunctuator;
import org.sonar.coldfusion.parser.CFGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

public class ComplexityVisitor extends SquidAstVisitor<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(
      CFGrammar.FUNCTION_DECLARATION,
      CFGrammar.COMPOUND_STATEMENT,
      // Branching nodes
      CFGrammar.STATEMENT,
      // Expressions
      CFPunctuator.ANDOPERATOR,
      CFPunctuator.OROPERATOR,
      CFPunctuator.QUESTIONMARK);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(CFGrammar.RETURN_STATEMENT) && isLastReturnStatement(astNode)) {
      return;
    }
    getContext().peekSourceCode().add(CFMetric.COMPLEXITY, 1);
  }

  private boolean isLastReturnStatement(AstNode astNode) {
    AstNode parent = astNode.getParent().getParent();
    return parent.is(CFGrammar.STATEMENT);
  }

}
