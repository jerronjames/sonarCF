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
package org.sonar.plugins.coldfusion.lcov;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.CoverageMeasuresBuilder;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PropertiesBuilder;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.plugins.coldfusion.ColdFusionPlugin;
import org.sonar.plugins.coldfusion.core.ColdFusion;

import java.io.File;
import java.util.Map;

public class LCOVSensor implements Sensor {

  private static final Logger LOG = LoggerFactory.getLogger(LCOVSensor.class);

  private final ColdFusion coldfusion;

  public LCOVSensor(ColdFusion coldfusion) {
    this.coldfusion = coldfusion;
  }

  public boolean shouldExecuteOnProject(Project project) {
    return ColdFusion.isEnabled(project)
      && StringUtils.isNotBlank(coldfusion.getSettings().getString(ColdFusionPlugin.LCOV_REPORT_PATH));
  }

  public void analyse(Project project, SensorContext context) {
    File lcovFile = project.getFileSystem().resolvePath(coldfusion.getSettings().getString(ColdFusionPlugin.LCOV_REPORT_PATH));
    if (lcovFile.isFile()) {
      LCOVParser parser = new LCOVParser(project.getFileSystem());
      LOG.info("Analysing {}", lcovFile);
      Map<String, CoverageMeasuresBuilder> coveredFiles = parser.parseFile(lcovFile);
      analyseCoveredFiles(project, context, coveredFiles);
    }
  }

  protected void analyseCoveredFiles(Project project, SensorContext context, Map<String, CoverageMeasuresBuilder> coveredFiles) {
    for (InputFile inputFile : project.getFileSystem().mainFiles(ColdFusion.KEY)) {
      try {
        CoverageMeasuresBuilder fileCoverage = coveredFiles.get(inputFile.getFile().getAbsolutePath());
        org.sonar.api.resources.File resource = org.sonar.api.resources.File.fromIOFile(inputFile.getFile(), project);
        PropertiesBuilder<Integer, Integer> lineHitsData = new PropertiesBuilder<Integer, Integer>(CoreMetrics.COVERAGE_LINE_HITS_DATA);

        if (fileCoverage != null) {
          for (Measure measure : fileCoverage.createMeasures()) {
            context.saveMeasure(resource, measure);
          }
        } else {

          // colour all lines as not executed
          for (int x = 1; x < context.getMeasure(resource, CoreMetrics.LINES).getIntValue(); x++) {
            lineHitsData.add(x, 0);
          }

          // use non comment lines of code for coverage calculation
          Measure ncloc = context.getMeasure(resource, CoreMetrics.NCLOC);
          context.saveMeasure(resource, lineHitsData.build());
          context.saveMeasure(resource, CoreMetrics.LINES_TO_COVER, ncloc.getValue());
          context.saveMeasure(resource, CoreMetrics.UNCOVERED_LINES, ncloc.getValue());
        }

      } catch (Exception e) {
        LOG.error("Problem while calculating coverage for " + inputFile.getFile().getAbsolutePath(), e);
      }
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
