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

import com.google.common.collect.ImmutableList;
import org.sonar.api.Extension;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.coldfusion.colorizer.ColdFusionColorizerFormat;
import org.sonar.plugins.coldfusion.core.ColdFusion;
import org.sonar.plugins.coldfusion.core.ColdFusionSourceImporter;
import org.sonar.plugins.coldfusion.cpd.ColdFusionCpdMapping;
import org.sonar.plugins.coldfusion.lcov.LCOVSensor;

import java.util.List;

@Properties({
  // Global ColdFusion settings
  @Property(
    key = ColdFusionPlugin.FILE_SUFFIXES_KEY,
    defaultValue = ColdFusionPlugin.FILE_SUFFIXES_DEFVALUE,
    name = "File suffixes",
    description = "Comma-separated list of suffixes for files to analyze.",
    global = true,
    project = true),
  @Property(
    key = ColdFusionPlugin.LCOV_REPORT_PATH,
    defaultValue = ColdFusionPlugin.LCOV_REPORT_PATH_DEFAULT_VALUE,
    name = "LCOV file",
    description = "Path (absolute or relative) to the file with LCOV data.",
    global = true,
    project = true)
})
public class ColdFusionPlugin extends SonarPlugin {

  // Global ColdFusion constants
  public static final String FALSE = "false";

  public static final String FILE_SUFFIXES_KEY = "sonar.coldfusion.file.suffixes";
  public static final String FILE_SUFFIXES_DEFVALUE = ".cfc";

  public static final String PROPERTY_PREFIX = "sonar.coldfusion";

  public static final String LCOV_REPORT_PATH = PROPERTY_PREFIX + ".lcov.reportPath";
  public static final String LCOV_REPORT_PATH_DEFAULT_VALUE = "";

  public List getExtensions() {
    return ImmutableList.of(
        ColdFusion.class,
        ColdFusionSourceImporter.class,
        ColdFusionColorizerFormat.class,
        ColdFusionCpdMapping.class,

        ColdFusionSquidSensor.class,
        ColdFusionRuleRepository.class,
        ColdFusionProfile.class,

        ColdFusionCommonRulesEngine.class,
        ColdFusionCommonRulesDecorator.class,

        LCOVSensor.class);
  }
}
