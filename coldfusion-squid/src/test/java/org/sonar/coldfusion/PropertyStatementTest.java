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

public class PropertyStatementTest {

  LexerlessGrammar g = CFGrammar.createGrammar();
  
  /* antlr grammar
	  propertyStatement
	  : PROPERTY paramStatementAttributes  
	  ;
  */
  @Test
  public void ok() {
    assertThat(g.rule(CFGrammar.PROPERTY_STATEMENT))
        .matches("property identifier = impliesExpression")
        .as("Multiple paramStatementAttributes").matches("property identifier = impliesExpression identifier = impliesExpression");
	
    assertThat(g.rule(CFGrammar.PROPERTY_STATEMENT))
	  .notMatches("identifier = impliesExpression")
	  .notMatches("identifier : impliesExpression");
  }
}
