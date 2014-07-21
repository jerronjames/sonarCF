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

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.sonar.api.measures.CoverageMeasuresBuilder;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.utils.SonarException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * http://ltp.sourceforge.net/coverage/lcov/geninfo.1.php
 */
public final class LCOVParser {

  private static final String SF = "SF:";
  private static final String DA = "DA:";
  private static final String BRDA = "BRDA:";

  private final ProjectFileSystem projectFileSystem;

  public LCOVParser(ProjectFileSystem projectFileSystem) {
    this.projectFileSystem = projectFileSystem;
  }

  public Map<String, CoverageMeasuresBuilder> parseFile(File file) {
    final List<String> lines;
    try {
      lines = FileUtils.readLines(file);
    } catch (IOException e) {
      throw new SonarException("Could not read content from file: " + file, e);
    }
    return parse(lines);
  }

  public Map<String, CoverageMeasuresBuilder> parse(List<String> lines) {
    final Map<String, FileData> files = Maps.newHashMap();
    FileData fileData = new FileData();

    for (String line : lines) {
      if (line.startsWith(SF)) {
        // SF:<absolute path to the source file>
        String filePath = line.substring(SF.length());

        // some tools (like Istanbul, Karma) provide relative paths, so let's consider them relative to project directory
        filePath = projectFileSystem.resolvePath(filePath).getAbsolutePath();

        fileData = files.get(filePath);
        if (fileData == null) {
          fileData = new FileData();
          files.put(filePath, fileData);
        }

      } else if (line.startsWith(DA)) {
        // DA:<line number>,<execution count>[,<checksum>]
        String execution = line.substring(DA.length());
        String executionCount = execution.substring(execution.indexOf(',') + 1);
        String lineNumber = execution.substring(0, execution.indexOf(','));

        fileData.addLine(Integer.valueOf(lineNumber), Integer.valueOf(executionCount));

      } else if (line.startsWith(BRDA)) {
        // BRDA:<line number>,<block number>,<branch number>,<taken>
        String[] tokens = line.substring(BRDA.length()).trim().split(",");
        String lineNumber = tokens[0];
        String branchNumber = tokens[2];
        String taken = tokens[3];

        fileData.addBranch(Integer.valueOf(lineNumber), branchNumber, "-".equals(taken) ? 0 : Integer.valueOf(taken));
      }
    }

    Map<String, CoverageMeasuresBuilder> coveredFiles = Maps.newHashMap();
    for (Map.Entry<String, FileData> e : files.entrySet()) {
      coveredFiles.put(e.getKey(), e.getValue().convert());
    }
    return coveredFiles;
  }

  private static class FileData {
    /**
     * line number -> branch number -> taken
     */
    private Map<Integer, Map<String, Integer>> branches = Maps.newHashMap();

    /**
     * line number -> execution count
     */
    private Map<Integer, Integer> hits = Maps.newHashMap();

    public void addBranch(Integer lineNumber, String branchNumber, Integer taken) {
      Map<String, Integer> branchesForLine = branches.get(lineNumber);
      if (branchesForLine == null) {
        branchesForLine = Maps.newHashMap();
        branches.put(lineNumber, branchesForLine);
      }
      Integer currentValue = branchesForLine.get(branchNumber);
      branchesForLine.put(branchNumber, Objects.firstNonNull(currentValue, 0) + taken);
    }

    public void addLine(Integer lineNumber, Integer executionCount) {
      Integer currentValue = hits.get(lineNumber);
      hits.put(lineNumber, Objects.firstNonNull(currentValue, 0) + executionCount);
    }

    public CoverageMeasuresBuilder convert() {
      CoverageMeasuresBuilder result = CoverageMeasuresBuilder.create();
      for (Map.Entry<Integer, Integer> e : hits.entrySet()) {
        result.setHits(e.getKey(), e.getValue());
      }
      for (Map.Entry<Integer, Map<String, Integer>> e : branches.entrySet()) {
        int conditions = e.getValue().size();
        int covered = 0;
        for (Integer taken : e.getValue().values()) {
          if (taken > 0) {
            covered++;
          }
        }
        result.setConditions(e.getKey(), conditions, covered);
      }
      return result;
    }
  }

}
