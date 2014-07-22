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

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.api.resources.Project;

public class ColdFusion extends AbstractLanguage {

  public static final String KEY = "coldfusion";

  private final Settings settings;

  public ColdFusion(Settings configuration) {
    super(KEY, "ColdFusion");
    this.settings = configuration;
  }

  public Settings getSettings() {
    return this.settings;
  }

  @Override
  public String[] getFileSuffixes() {
    String[] suffixes = settings.getStringArray(ColdFusionPlugin.FILE_SUFFIXES_KEY);
    if (suffixes == null || suffixes.length == 0) {
      suffixes = StringUtils.split(ColdFusionPlugin.FILE_SUFFIXES_DEFVALUE, ",");
    }
    return suffixes;
  }

  public static boolean isEnabled(Project project) {
    return !project.getFileSystem().mainFiles(KEY).isEmpty();
  }

}
