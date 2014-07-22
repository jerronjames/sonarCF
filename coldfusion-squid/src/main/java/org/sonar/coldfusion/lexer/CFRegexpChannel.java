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


/**
 * Provides a heuristic to guess whether a forward slash starts a regular expression.
 * http://stackoverflow.com/questions/7936593/finding-regular-expression-literals-in-a-string-of-coldfusion-code
 */
public class CFRegexpChannel {

  private static final String NON_TERMINATOR = "[^\\r\\n\\u2028\\u2029]";

  private static final String BACKSLASH_SEQUENCE = "\\\\" + NON_TERMINATOR;

  private static final String CLASS = "\\["
    + "(?:"
    + "[^\\]\\\\&&" + NON_TERMINATOR + "]"
    + "|" + BACKSLASH_SEQUENCE
    + ")*+"
    + "\\]";

  public static final String REGULAR_EXPRESSION = ""
    // A slash starts a regexp but only if not a comment start
    + "\\/(?![*/])"
    + "(?:"
    + "[^\\\\\\[/&&" + NON_TERMINATOR + "]"
    + "|" + CLASS
    + "|" + BACKSLASH_SEQUENCE
    + ")*+"
    // finished by a '/'
    + "\\/"
    + "\\p{javaJavaIdentifierPart}*+";
  /*
   * ColdFusion Specific RegExp
   */
  // public static final String CF_OPTIONS =

  /*
   * End ColdFusion Specific RegExp
   */

}
