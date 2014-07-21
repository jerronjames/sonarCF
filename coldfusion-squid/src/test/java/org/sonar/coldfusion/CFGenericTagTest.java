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

public class CFGenericTagTest {

  LexerlessGrammar g = CFGrammar.createGrammar();

  // @Test
  // public void singleTag() {
  //   assertThat(g.rule(CFGrammar.ROOT))
  //     .matches("<cftag/>")                 
  //     .matches("<tag ignore = \"true\"/>");
  // }

  // @Test
  // public void startAndEndTag() {
  //   assertThat(g.rule(CFGrammar.ROOT))
  //     .matches("<cftag></cftag>")
  //     .matches("<cftag>\nanalyze = true;\n</cftag>");  
  // }

  @Test
  public void allTag() {
    assertThat(g.rule(CFGrammar.ROOT))
      //.matches("<cftag>this is a tag</cftag>")
      .matches("<cftag analyze = false />")
      .matches("<cfset caller.addition = attributes.first + attributes.second />")
      .matches("<cfexit method=\"exitTag\" />")
      .matches("<cf_addition first=\"1\" second=\"2\">")
      .matches("<cftag analyze = false />\n"
      +"    <cfset caller.addition = attributes.first + attributes.second />\n"
      +"    <cfscript>\n"
      +"    component {}\n"
      +"   </cfscript>");
  }
}
