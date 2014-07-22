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
package org.sonar.coldfusion.api;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;
import org.sonar.sslr.grammar.GrammarRuleKey;

public enum CFPunctuator implements TokenType, GrammarRuleKey {
  // operators
  DOT("."),
  STAR("*"),
  SLASH("/"),
  BSLASH("\\"),
  POWER("^"),
  PLUS("+"),
  PLUSPLUS("++"),
  MINUS("-"),
  MINUSMINUS("--"),
  MODOPERATOR("%"),
  CONCAT("&"),
  EQUALSEQUALSOP("=="),
  EQUALSOP("="),
  PLUSEQUALS("+="),
  MINUSEQUALS("-="),
  STAREQUALS("*="),
  SLASHEQUALS("/="),
  MODEQUALS("%="),
  CONCATEQUALS("&="),
  COLON(":"),
  NOTOP("!"),
  SEMICOLON(";"),
  OROPERATOR("||"),
  ANDOPERATOR("&&"),
  LBRACKET("["),
  RBRACKET("]"),
  LPARENTHESIS("("),
  RPARENTHESIS(")"),
  LCURLYBRACE("{"),
  RCURLYBRACE("}"),
  QUESTIONMARK("?"),
  NOTEQUAL("!="),
  COMMA(","),
  ELVIS("?:"),
  TILDA("~"),
  RCHEVRON(">"),
  LCHEVRON("<"),
  RCHEVRONEQUAL(">="),
  LCHEVRONEQUAL("<="),
  HASH("#"),
  SINGLEQUOTE("\'"),
  DOUBLEQUOTE("\"");

  private final String value;

  private CFPunctuator(String word) {
    this.value = word;
  }

  public String getName() {
    return name();
  }

  public String getValue() {
    return value;
  }

  public boolean hasToBeSkippedFromAst(AstNode node) {
    return false;
  }

}
