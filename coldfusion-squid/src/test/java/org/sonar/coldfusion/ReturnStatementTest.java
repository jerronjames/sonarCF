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

public class ReturnStatementTest {

  /*
   * returnStatement
   * : RETURN SEMICOLON!
   * | RETURN assignmentExpression SEMICOLON!
   * ;
   */
  LexerlessGrammar g = CFGrammar.createGrammar();

  @Test
  public void ok() {
    assertThat(g.rule(CFGrammar.RETURN_STATEMENT))
      .matches("return;")
      .matches("return false;")
      .matches("return '';")
      .matches("return 'stuff';")
      .matches("return 42;")
      .matches("return request.changeReason;")
      .matches("return local.thisThing;")
      .matches("return prefs.getStuff();")
      .matches("return arguments.functionToUse(args.one, args.two);")
      .matches("return key;")
      .matches("return structArr(ids)[1];")
      .matches("return request['sayYes'];");
  }
}
