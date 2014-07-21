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

public enum CFKeyword implements TokenType, GrammarRuleKey {

  // Operators
CONTAINS("contains"),
CONTAIN("contain"),
DOES("does"),
IS("is"),
GT("gt"),
GE("ge"),
GTE("gte"),
LTE("lte"),
LT("lt"),
LE("le"),
EQ("eq"),
EQUAL("equal"),
EQUALS("equals"),
NEQ("neq"),
LESS("less"),
THAN("than"),
GREATER("greater"),
OR("or"),
TO("to"),
IMP("imp"),
EQV("eqv"),
XOR("xor"),
AND("and"),
NOT("not"),
MOD("mod"),
VAR("var"),
NEW("new"),
LOCAL("local"),
VARIABLES("variables"),
THIS("this"),
ARGUMENTS("arguments"),
NULL("null"),
TRUE("true"),
FALSE("false"),

// cfscript
IF("if"),
ELSE("else"),
BREAK("break"),
CONTINUE("continue"),
FUNCTION("function"),
RETURN("return"),
WHILE("while"),
DO("do"),
FOR("for"),
IN("in"),
TRY("try"),
CATCH("catch"),
SWITCH("switch"),
CASE("case"),
DEFAULT("default"),
FINALLY("finally"),

SCRIPTCLOSE("</cfscript>"), 


// tag operators
INCLUDE("include"),
IMPORT("import"),
ABORT("abort"),
THROW("throw"),
RETHROW("rethrow"),
EXIT("exit"),
PARAM("param"),
PROPERTY("property"),
LOCK("lock"),
THREAD("thread"),
TRANSACTION("transaction"),

// cfmlfunction (tags you can call from script)
LOCATION("location"),
SAVECONTENT("savecontent"),
HTTP("http"),
FILE("file"),
DIRECTORY("directory"),
LOOP("loop"), 
SETTING("setting"),
QUERY("query"),

// function related
PRIVATE("private"),
PUBLIC("public"),
REMOTE("remote"),
PACKAGE("package"),
REQUIRED("required"),
COMPONENT("component"),

// CFML
CFSCRIPTSTART("<cfscript>"),
CFSCRIPTEND("</cfscript>");

  private final String value;

  private CFKeyword(String value) {
    this.value = value;
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

  public static String[] keywordValues() {
    CFKeyword[] keywordsEnum = CFKeyword.values();
    String[] keywords = new String[keywordsEnum.length];
    for (int i = 0; i < keywords.length; i++) {
      keywords[i] = keywordsEnum[i].getValue();
    }
    return keywords;
  }

}
