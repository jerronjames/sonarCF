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

import org.sonar.commonrules.api.CommonRulesEngine;
import org.sonar.commonrules.api.CommonRulesRepository;
import org.sonar.plugins.coldfusion.core.ColdFusion;

public class ColdFusionCommonRulesEngine extends CommonRulesEngine {

  public ColdFusionCommonRulesEngine() {
    super(ColdFusion.KEY);
  }

  @Override
  protected void doEnableRules(CommonRulesRepository repository) {
    // null parameters -> use the default values of parameters as
    // hardcoded in sonar-common-rules
    repository
      .enableDuplicatedBlocksRule()
      .enableInsufficientCommentDensityRule(null)
      .enableInsufficientLineCoverageRule(null)
      .enableInsufficientBranchCoverageRule(null);
  }

}
