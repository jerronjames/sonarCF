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
package org.sonar.coldfusion.lexer;

import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.PunctuatorChannel;
import com.sonar.sslr.impl.channel.UnknownCharacterChannel;
import org.sonar.coldfusion.CFConfiguration;
import org.sonar.coldfusion.api.CFKeyword;
import org.sonar.coldfusion.api.CFPunctuator;
import org.sonar.coldfusion.api.CFTokenType;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.commentRegexp;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;

public final class CFLexer {

  private static final String EXP = "([Ee][+-]?+[0-9_]++)";
  private static final String BINARY_EXP = "([Pp][+-]?+[0-9_]++)";

  private static final String FLOAT_SUFFIX = "[fFdD]";
  private static final String INT_SUFFIX = "[lL]";

  public static final String NUMERIC_LITERAL = "(?:"
      // Decimal
      + "[0-9]++\\.([0-9]++)?+" + EXP + "?+" + FLOAT_SUFFIX + "?+"
      // Decimal
      + "|\\.[0-9]++" + EXP + "?+" + FLOAT_SUFFIX + "?+"
      // Decimal
      + "|[0-9]++" + FLOAT_SUFFIX
      + "|[0-9]++" + EXP + FLOAT_SUFFIX + "?+"
      // Hexadecimal
      + "|0[xX][0-9a-fA-F]++\\.[0-9a-fA-F_]*+" + BINARY_EXP + "?+" + FLOAT_SUFFIX + "?+"
      // Hexadecimal
      + "|0[xX][0-9a-fA-F]++" + BINARY_EXP + FLOAT_SUFFIX + "?+"

      // Integer Literals
      // Hexadecimal
      + "|0[xX][0-9a-fA-F]++" + INT_SUFFIX + "?+"
      // Binary (Java 7)
      + "|0[bB][01]++" + INT_SUFFIX + "?+"
      // Decimal and Octal
      + "|[0-9]++" + INT_SUFFIX + "?+"
      + ")";

  public static final String LITERAL = "[\\s]*(?:"
      + "\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""
      + "|\'([^\'\\\\]*+(\\\\[\\s\\S])?+)*+\'"
      + ")";

  public static final String CF_SINGLE_LINE_COMMENT = "//[^\\n\\r]*+|<!--[^\\n\\r]*+";
  public static final String CF_MULTI_LINE_COMMENT = "/\\*[\\s\\S]*?\\*/";
  public static final String MULTI_LINE_COMMENT = "<!--[\\s\\S]*?-->";
  public static final String MULTI_LINE_COMMENT_NO_LB = "<!--[^\\n\\r]*?-->";

  public static final String COMMENT = "(?:" + MULTI_LINE_COMMENT + "|" + CF_MULTI_LINE_COMMENT + "|" + CF_SINGLE_LINE_COMMENT + ")";

  private static final String HEX_DIGIT = "[0-9a-fA-F]";
  private static final String UNICODE_ESCAPE_SEQUENCE = "u" + HEX_DIGIT + HEX_DIGIT + HEX_DIGIT + HEX_DIGIT;

  private static final String UNICODE_LETTER = "\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Nl}";
  private static final String UNICODE_COMBINING_MARK = "\\p{Mn}\\p{Mc}";
  private static final String UNICODE_DIGIT = "\\p{Nd}";
  private static final String UNICODE_CONNECTOR_PUNCTUATION = "\\p{Pc}";

  private static final String IDENTIFIER_START = "(?:[$_" + UNICODE_LETTER + "]|\\\\" + UNICODE_ESCAPE_SEQUENCE + ")";
  private static final String IDENTIFIER_PART = "(?:" + IDENTIFIER_START + "|[" + UNICODE_COMBINING_MARK + UNICODE_DIGIT + UNICODE_CONNECTOR_PUNCTUATION + "])";

  public static final String IDENTIFIER = "([_a-zA-Z][_a-zA-Z0-9]*)";//IDENTIFIER_START + IDENTIFIER_PART + "*+";

  /**
   * LF, CR, LS, PS
   */
  public static final String LINE_TERMINATOR = "\\n\\r\\u2028\\u2029";

  /**
   * Tab, Vertical Tab, Form Feed, Space, No-break space, Byte Order Mark, Any other Unicode "space separator"
   */
  public static final String WHITESPACE = "\\t\\u000B\\f\\u0020\\u00A0\\uFEFF\\p{Zs}";



  // start generic cf tags <cftag analyze = false />
  public static final String GENERIC_START_TAG = "<[^>]*>";
  public static final String GENERIC_END_TAG = "<\\/{1}[^>]*>";
  public static final String GENERIC_SINGLE_TAG = "<[^>]*/>";
  public static final String GENERIC_ALL_TAG = "<[^>]*>[\\w\\s\\n]*<\\/{1}[^>]*>";
  public static final String CF_SINGLE_BEGIN_TAG = "<[\\W]*cf[\\S]*";
  public static final String CF_SINGLE_END_TAG = "[\\s]*/>";
  public static final String CF_END_TAG = "[\\s]*>";

  private CFLexer() {
  }

  public static Lexer create(CFConfiguration conf) {
    return Lexer.builder()
        .withCharset(conf.getCharset())

        .withFailIfNoChannelToConsumeOneCharacter(true)

        // Channels, which consumes more frequently should come first.
        // Whitespace character occurs more frequently than any other, and thus come first:
        .withChannel(new BlackHoleChannel("[" + LINE_TERMINATOR + WHITESPACE + "]++"))

        // Comments
        .withChannel(commentRegexp(COMMENT))

        // String Literals
        .withChannel(regexp(GenericTokenType.LITERAL, LITERAL))

        // Regular Expression Literals
        .withChannel(new CFRegexpChannel())

        .withChannel(regexp(CFTokenType.NUMERIC_LITERAL, NUMERIC_LITERAL))

        .withChannel(new IdentifierAndKeywordChannel(IDENTIFIER, true, CFKeyword.values()))
        .withChannel(new PunctuatorChannel(CFPunctuator.values()))

        .withChannel(new UnknownCharacterChannel(true))

        .build();
  }
}
