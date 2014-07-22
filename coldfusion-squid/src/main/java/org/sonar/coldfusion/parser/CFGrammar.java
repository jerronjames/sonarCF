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

package org.sonar.coldfusion.parser;

import com.sonar.sslr.api.GenericTokenType;
import org.sonar.coldfusion.api.CFKeyword;
import org.sonar.coldfusion.api.CFPunctuator;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.sonar.coldfusion.api.CFKeyword.ABORT;
import static org.sonar.coldfusion.api.CFKeyword.AND;
import static org.sonar.coldfusion.api.CFKeyword.BREAK;
import static org.sonar.coldfusion.api.CFKeyword.CASE;
import static org.sonar.coldfusion.api.CFKeyword.CATCH;
import static org.sonar.coldfusion.api.CFKeyword.CFSCRIPTEND;
import static org.sonar.coldfusion.api.CFKeyword.CFSCRIPTSTART;
import static org.sonar.coldfusion.api.CFKeyword.COMPONENT;
import static org.sonar.coldfusion.api.CFKeyword.CONTAIN;
import static org.sonar.coldfusion.api.CFKeyword.CONTAINS;
import static org.sonar.coldfusion.api.CFKeyword.CONTINUE;
import static org.sonar.coldfusion.api.CFKeyword.DEFAULT;
import static org.sonar.coldfusion.api.CFKeyword.DIRECTORY;
import static org.sonar.coldfusion.api.CFKeyword.DO;
import static org.sonar.coldfusion.api.CFKeyword.DOES;
import static org.sonar.coldfusion.api.CFKeyword.ELSE;
import static org.sonar.coldfusion.api.CFKeyword.EQ;
import static org.sonar.coldfusion.api.CFKeyword.EQUAL;
import static org.sonar.coldfusion.api.CFKeyword.EQUALS;
import static org.sonar.coldfusion.api.CFKeyword.EQV;
import static org.sonar.coldfusion.api.CFKeyword.EXIT;
import static org.sonar.coldfusion.api.CFKeyword.FALSE;
import static org.sonar.coldfusion.api.CFKeyword.FILE;
import static org.sonar.coldfusion.api.CFKeyword.FINALLY;
import static org.sonar.coldfusion.api.CFKeyword.FOR;
import static org.sonar.coldfusion.api.CFKeyword.FUNCTION;
import static org.sonar.coldfusion.api.CFKeyword.GE;
import static org.sonar.coldfusion.api.CFKeyword.GREATER;
import static org.sonar.coldfusion.api.CFKeyword.GT;
import static org.sonar.coldfusion.api.CFKeyword.GTE;
import static org.sonar.coldfusion.api.CFKeyword.HTTP;
import static org.sonar.coldfusion.api.CFKeyword.IF;
import static org.sonar.coldfusion.api.CFKeyword.IMP;
import static org.sonar.coldfusion.api.CFKeyword.IMPORT;
import static org.sonar.coldfusion.api.CFKeyword.IN;
import static org.sonar.coldfusion.api.CFKeyword.INCLUDE;
import static org.sonar.coldfusion.api.CFKeyword.IS;
import static org.sonar.coldfusion.api.CFKeyword.LE;
import static org.sonar.coldfusion.api.CFKeyword.LESS;
import static org.sonar.coldfusion.api.CFKeyword.LOCAL;
import static org.sonar.coldfusion.api.CFKeyword.LOCATION;
import static org.sonar.coldfusion.api.CFKeyword.LOCK;
import static org.sonar.coldfusion.api.CFKeyword.LOOP;
import static org.sonar.coldfusion.api.CFKeyword.LT;
import static org.sonar.coldfusion.api.CFKeyword.LTE;
import static org.sonar.coldfusion.api.CFKeyword.MOD;
import static org.sonar.coldfusion.api.CFKeyword.NEQ;
import static org.sonar.coldfusion.api.CFKeyword.NEW;
import static org.sonar.coldfusion.api.CFKeyword.NOT;
import static org.sonar.coldfusion.api.CFKeyword.NULL;
import static org.sonar.coldfusion.api.CFKeyword.OR;
import static org.sonar.coldfusion.api.CFKeyword.PACKAGE;
import static org.sonar.coldfusion.api.CFKeyword.PARAM;
import static org.sonar.coldfusion.api.CFKeyword.PRIVATE;
import static org.sonar.coldfusion.api.CFKeyword.PROPERTY;
import static org.sonar.coldfusion.api.CFKeyword.PUBLIC;
import static org.sonar.coldfusion.api.CFKeyword.QUERY;
import static org.sonar.coldfusion.api.CFKeyword.REMOTE;
import static org.sonar.coldfusion.api.CFKeyword.REQUIRED;
import static org.sonar.coldfusion.api.CFKeyword.RETHROW;
import static org.sonar.coldfusion.api.CFKeyword.RETURN;
import static org.sonar.coldfusion.api.CFKeyword.SAVECONTENT;
import static org.sonar.coldfusion.api.CFKeyword.SETTING;
import static org.sonar.coldfusion.api.CFKeyword.SWITCH;
import static org.sonar.coldfusion.api.CFKeyword.THAN;
import static org.sonar.coldfusion.api.CFKeyword.THIS;
import static org.sonar.coldfusion.api.CFKeyword.THREAD;
import static org.sonar.coldfusion.api.CFKeyword.THROW;
import static org.sonar.coldfusion.api.CFKeyword.TO;
import static org.sonar.coldfusion.api.CFKeyword.TRANSACTION;
import static org.sonar.coldfusion.api.CFKeyword.TRUE;
import static org.sonar.coldfusion.api.CFKeyword.TRY;
import static org.sonar.coldfusion.api.CFKeyword.VAR;
import static org.sonar.coldfusion.api.CFKeyword.VARIABLES;
import static org.sonar.coldfusion.api.CFKeyword.WHILE;
import static org.sonar.coldfusion.api.CFKeyword.XOR;
import static org.sonar.coldfusion.api.CFPunctuator.ANDOPERATOR;
import static org.sonar.coldfusion.api.CFPunctuator.BSLASH;
import static org.sonar.coldfusion.api.CFPunctuator.COLON;
import static org.sonar.coldfusion.api.CFPunctuator.COMMA;
import static org.sonar.coldfusion.api.CFPunctuator.CONCAT;
import static org.sonar.coldfusion.api.CFPunctuator.CONCATEQUALS;
import static org.sonar.coldfusion.api.CFPunctuator.DOT;
import static org.sonar.coldfusion.api.CFPunctuator.DOUBLEQUOTE;
import static org.sonar.coldfusion.api.CFPunctuator.ELVIS;
import static org.sonar.coldfusion.api.CFPunctuator.EQUALSEQUALSOP;
import static org.sonar.coldfusion.api.CFPunctuator.EQUALSOP;
import static org.sonar.coldfusion.api.CFPunctuator.HASH;
import static org.sonar.coldfusion.api.CFPunctuator.LBRACKET;
import static org.sonar.coldfusion.api.CFPunctuator.LCHEVRON;
import static org.sonar.coldfusion.api.CFPunctuator.LCHEVRONEQUAL;
import static org.sonar.coldfusion.api.CFPunctuator.LCURLYBRACE;
import static org.sonar.coldfusion.api.CFPunctuator.LPARENTHESIS;
import static org.sonar.coldfusion.api.CFPunctuator.MINUS;
import static org.sonar.coldfusion.api.CFPunctuator.MINUSEQUALS;
import static org.sonar.coldfusion.api.CFPunctuator.MINUSMINUS;
import static org.sonar.coldfusion.api.CFPunctuator.MODEQUALS;
import static org.sonar.coldfusion.api.CFPunctuator.MODOPERATOR;
import static org.sonar.coldfusion.api.CFPunctuator.NOTEQUAL;
import static org.sonar.coldfusion.api.CFPunctuator.NOTOP;
import static org.sonar.coldfusion.api.CFPunctuator.OROPERATOR;
import static org.sonar.coldfusion.api.CFPunctuator.PLUS;
import static org.sonar.coldfusion.api.CFPunctuator.PLUSEQUALS;
import static org.sonar.coldfusion.api.CFPunctuator.PLUSPLUS;
import static org.sonar.coldfusion.api.CFPunctuator.POWER;
import static org.sonar.coldfusion.api.CFPunctuator.QUESTIONMARK;
import static org.sonar.coldfusion.api.CFPunctuator.RBRACKET;
import static org.sonar.coldfusion.api.CFPunctuator.RCHEVRON;
import static org.sonar.coldfusion.api.CFPunctuator.RCHEVRONEQUAL;
import static org.sonar.coldfusion.api.CFPunctuator.RCURLYBRACE;
import static org.sonar.coldfusion.api.CFPunctuator.RPARENTHESIS;
import static org.sonar.coldfusion.api.CFPunctuator.SEMICOLON;
import static org.sonar.coldfusion.api.CFPunctuator.SINGLEQUOTE;
import static org.sonar.coldfusion.api.CFPunctuator.SLASH;
import static org.sonar.coldfusion.api.CFPunctuator.SLASHEQUALS;
import static org.sonar.coldfusion.api.CFPunctuator.STAR;
import static org.sonar.coldfusion.api.CFPunctuator.STAREQUALS;
import static org.sonar.coldfusion.api.CFPunctuator.TILDA;
import static org.sonar.coldfusion.api.CFTokenType.NUMERIC_LITERAL;
import static org.sonar.coldfusion.api.CFTokenType.REGULAR_EXPRESSION_LITERAL;

public enum CFGrammar implements GrammarRuleKey {

  /**
   * End of file.
   */
  EOF,

  /**
   * End of statement.
   */
  EOS,
  EOS_NO_LB,

  IDENTIFIERS,
  IDENTIFIER,
  PROPERTY_SET_PARAMETER_LIST,

  ACCESS_MODIFIERS,

  CONDITION,

  TAG_START,
  TAG_END,

  // A.1 Lexical

  LITERAL,
  NULL_LITERAL,
  BOOLEAN_LITERAL,
  STRING_LITERAL,
  EMPTY_STRING,

  KEYWORD,
  LETTER_OR_DIGIT,

  /**
   * Spacing.
   */
  SPACING,

  /**
   * Spacing without line break.
   */
  SPACING_NO_LB,
  NEXT_NOT_LB,
  LINE_TERMINATOR_SEQUENCE,

  // start cfscript
  ROOT,
  VARIABLE,
  SCRIPT_BLOCK,
  COMPONENT_DECLARATION,
  END_OF_SCRIPT_BLOCK,
  ELEMENT,
  FUNCTION_DECLARATION,
  FUNCTION_ACCESS_TYPE,
  FUNCTION_RETURN_TYPE,
  ACCESS_TYPE,
  TYPE_SPEC,
  PARAMETER_LIST,
  PARAMETER,
  PARAMETER_TYPE,
  COMPONENT_ATTRIBUTE,
  FUNCTION_ATTRIBUTE,
  COMPOUND_STATEMENT,
  COMPONENT_BLOCK,
  STATEMENT,
  RETURN_STATEMENT,
  IF_STATEMENT,
  WHILE_STATEMENT,
  DO_WHILE_STATEMENT,
  FOR_STATEMENT,
  FOR_IN_KEY,
  TRY_CATCH_STATEMENT,
  CATCH_CONDITION,
  FINALLY_STATEMENT,
  EXCEPTION_TYPE,
  CONSTANT_EXPRESSION,
  SWITCH_STATEMENT,
  CASE_STATEMENT,
  TAG_OPERATOR_STATEMENT,
  INCLUDE_STATEMENT,
  TRANSACTION_STATEMENT,
  CFML_FUNCTION_STATEMENT,
  CFML_FUNCTION,
  LOCK_STATEMENT,
  THREAD_STATEMENT,
  ABORT_STATEMENT,
  THROW_STATEMENT,
  EXIT_STATEMENT,
  PARAM_STATEMENT,
  PROPERTY_STATEMENT,
  PARAM_STATEMENT_ATTRIBUTES,
  PA, // PARAM
  EXPRESSION,
  LOCAL_ASSIGNMENT_EXPRESSION,
  ASSIGNMENT_EXPRESSION,
  IMPLIES_EXPRESSION,
  TERNARY,
  EQUIVALENT_EXPRESSION,
  XOR_EXPRESSION,
  OR_EXPRESSION,
  AND_EXPRESSION,
  NOT_EXPRESSION,
  EQUALITY_EXPRESSION,
  EQUALITY_OPERATOR_1,
  EQUALITY_OPERATOR_2,
  EQUALITY_OPERATOR_3,
  EQUALITY_OPERATOR_5,
  CONCATENATION_EXPRESSION,
  ADDITIVE_EXPRESSION,
  MOD_EXPRESSION,
  INT_DIVISION_EXPRESSION,
  MULTIPLICATIVE_EXPRESSION,
  POWER_OF_EXPRESSION,
  UNARY_EXPRESSION,
  MEMBER_EXPRESSION,
  MEMBER_EXPRESSION_B,
  MEMBER_EXPRESSION_SUFFIX,
  PROPERTY_REFERENCE_SUFFIX,
  INDEX_SUFFIX,
  PRIMARY_EXPRESSION_IRW,
  RESERVED_WORD,
  ARGUMENT_LIST,
  ARGUMENT,
  CF_SCRIPT_KEYWORDS,
  PRIMARY_EXPRESSION,
  IMPLICIT_ARRAY,
  IMPLICIT_ARRAY_ELEMENTS,
  IMPLICIT_STRUCT,
  IMPLICIT_STRUCT_ELEMENTS,
  IMPLICIT_STRUCT_EXPRESSION,
  IMPLICIT_STRUCT_KEY_EXPRESSION,
  NEW_COMPONENT_EXPRESSION,
  COMPONENT_PATH,
  ELVIS_EXPRESSION,

  // start cftags
  GENERIC_ALL_TAG,
  GENERIC_SINGLE_TAG,
  GENERIC_START_TAG,
  GENERIC_END_TAG,
  CF_SINGLE_BEGIN_TAG,
  CF_SINGLE_END_TAG,
  CF_END_TAG,
  CFML_STATEMENT,
  CFML_SINGLE_LINE_TAG,
  CFML_MULTI_LINE_TAG;

  private static final String LINE_TERMINATOR_REGEXP = "\\n\\r\\u2028\\u2029";
  private static final String WHITESPACE_REGEXP = "\\t\\u000B\\f\\u0020\\u00A0\\uFEFF\\p{Zs}";
  private static final String CF_SINGLE_LINE_COMMENT_REGEXP = "//[^\\n\\r]*+|<!--[^\\n\\r]*+";
  private static final String CF_MULTI_LINE_COMMENT_REGEXP = "/\\*[\\s\\S]*?\\*/";
  private static final String MULTI_LINE_COMMENT_REGEXP = "<!--[\\s\\S]*?-->";
  private static final String MULTI_LINE_COMMENT_NO_LB_REGEXP = "<!--[^\\n\\r]*?-->";
  private static final String COMMENT_REGEXP = "(?:" + MULTI_LINE_COMMENT_REGEXP + "|" + CF_MULTI_LINE_COMMENT_REGEXP + "|" + CF_SINGLE_LINE_COMMENT_REGEXP + ")";

  private static final String LITERAL_REGEXP = "[\\s]*(?:"
    + "\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""
    + "|\'([^\'\\\\]*+(\\\\[\\s\\S])?+)*+\'"
    + ")";

  private static final String EXP_REGEXP = "([Ee][+-]?+[0-9_]++)";
  private static final String BINARY_EXP_REGEXP = "([Pp][+-]?+[0-9_]++)";

  private static final String FLOAT_SUFFIX_REGEXP = "[fFdD]";
  private static final String INT_SUFFIX_REGEXP = "[lL]";

  private static final String NUMERIC_LITERAL_REGEXP = "(?:"
    // Decimal
    + "[0-9]++\\.([0-9]++)?+" + EXP_REGEXP + "?+" + FLOAT_SUFFIX_REGEXP + "?+"
    // Decimal
    + "|\\.[0-9]++" + EXP_REGEXP + "?+" + FLOAT_SUFFIX_REGEXP + "?+"
    // Decimal
    + "|[0-9]++" + FLOAT_SUFFIX_REGEXP
    + "|[0-9]++" + EXP_REGEXP + FLOAT_SUFFIX_REGEXP + "?+"
    // Hexadecimal
    + "|0[xX][0-9a-fA-F]++\\.[0-9a-fA-F_]*+" + BINARY_EXP_REGEXP + "?+" + FLOAT_SUFFIX_REGEXP + "?+"
    // Hexadecimal
    + "|0[xX][0-9a-fA-F]++" + BINARY_EXP_REGEXP + FLOAT_SUFFIX_REGEXP + "?+"

    // Integer Literals
    // Hexadecimal
    + "|0[xX][0-9a-fA-F]++" + INT_SUFFIX_REGEXP + "?+"
    // Binary (Java 7)
    + "|0[bB][01]++" + INT_SUFFIX_REGEXP + "?+"
    // Decimal and Octal
    + "|[0-9]++" + INT_SUFFIX_REGEXP + "?+"
    + ")";

  private static final String NON_TERMINATOR_REGEXP = "[^\\r\\n\\u2028\\u2029]";

  private static final String BACKSLASH_SEQUENCE_REGEXP = "\\\\" + NON_TERMINATOR_REGEXP;

  private static final String CLASS_REGEXP = "\\["
    + "(?:"
    + "[^\\]\\\\&&" + NON_TERMINATOR_REGEXP + "]"
    + "|" + BACKSLASH_SEQUENCE_REGEXP
    + ")*+"
    + "\\]";

  private static final String REGULAR_EXPRESSION_REGEXP = ""
    // A slash starts a regexp but only if not a comment start
    + "\\/(?![*/])"
    + "(?:"
    + "[^\\\\\\[/&&" + NON_TERMINATOR_REGEXP + "]"
    + "|" + CLASS_REGEXP
    + "|" + BACKSLASH_SEQUENCE_REGEXP
    + ")*+"
    // finished by a '/'
    + "\\/"
    + "\\p{javaJavaIdentifierPart}*+";

  public static LexerlessGrammar createGrammar() {
    return createGrammarBuilder().build();
  }

  public static LexerlessGrammarBuilder createGrammarBuilder() {
    LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

    b.rule(IDENTIFIER).is(
      SPACING,
      b.regexp("([_a-zA-Z][_a-zA-Z0-9]*)"));

    b.rule(LITERAL).is(b.firstOf(
      NULL_LITERAL,
      BOOLEAN_LITERAL,
      NUMERIC_LITERAL,
      STRING_LITERAL,
      REGULAR_EXPRESSION_LITERAL));
    b.rule(NULL_LITERAL).is(NULL);
    b.rule(BOOLEAN_LITERAL).is(b.firstOf(
      TRUE,
      FALSE));
    b.rule(PROPERTY_SET_PARAMETER_LIST).is(VARIABLE);

    lexical(b);
    keywords(b);
    punctuators(b);

    genericTags(b);
    cfmlTags(b);
    declarations(b);
    b.setRootRule(ROOT);
    return b;
  }

  private static void genericTags(LexerlessGrammarBuilder b) {
    b.rule(GENERIC_START_TAG).is(b.regexp("<[^>]*>"));
    b.rule(GENERIC_END_TAG).is(b.regexp("<\\/{1}[^>]*>"));
    b.rule(GENERIC_SINGLE_TAG).is(b.regexp("<[^>]*/>"));
    b.rule(GENERIC_ALL_TAG).is(b.regexp("<[^>]*>[\\w\\s\\n]*<\\/{1}[^>]*>"));
    b.rule(CF_SINGLE_BEGIN_TAG).is(b.optional(SPACING), b.regexp("<[\\W]*cf[\\S]*"));
    b.rule(CF_SINGLE_END_TAG).is(b.regexp("[\\s]*/>"), b.optional(SPACING));
    b.rule(CF_END_TAG).is(b.regexp("[\\s]*>"), b.optional(SPACING));
  }

  private static void cfmlTags(LexerlessGrammarBuilder b) {
    b.rule(CFML_STATEMENT).is(b.firstOf(
      b.sequence(CFSCRIPTSTART, SCRIPT_BLOCK, CFSCRIPTEND), CFML_SINGLE_LINE_TAG),
      b.zeroOrMore(CFML_STATEMENT));
    b.rule(CFML_SINGLE_LINE_TAG).is(CF_SINGLE_BEGIN_TAG, b.zeroOrMore(FUNCTION_ATTRIBUTE),
      b.firstOf(CF_SINGLE_END_TAG, CF_END_TAG, GENERIC_END_TAG));
    b.rule(CFML_MULTI_LINE_TAG).is(CF_SINGLE_BEGIN_TAG, b.zeroOrMore(COMPONENT_ATTRIBUTE), CF_END_TAG, b.zeroOrMore(CFML_STATEMENT), GENERIC_END_TAG);
  }

  private static void declarations(LexerlessGrammarBuilder b) {
    // decide if CMFL tag
    b.rule(ROOT).is(b.firstOf(
      CFML_STATEMENT,
      SCRIPT_BLOCK));
    b.rule(VARIABLE).is(b.firstOf(
      b.sequence(VAR, IDENTIFIERS),
      b.sequence(LOCAL, DOT, IDENTIFIERS),
      b.sequence(THIS, DOT, IDENTIFIERS),
      b.sequence(VARIABLES, DOT, IDENTIFIERS),
      IDENTIFIERS));
    b.rule(SCRIPT_BLOCK).is(b.firstOf(COMPONENT_DECLARATION, b.sequence(b.oneOrMore(ELEMENT), END_OF_SCRIPT_BLOCK)));
    b.rule(COMPONENT_DECLARATION).is(COMPONENT, b.zeroOrMore(COMPONENT_ATTRIBUTE), COMPONENT_BLOCK);
    b.rule(END_OF_SCRIPT_BLOCK).is(b.optional(SPACING), b.firstOf(EOS, EOS_NO_LB, EOF));
    b.rule(ELEMENT).is(b.firstOf(FUNCTION_DECLARATION, STATEMENT));
    b.rule(FUNCTION_DECLARATION).is(b.sequence(
      b.firstOf(
        b.sequence(b.optional(FUNCTION_ACCESS_TYPE), b.optional(FUNCTION_RETURN_TYPE), FUNCTION),
        b.sequence(b.optional(FUNCTION_RETURN_TYPE), FUNCTION),
        FUNCTION),
      VARIABLE,
      b.zeroOrMore(FUNCTION_ATTRIBUTE),
      b.sequence(LPARENTHESIS, b.optional(PARAMETER_LIST), RPARENTHESIS),
      COMPOUND_STATEMENT));
    b.rule(FUNCTION_ACCESS_TYPE).is(b.optional(ACCESS_TYPE));
    b.rule(FUNCTION_RETURN_TYPE).is(b.optional(TYPE_SPEC));
    b.rule(ACCESS_TYPE).is(b.firstOf(PACKAGE, PRIVATE, PUBLIC, REMOTE));
    b.rule(TYPE_SPEC).is(b.firstOf(b.sequence(VARIABLE, b.zeroOrMore(b.sequence(DOT, b.firstOf(RESERVED_WORD, VARIABLE)))), LITERAL));
    b.rule(PARAMETER_LIST).is(PARAMETER, b.zeroOrMore(COMMA, PARAMETER));
    b.rule(PARAMETER).is(b.zeroOrMore(
      b.firstOf(
        b.sequence(REQUIRED, PARAMETER_TYPE, VARIABLE),
        b.sequence(PARAMETER_TYPE, VARIABLE),
        VARIABLE),
      b.optional(b.sequence(EQUALSOP, IMPLIES_EXPRESSION))));
    b.rule(PARAMETER_TYPE).is(TYPE_SPEC);
    b.rule(COMPONENT_ATTRIBUTE).is(VARIABLE, b.optional(COLON, VARIABLE), EQUALSOP, b.optional(IMPLIES_EXPRESSION));
    b.rule(FUNCTION_ATTRIBUTE).is(b.nextNot(LPARENTHESIS), IMPLIES_EXPRESSION);
    b.rule(COMPOUND_STATEMENT).is(LCURLYBRACE, b.zeroOrMore(STATEMENT), RCURLYBRACE, b.optional(SPACING));
    b.rule(COMPONENT_BLOCK).is(LCURLYBRACE, b.zeroOrMore(ELEMENT), RCURLYBRACE);
    b.rule(STATEMENT).is(
      b.firstOf(
        TRY_CATCH_STATEMENT,
        IF_STATEMENT,
        WHILE_STATEMENT,
        DO_WHILE_STATEMENT,
        FOR_STATEMENT,
        SWITCH_STATEMENT,
        b.sequence(CONTINUE, SEMICOLON),
        b.sequence(BREAK, SEMICOLON),
        RETURN_STATEMENT,
        TAG_OPERATOR_STATEMENT,
        COMPOUND_STATEMENT,
        b.sequence(LOCAL_ASSIGNMENT_EXPRESSION, SEMICOLON),
        SEMICOLON));
    b.rule(CONDITION).is(LPARENTHESIS, b.zeroOrMore(LOCAL_ASSIGNMENT_EXPRESSION), RPARENTHESIS);
    b.rule(RETURN_STATEMENT).is(b.firstOf(b.sequence(RETURN, SEMICOLON), b.sequence(RETURN, ASSIGNMENT_EXPRESSION, SEMICOLON)));
    b.rule(IF_STATEMENT).is(IF, CONDITION, STATEMENT, b.optional(b.sequence(ELSE, STATEMENT)));
    b.rule(WHILE_STATEMENT).is(WHILE, CONDITION, STATEMENT);
    b.rule(DO_WHILE_STATEMENT).is(DO, STATEMENT, WHILE, CONDITION, SEMICOLON, b.optional(SPACING));
    b.rule(FOR_STATEMENT).is(b.firstOf(
      b.sequence(
        FOR,
        LPARENTHESIS,
        FOR_IN_KEY,
        IN,
        ASSIGNMENT_EXPRESSION,
        RPARENTHESIS,
        STATEMENT),
      b.sequence(
        FOR,
        LPARENTHESIS,
        VARIABLE,
        ASSIGNMENT_EXPRESSION,
        SEMICOLON,
        ASSIGNMENT_EXPRESSION,
        SEMICOLON,
        ASSIGNMENT_EXPRESSION,
        RPARENTHESIS,
        STATEMENT)));
    b.rule(FOR_IN_KEY).is(VARIABLE, b.optional(b.sequence(DOT, b.firstOf(RESERVED_WORD, VARIABLE))));
    b.rule(TRY_CATCH_STATEMENT).is(TRY, STATEMENT, b.zeroOrMore(CATCH_CONDITION), b.optional(FINALLY_STATEMENT));
    b.rule(CATCH_CONDITION).is(b.firstOf(
      b.sequence(CATCH, LPARENTHESIS, EXCEPTION_TYPE, RPARENTHESIS, COMPOUND_STATEMENT),
      b.sequence(CATCH, LPARENTHESIS, EXCEPTION_TYPE, VARIABLE, RPARENTHESIS, COMPOUND_STATEMENT)));
    b.rule(FINALLY_STATEMENT).is(FINALLY, COMPOUND_STATEMENT);
    b.rule(EXCEPTION_TYPE).is(b.firstOf(
      b.sequence(VARIABLE, b.zeroOrMore(DOT, b.firstOf(RESERVED_WORD, VARIABLE))),
      STRING_LITERAL));
    b.rule(CONSTANT_EXPRESSION).is(b.optional(SPACING),
      b.firstOf(
        b.sequence(LPARENTHESIS, CONSTANT_EXPRESSION, RPARENTHESIS),
        b.sequence(MINUS, LITERAL),
        LITERAL,
        NULL));
    b.rule(SWITCH_STATEMENT).is(
      SWITCH,
      CONDITION,
      LCURLYBRACE,
      b.zeroOrMore(CASE_STATEMENT),
      RCURLYBRACE,
      b.optional(SPACING));
    b.rule(CASE_STATEMENT).is(b.optional(SPACING),
      b.firstOf(b.sequence(CASE, CONSTANT_EXPRESSION, COLON, b.zeroOrMore(STATEMENT)),
        b.sequence(DEFAULT, COLON)), b.zeroOrMore(STATEMENT));
    b.rule(TAG_OPERATOR_STATEMENT).is(
      b.firstOf(
        INCLUDE_STATEMENT,
        b.sequence(IMPORT, COMPONENT_PATH, SEMICOLON),
        ABORT_STATEMENT,
        THROW_STATEMENT,
        b.sequence(RETHROW, SEMICOLON),
        EXIT_STATEMENT,
        PARAM_STATEMENT,
        PROPERTY_STATEMENT,
        LOCK_STATEMENT,
        THREAD_STATEMENT,
        TRANSACTION_STATEMENT,
        CFML_FUNCTION_STATEMENT));
    b.rule(INCLUDE_STATEMENT).is(INCLUDE, b.zeroOrMore(IMPLIES_EXPRESSION), SEMICOLON);
    b.rule(TRANSACTION_STATEMENT).is(TRANSACTION, PARAM_STATEMENT_ATTRIBUTES, b.optional(COMPOUND_STATEMENT));
    b.rule(CFML_FUNCTION_STATEMENT).is(CFML_FUNCTION, b.zeroOrMore(PA), COMPOUND_STATEMENT);
    b.rule(CFML_FUNCTION).is(b.firstOf(LOCATION, SAVECONTENT, HTTP, FILE, DIRECTORY, LOOP, SETTING, QUERY));
    b.rule(LOCK_STATEMENT).is(LOCK, PARAM_STATEMENT_ATTRIBUTES, COMPOUND_STATEMENT);
    b.rule(THREAD_STATEMENT).is(THREAD, PARAM_STATEMENT_ATTRIBUTES, b.optional(COMPOUND_STATEMENT));
    b.rule(ABORT_STATEMENT).is(ABORT, b.optional(MEMBER_EXPRESSION), SEMICOLON);
    b.rule(THROW_STATEMENT).is(THROW, b.optional(MEMBER_EXPRESSION), SEMICOLON);
    b.rule(EXIT_STATEMENT).is(EXIT, b.optional(MEMBER_EXPRESSION), SEMICOLON);
    b.rule(PARAM_STATEMENT).is(PARAM, PARAM_STATEMENT_ATTRIBUTES);
    b.rule(PROPERTY_STATEMENT).is(PROPERTY, PARAM_STATEMENT_ATTRIBUTES);
    b.rule(PARAM_STATEMENT_ATTRIBUTES).is(b.oneOrMore(PA));
    b.rule(PA).is(VARIABLE, EQUALSOP, IMPLIES_EXPRESSION);
    b.rule(EXPRESSION).is(LOCAL_ASSIGNMENT_EXPRESSION);
    b.rule(LOCAL_ASSIGNMENT_EXPRESSION).is(b.firstOf(
      ASSIGNMENT_EXPRESSION,
      b.sequence(VARIABLE, b.optional(b.sequence(EQUALSOP, IMPLIES_EXPRESSION)))));
    b.rule(ASSIGNMENT_EXPRESSION).is(
      IMPLIES_EXPRESSION,
      b.optional(
        b.firstOf(EQUALSOP, PLUSEQUALS, MINUSEQUALS, STAREQUALS, SLASHEQUALS, MODEQUALS, CONCATEQUALS),
        IMPLIES_EXPRESSION));
    b.rule(IMPLIES_EXPRESSION).is(b.firstOf(
      TERNARY,
      ELVIS_EXPRESSION,
      b.sequence(EQUIVALENT_EXPRESSION, b.zeroOrMore(b.sequence(IMP, EQUIVALENT_EXPRESSION)))));
    b.rule(TERNARY).is(b.sequence(
      EQUIVALENT_EXPRESSION,
      QUESTIONMARK,
      LOCAL_ASSIGNMENT_EXPRESSION,
      COLON,
      LOCAL_ASSIGNMENT_EXPRESSION));
    b.rule(ELVIS_EXPRESSION).is(EQUIVALENT_EXPRESSION, ELVIS, EQUIVALENT_EXPRESSION);
    b.rule(EQUIVALENT_EXPRESSION).is(XOR_EXPRESSION, b.zeroOrMore(b.firstOf(EQV, XOR_EXPRESSION)));
    b.rule(XOR_EXPRESSION).is(OR_EXPRESSION, b.zeroOrMore(b.firstOf(XOR, OR_EXPRESSION)));
    b.rule(OR_EXPRESSION).is(AND_EXPRESSION, b.zeroOrMore(b.firstOf(OR, OROPERATOR, AND_EXPRESSION)));
    b.rule(AND_EXPRESSION).is(NOT_EXPRESSION, b.zeroOrMore(b.firstOf(AND, ANDOPERATOR, NOT_EXPRESSION)));
    b.rule(NOT_EXPRESSION).is(b.zeroOrMore(b.firstOf(NOT, NOTOP)), EQUALITY_EXPRESSION);
    b.rule(EQUALITY_EXPRESSION).is(CONCATENATION_EXPRESSION,
      b.zeroOrMore(
        b.firstOf(
          EQUALITY_OPERATOR_5,
          EQUALITY_OPERATOR_3,
          EQUALITY_OPERATOR_2,
          EQUALITY_OPERATOR_1),
        CONCATENATION_EXPRESSION));
    b.rule(EQUALITY_OPERATOR_1).is(
      b.firstOf(IS, EQUALSEQUALSOP, LT, LCHEVRON, LTE, LCHEVRONEQUAL, LE, GT, RCHEVRON, GTE, RCHEVRONEQUAL, GE, EQ, NEQ, NOTEQUAL, EQUAL, EQUALS, CONTAINS));
    b.rule(EQUALITY_OPERATOR_2).is(b.firstOf(b.sequence(LESS, THAN), b.sequence(GREATER, THAN), b.sequence(NOT, EQUAL), b.sequence(IS, NOT)));
    b.rule(EQUALITY_OPERATOR_3).is(DOES, NOT, CONTAIN);
    b.rule(EQUALITY_OPERATOR_5).is(b.firstOf(b.sequence(LESS, THAN, OR, EQUAL, TO), b.sequence(GREATER, THAN, OR, EQUAL, TO)));
    b.rule(CONCATENATION_EXPRESSION).is(ADDITIVE_EXPRESSION, b.zeroOrMore(b.sequence(CONCAT, ADDITIVE_EXPRESSION)));
    b.rule(ADDITIVE_EXPRESSION).is(MOD_EXPRESSION, b.zeroOrMore(b.firstOf(PLUS, MINUS), MOD_EXPRESSION));
    b.rule(MOD_EXPRESSION).is(INT_DIVISION_EXPRESSION, b.zeroOrMore(b.firstOf(MOD, MODOPERATOR), INT_DIVISION_EXPRESSION));
    b.rule(INT_DIVISION_EXPRESSION).is(MULTIPLICATIVE_EXPRESSION, b.zeroOrMore(BSLASH, MULTIPLICATIVE_EXPRESSION));
    b.rule(MULTIPLICATIVE_EXPRESSION).is(POWER_OF_EXPRESSION, b.zeroOrMore(b.firstOf(STAR, SLASH), POWER_OF_EXPRESSION));
    b.rule(POWER_OF_EXPRESSION).is(UNARY_EXPRESSION, b.zeroOrMore(b.sequence(POWER, UNARY_EXPRESSION)));
    b.rule(UNARY_EXPRESSION).is(b.firstOf(
      b.sequence(MINUS, MEMBER_EXPRESSION),
      b.sequence(PLUS, MEMBER_EXPRESSION),
      b.sequence(MINUSMINUS, MEMBER_EXPRESSION),
      b.sequence(PLUSPLUS, MEMBER_EXPRESSION),
      b.sequence(NEW_COMPONENT_EXPRESSION, b.zeroOrMore(b.sequence(DOT, PRIMARY_EXPRESSION_IRW), b.zeroOrMore(b.sequence(LPARENTHESIS, ARGUMENT_LIST, RPARENTHESIS)))),
      b.sequence(MEMBER_EXPRESSION, MINUSMINUS),
      b.sequence(MEMBER_EXPRESSION, PLUSPLUS),
      MEMBER_EXPRESSION));
    b.rule(MEMBER_EXPRESSION).is(b.firstOf(b.sequence(HASH, MEMBER_EXPRESSION_B, HASH), MEMBER_EXPRESSION_B));
    b.rule(MEMBER_EXPRESSION_B).is(PRIMARY_EXPRESSION, b.zeroOrMore(b.firstOf(
      b.sequence(DOT, PRIMARY_EXPRESSION_IRW, LPARENTHESIS, ARGUMENT_LIST, RPARENTHESIS),
      b.sequence(LPARENTHESIS, ARGUMENT_LIST, RPARENTHESIS),
      b.sequence(LBRACKET, IMPLIES_EXPRESSION, RBRACKET),
      b.sequence(DOT, PRIMARY_EXPRESSION_IRW))));
    b.rule(MEMBER_EXPRESSION_SUFFIX).is(b.firstOf(INDEX_SUFFIX, PROPERTY_REFERENCE_SUFFIX));
    b.rule(PROPERTY_REFERENCE_SUFFIX).is(DOT, b.zeroOrMore(LT), VARIABLE);
    b.rule(INDEX_SUFFIX).is(LBRACKET, b.zeroOrMore(LT), PRIMARY_EXPRESSION, b.zeroOrMore(LT), RBRACKET);
    b.rule(PRIMARY_EXPRESSION_IRW).is(b.firstOf(RESERVED_WORD, PRIMARY_EXPRESSION));
    b.rule(RESERVED_WORD).is(b.firstOf(CONTAINS, IS, EQUAL, EQ, NEQ, GT, LT, GTE, GE, LTE, LE, NOT, AND, OR, XOR, EQV, IMP, MOD, NULL, EQUALS, CF_SCRIPT_KEYWORDS));
    b.rule(ARGUMENT_LIST).is(b.optional(ARGUMENT, b.zeroOrMore(COMMA, ARGUMENT)));
    b.rule(ARGUMENT).is(b.firstOf(b.sequence(VARIABLE, COLON, IMPLIES_EXPRESSION),
      b.sequence(VARIABLE, EQUALSOP, IMPLIES_EXPRESSION),
      IMPLIES_EXPRESSION));
    b.rule(IDENTIFIERS).is(b.optional(SPACING), b.firstOf(
      IDENTIFIER,
      DOES,
      CONTAIN,
      GREATER,
      THAN,
      LESS,
      VAR,
      TO,
      DEFAULT,
      INCLUDE,
      NEW,
      ABORT,
      THROW,
      RETHROW,
      PARAM,
      EXIT,
      THREAD,
      LOCK,
      TRANSACTION,
      PUBLIC,
      PRIVATE,
      REMOTE,
      PACKAGE,
      REQUIRED,
      CFML_FUNCTION,
      CF_SCRIPT_KEYWORDS
      ), b.optional(SPACING));
    b.rule(CF_SCRIPT_KEYWORDS).is(b.optional(SPACING), b.firstOf(
      IF,
      ELSE,
      BREAK,
      CONTINUE,
      FUNCTION,
      RETURN,
      WHILE,
      DO,
      FOR,
      IN,
      TRY,
      CATCH,
      SWITCH,
      CASE,
      DEFAULT,
      IMPORT), b.optional(SPACING));
    b.rule(PRIMARY_EXPRESSION).is(SPACING, b.firstOf(
      IMPLICIT_ARRAY,
      IMPLICIT_STRUCT,
      NULL,
      b.sequence(LPARENTHESIS, b.zeroOrMore(LT), LOCAL_ASSIGNMENT_EXPRESSION, b.zeroOrMore(LT), RPARENTHESIS),
      b.sequence(VARIABLE, b.optional(b.sequence(EQUALSOP, IMPLIES_EXPRESSION))),
      VARIABLE,
      LITERAL));
    b.rule(IMPLICIT_ARRAY).is(LBRACKET, b.optional(IMPLICIT_ARRAY_ELEMENTS), RBRACKET);
    b.rule(IMPLICIT_ARRAY_ELEMENTS).is(IMPLIES_EXPRESSION, b.zeroOrMore(b.sequence(COMMA, IMPLIES_EXPRESSION)));
    b.rule(IMPLICIT_STRUCT).is(LCURLYBRACE, b.optional(IMPLICIT_STRUCT_ELEMENTS), RCURLYBRACE);
    b.rule(IMPLICIT_STRUCT_ELEMENTS).is(IMPLICIT_STRUCT_EXPRESSION, b.zeroOrMore(b.sequence(COMMA, IMPLICIT_STRUCT_EXPRESSION)));
    b.rule(IMPLICIT_STRUCT_EXPRESSION).is(IMPLICIT_STRUCT_KEY_EXPRESSION, b.firstOf(COLON, EQUALSOP), IMPLIES_EXPRESSION);
    b.rule(IMPLICIT_STRUCT_KEY_EXPRESSION).is(b.firstOf(
      b.sequence(VARIABLE,
        b.zeroOrMore(DOT, b.firstOf(RESERVED_WORD, VARIABLE))),
      LITERAL));
    b.rule(NEW_COMPONENT_EXPRESSION).is(NEW, COMPONENT_PATH, LPARENTHESIS, ARGUMENT_LIST, RPARENTHESIS);
    b.rule(COMPONENT_PATH).is(b.firstOf(STRING_LITERAL, b.sequence(VARIABLE, b.zeroOrMore(DOT, VARIABLE))));
  }

  private static void lexical(LexerlessGrammarBuilder b) {
    b.rule(SPACING).is(
      b.skippedTrivia(b.regexp("[" + LINE_TERMINATOR_REGEXP + WHITESPACE_REGEXP + "]*+")),
      b.zeroOrMore(
        b.commentTrivia(b.regexp(COMMENT_REGEXP)),
        b.skippedTrivia(b.regexp("[" + LINE_TERMINATOR_REGEXP + WHITESPACE_REGEXP + "]*+")))).skip();

    b.rule(SPACING_NO_LB).is(
      b.zeroOrMore(
        b.firstOf(
          b.skippedTrivia(b.regexp("[" + WHITESPACE_REGEXP + "]++")),
          b.commentTrivia(b.regexp("(?:" + MULTI_LINE_COMMENT_NO_LB_REGEXP + ")"))))).skip();
    b.rule(NEXT_NOT_LB).is(b.nextNot(b.regexp("(?:" + MULTI_LINE_COMMENT_REGEXP + "|[" + LINE_TERMINATOR_REGEXP + "])"))).skip();

    b.rule(LINE_TERMINATOR_SEQUENCE).is(b.skippedTrivia(b.regexp("(?:\\n|\\r\\n|\\r|\\u2028|\\u2029)"))).skip();

    b.rule(EOS).is(b.firstOf(
      b.sequence(SPACING, SEMICOLON),
      b.sequence(SPACING_NO_LB, LINE_TERMINATOR_SEQUENCE),
      b.sequence(SPACING, b.endOfInput())));

    b.rule(EOS_NO_LB).is(b.firstOf(
      b.sequence(SPACING_NO_LB, NEXT_NOT_LB, SEMICOLON),
      b.sequence(SPACING_NO_LB, LINE_TERMINATOR_SEQUENCE),
      b.sequence(SPACING_NO_LB, b.endOfInput())));

    b.rule(EOF).is(b.token(GenericTokenType.EOF, b.endOfInput())).skip();
    b.rule(NUMERIC_LITERAL).is(
      SPACING,
      b.regexp(NUMERIC_LITERAL_REGEXP));

    b.rule(STRING_LITERAL).is(b.token(GenericTokenType.LITERAL, b.regexp(LITERAL_REGEXP)));

    b.rule(REGULAR_EXPRESSION_LITERAL).is(
      SPACING,
      b.regexp(REGULAR_EXPRESSION_REGEXP));

    b.rule(EMPTY_STRING).is(b.oneOrMore(b.firstOf(SINGLEQUOTE, DOUBLEQUOTE)));
  }

  private static void keywords(LexerlessGrammarBuilder b) {
    b.rule(LETTER_OR_DIGIT).is(b.regexp("\\p{javaJavaIdentifierPart}"));
    Object[] rest = new Object[CFKeyword.values().length - 2];
    for (int i = 0; i < CFKeyword.values().length; i++) {
      CFKeyword tokenType = CFKeyword.values()[i];
      b.rule(tokenType).is(SPACING, tokenType.getValue(), b.nextNot(LETTER_OR_DIGIT));
      if (i > 1) {
        rest[i - 2] = tokenType.getValue();
      }
    }
    b.rule(KEYWORD).is(b.firstOf(
      CFKeyword.keywordValues()[0],
      CFKeyword.keywordValues()[1],
      rest), b.nextNot(LETTER_OR_DIGIT));
  }

  private static void punctuator(LexerlessGrammarBuilder b, GrammarRuleKey ruleKey, String value) {
    for (CFPunctuator tokenType : CFPunctuator.values()) {
      if (value.equals(tokenType.getValue())) {
        b.rule(tokenType).is(SPACING, value);
        return;
      }
    }
    throw new IllegalStateException(value);
  }

  private static void punctuator(LexerlessGrammarBuilder b, GrammarRuleKey ruleKey, String value, Object element) {
    for (CFPunctuator tokenType : CFPunctuator.values()) {
      if (value.equals(tokenType.getValue())) {
        b.rule(tokenType).is(b.optional(SPACING), value, element);
        return;
      }
    }
    throw new IllegalStateException(value);
  }

  private static void punctuators(LexerlessGrammarBuilder b) {
    punctuator(b, LCURLYBRACE, "{");
    punctuator(b, RCURLYBRACE, "}");
    punctuator(b, LPARENTHESIS, "(");
    punctuator(b, RPARENTHESIS, ")");
    punctuator(b, LBRACKET, "[");
    punctuator(b, RBRACKET, "]");
    punctuator(b, DOT, ".");
    punctuator(b, SEMICOLON, ";");
    punctuator(b, COMMA, ",");
    punctuator(b, LCHEVRON, "<", b.nextNot("="));
    punctuator(b, RCHEVRON, ">", b.nextNot("="));
    punctuator(b, RCHEVRONEQUAL, "<=");
    punctuator(b, LCHEVRONEQUAL, ">=");
    punctuator(b, EQUALSEQUALSOP, "==", b.nextNot("="));
    punctuator(b, NOTEQUAL, "!=", b.nextNot("="));
    punctuator(b, PLUS, "+", b.nextNot(b.firstOf("+", "=")));
    punctuator(b, MINUS, "-", b.nextNot(b.firstOf("-", "=")));
    punctuator(b, STAR, "*", b.nextNot("="));
    punctuator(b, MODOPERATOR, "%", b.nextNot("="));
    punctuator(b, SLASH, "/", b.nextNot("="));
    punctuator(b, PLUSPLUS, "++");
    punctuator(b, MINUSMINUS, "--");
    punctuator(b, CONCAT, "&", b.nextNot("&", "="));
    // punctuator(b, OR, "|", b.nextNot("="));
    punctuator(b, POWER, "^", b.nextNot("="));
    punctuator(b, NOTOP, "!", b.nextNot("="));
    punctuator(b, TILDA, "~");
    punctuator(b, ANDOPERATOR, "&&");
    punctuator(b, OROPERATOR, "||");
    punctuator(b, QUESTIONMARK, "?", b.nextNot(":"));
    punctuator(b, COLON, ":");
    punctuator(b, EQUALSOP, "=", b.nextNot("="));
    punctuator(b, PLUSEQUALS, "+=");
    punctuator(b, MINUSEQUALS, "-=");
    punctuator(b, SLASHEQUALS, "/=");
    punctuator(b, STAREQUALS, "*=");
    punctuator(b, MODEQUALS, "%=");
    punctuator(b, CONCATEQUALS, "&=");
    punctuator(b, ELVIS, "?:");
    punctuator(b, HASH, "#");
    punctuator(b, DOUBLEQUOTE, "\"");
    punctuator(b, SINGLEQUOTE, "\'");
    punctuator(b, BSLASH, "\\");
  }
}
