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

import java.io.File;
import java.io.IOException;
import org.sonar.coldfusion.parser.CFGrammar;
import org.sonar.coldfusion.CFConfiguration;
import com.sonar.sslr.impl.Parser;
import org.sonar.coldfusion.lexer.CFLexer;
import org.sonar.sslr.parser.LexerlessGrammar;
//import com.sonar.sslr.api.Grammar;
import org.sonar.sslr.parser.ParserAdapter;


public final class CFParser {

  private CFParser() {
  }

  public static Parser<LexerlessGrammar> create(CFConfiguration conf) {
  	return new ParserAdapter<LexerlessGrammar>(conf.getCharset(), CFGrammar.createGrammar());
  }

}
