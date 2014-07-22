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

public class IfElseStatementTest {

  LexerlessGrammar g = CFGrammar.createGrammar();

  @Test
  public void ok() {
    assertThat(g.rule(CFGrammar.IF_STATEMENT))
      .matches("if () {}")
      .matches("if (x lt y){ cough = true; }")
      .matches("if (x gte y) {\n}\nelse{\n}")
      .matches("if( isObject(arguments.entity) ){"
        + "local.isAuditable = isAuditable(arguments.entity);"
        + "local.columnDetails = getColumnDetails(arguments.entity);"
        + "}else{"
        + "	local.isAuditable = isAuditable(arguments.oldEntity);"
        + "	local.columnDetails = getColumnDetails(arguments.oldEntity);"
        + "}")
      .matches("if () {} else {}")
      .matches("if () {} else if () {}")
      .matches("if(not isSimpleValue(arguments.struct[local.current].formatFunction)){}")
      .matches("if( local.auditable != 'always' || local.auditable != \"optional\" ){return false;}");
  }

}
