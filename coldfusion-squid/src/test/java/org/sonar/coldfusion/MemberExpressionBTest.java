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

import org.junit.Test;
import org.sonar.coldfusion.parser.CFGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class MemberExpressionBTest {

  LexerlessGrammar g = CFGrammar.createGrammar();
  
  /* antlr grammar
	memberExpressionB
	  : ( primaryExpression -> primaryExpression ) // set return tree to just primary
	  ( 
	  : DOT primaryExpressionIRW LEFTPAREN argumentList ')' -> ^(JAVAMETHODCALL $memberExpressionB primaryExpressionIRW argumentList )
	    |  LEFTPAREN argumentList RIGHTPAREN -> ^(FUNCTIONCALL $memberExpressionB argumentList)
	    | LEFTBRACKET impliesExpression RIGHTBRACKET -> ^(LEFTBRACKET $memberExpressionB impliesExpression)
	    | DOT primaryExpressionIRW -> ^(DOT $memberExpressionB primaryExpressionIRW)
	  )*
	  ;
   */
  @Test
  public void ok() {
    assertThat(g.rule(CFGrammar.MEMBER_EXPRESSION_B))
    	.matches("primaryExpression")
    	.matches("primaryExpression.is(argument, argument)")
    	.matches("primaryExpression (argument, argument, argument)")
    	.matches("primaryExpression[impliesExpression]")
    	.matches("primaryExpression.equals")
    	
    	.matches("primaryExpression.is(argument, argument) .equals(argument)")
    	.matches("primaryExpression (argument, argument, argument) [impliesExpression]")
    	.matches("primaryExpression[impliesExpression] (argument)")
    	.matches("primaryExpression.equals (argument, list)");
  }
}
