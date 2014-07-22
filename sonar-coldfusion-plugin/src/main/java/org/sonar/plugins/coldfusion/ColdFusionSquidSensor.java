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

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.checks.AnnotationCheckFactory;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.coldfusion.checks.CheckList;
import org.sonar.plugins.coldfusion.core.ColdFusion;

public class ColdFusionSquidSensor implements Sensor {

  private final AnnotationCheckFactory annotationCheckFactory;

  public ColdFusionSquidSensor(RulesProfile profile, FileLinesContextFactory fileLinesContextFactory) {
    this.annotationCheckFactory = AnnotationCheckFactory.create(profile, CheckList.REPOSITORY_KEY, CheckList.getChecks());
  }

  @Override
  public boolean shouldExecuteOnProject(Project project) {
    return ColdFusion.isEnabled(project);
  }

  @Override
  public void analyse(Project project, SensorContext context) {
    // this.project = project;
    // this.context = context;

    // Collection<SquidAstVisitor<LexerlessGrammar>> squidChecks = annotationCheckFactory.getChecks();
    // List<SquidAstVisitor<LexerlessGrammar>> visitors = Lists.newArrayList(squidChecks);
    // visitors.add(new FileLinesVisitor(project, fileLinesContextFactory));
    // scanner = ColdFusionAstScanner.create(createConfiguration(project), visitors.toArray(new SquidAstVisitor[visitors.size()]));
    // scanner.scanFiles(InputFileUtils.toFiles(project.getFileSystem().mainFiles(ColdFusion.KEY)));

    // Collection<SourceCode> squidSourceFiles = scanner.getIndex().search(new QueryByType(SourceFile.class));
    // save(squidSourceFiles);
  }

  // private void saveMeasures(File sonarFile, SourceFile squidFile) {
  // context.saveMeasure(sonarFile, CoreMetrics.FILES, squidFile.getDouble(CFMetric.FILES));
  // context.saveMeasure(sonarFile, CoreMetrics.LINES, squidFile.getDouble(CFMetric.LINES));
  // context.saveMeasure(sonarFile, CoreMetrics.NCLOC, squidFile.getDouble(CFMetric.LINES_OF_CODE));
  // context.saveMeasure(sonarFile, CoreMetrics.FUNCTIONS, squidFile.getDouble(CFMetric.FUNCTIONS));
  // context.saveMeasure(sonarFile, CoreMetrics.STATEMENTS, squidFile.getDouble(CFMetric.STATEMENTS));
  // context.saveMeasure(sonarFile, CoreMetrics.COMPLEXITY, squidFile.getDouble(CFMetric.COMPLEXITY));
  // context.saveMeasure(sonarFile, CoreMetrics.COMMENT_LINES, squidFile.getDouble(CFMetric.COMMENT_LINES));
  // }

  // private void saveFunctionsComplexityDistribution(File sonarFile, SourceFile squidFile) {
  // Collection<SourceCode> squidFunctionsInFile = scanner.getIndex().search(new QueryByParent(squidFile), new
  // QueryByType(SourceFunction.class));
  // RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION,
  // FUNCTIONS_DISTRIB_BOTTOM_LIMITS);
  // for (SourceCode squidFunction : squidFunctionsInFile) {
  // complexityDistribution.add(squidFunction.getDouble(CFMetric.COMPLEXITY));
  // }
  // context.saveMeasure(sonarFile, complexityDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
  // }

  // private void saveFilesComplexityDistribution(File sonarFile, SourceFile squidFile) {
  // RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION,
  // FILES_DISTRIB_BOTTOM_LIMITS);
  // complexityDistribution.add(squidFile.getDouble(CFMetric.COMPLEXITY));
  // context.saveMeasure(sonarFile, complexityDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
  // }

  // private void saveViolations(File sonarFile, SourceFile squidFile) {
  // Collection<CheckMessage> messages = squidFile.getCheckMessages();
  // if (messages != null) {
  // for (CheckMessage message : messages) {
  // Violation violation = Violation.create(annotationCheckFactory.getActiveRule(message.getCheck()), sonarFile)
  // .setLineId(message.getLine())
  // .setMessage(message.getText(Locale.ENGLISH));
  // context.saveViolation(violation);
  // }
  // }
  // }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
