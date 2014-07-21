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
package org.sonar.plugins.coldfusion;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.plugins.coldfusion.core.ColdFusion;

import static org.fest.assertions.Assertions.assertThat;

public class ColdFusionTest {

  private Settings settings;
  private ColdFusion javaScript;

  @Before
  public void setUp() {
    settings = new Settings();
    javaScript = new ColdFusion(settings);
  }

  @Test
  public void defaultSuffixes() {
    settings.setProperty(ColdFusionPlugin.FILE_SUFFIXES_KEY, "");
    assertThat(javaScript.getFileSuffixes()).containsOnly(".js");
    assertThat(javaScript.getSettings()).isSameAs(settings);
  }

  @Test
  public void customSuffixes() {
    settings.setProperty(ColdFusionPlugin.FILE_SUFFIXES_KEY, "coldfusion");
    assertThat(javaScript.getFileSuffixes()).containsOnly("coldfusion");
  }

}
