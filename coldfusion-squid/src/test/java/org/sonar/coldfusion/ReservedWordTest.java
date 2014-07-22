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

public class ReservedWordTest {

  LexerlessGrammar g = CFGrammar.createGrammar();

  /*
   * ANTLR grammar
   * reservedWord
   * : CONTAINS | IS | EQUAL
   * | EQ | NEQ | GT | LT | GTE
   * | GE | LTE | LE | NOT | AND
   * | OR | XOR | EQV | IMP | MOD
   * | NULL | EQUALS
   * | cfscriptKeywords
   * ;
   */
  @Test
  public void realLife() {
    assertThat(g.rule(CFGrammar.RESERVED_WORD))
      .matches("contains")
      .matches("is")
      .matches("equal")
      .matches("eq")
      .matches("neq")
      .matches("gt")
      .matches("lt")
      .matches("gte")
      .matches("ge")
      .matches("lte")
      .matches("le")
      .matches("not")
      .matches("and")
      .matches("or")
      .matches("xor")
      .matches("eqv")
      .matches("imp")
      .matches("mod")
      .matches("null")
      .matches("equals")

      // CFScriptKeywords
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
