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

public class CFScriptKeywordsTest {

  LexerlessGrammar g = CFGrammar.createGrammar();


  public static String getCfscriptKeyword() {
	  return "if";
  }
  
  /* ANTLR grammar
   cfscriptKeywords
	  : IF
	  | ELSE
	  | BREAK
	  | CONTINUE
	  | FUNCTION
	  | RETURN
	  | WHILE
	  | DO
	  | FOR
	  | IN
	  | TRY
	  | CATCH
	  | SWITCH
	  | CASE
	  | DEFAULT
	  | IMPORT
	  ;
   */
  @Test
  public void ok() {
	  assertThat(g.rule(CFGrammar.CF_SCRIPT_KEYWORDS))
	      .matches("if")
	      .matches("else")
	      .matches("break")
	      .matches("continue")
	      .matches("function")
	      .matches("return")
	      .matches("while")
	      .matches("do")
	      .matches("for")
	      .matches("in")
	      .matches("try")
	      .matches("catch")
	      .matches("switch")
	      .matches("case")
	      .matches("default")
	      .matches("import");

	  assertThat(g.rule(CFGrammar.CF_SCRIPT_KEYWORDS))
	      .notMatches("random")
	      .notMatches("words");

  }

}
