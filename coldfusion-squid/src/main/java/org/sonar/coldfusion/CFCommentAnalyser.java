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

import org.sonar.squidbridge.CommentAnalyser;

public class CFCommentAnalyser extends CommentAnalyser {

  @Override
  public boolean isBlank(String line) {
    for (int i = 0; i < line.length(); i++) {
      if (Character.isLetterOrDigit(line.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String getContents(String comment) {
    if (comment.startsWith("//")) {
      return comment.substring(2);
    } else if (comment.startsWith("/*")) {
      return comment.substring(2, comment.length() - 2);
    } else if (comment.startsWith("<!--")) {
      if (comment.endsWith("-->")) {
        return comment.substring(4, comment.length() - 3);
      }
      return comment.substring(4);
    } else {
      throw new IllegalArgumentException();
    }
  }

}
