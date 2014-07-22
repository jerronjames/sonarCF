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

public class IdentifiersTest {

  LexerlessGrammar g = CFGrammar.createGrammar();
  String cfscriptKeyword = CFScriptKeywordsTest.getCfscriptKeyword();

  /*
   * ANTLR grammar
   * identifier
   * : IDENTIFIER
   * | DOES
   * | CONTAIN
   * | GREATER
   * | THAN
   * | LESS
   * | VAR
   * | TO
   * | DEFAULT // default is a cfscript keyword that's always allowed as a var name
   * | INCLUDE
   * | NEW
   * | ABORT
   * | THROW
   * | RETHROW
   * | PARAM
   * | EXIT
   * | THREAD
   * | LOCK
   * | TRANSACTION
   * | PUBLIC
   * | PRIVATE
   * | REMOTE
   * | PACKAGE
   * | REQUIRED
   * | cfmlFunction
   * | {!scriptMode}?=> cfscriptKeywords
   * ;
   */
  @Test
  public void ok() {
    assertThat(g.rule(CFGrammar.IDENTIFIERS))
      .matches("identifier")
      .matches("does")
      .matches("contain")
      .matches("greater")
      .matches("than")
      .matches("less")
      .matches("var")
      .matches("to")
      .matches("default")
      .matches("include")
      .matches("new")
      .matches("abort")
      .matches("throw")
      .matches("rethrow")
      .matches("param")
      .matches("exit")
      .matches("thread")
      .matches("lock")
      .matches("transaction")
      .matches("public")
      .matches("private")
      .matches("remote")
      .matches("package")
      .matches("required")

      // CFML_FUNCTION
      .matches("location")
      .matches("savecontent")
      .matches("http")
      .matches("file")
      .matches("directory")
      .matches("loop")
      .matches("setting")
      .matches("query")

      // CF_SCRIPT_KEYWORDS
      .matches("if")
      .matches(cfscriptKeyword)
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

  }

}
