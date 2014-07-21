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

import org.junit.Test;
import org.sonar.commonrules.api.CommonRulesRepository;

import static org.fest.assertions.Assertions.assertThat;

public class ColdFusionCommonRulesEngineTest {

  @Test
  public void provide_extensions() {
    ColdFusionCommonRulesEngine engine = new ColdFusionCommonRulesEngine();
    assertThat(engine.provide()).isNotEmpty();
  }

  @Test
  public void define_rules() {
    ColdFusionCommonRulesEngine engine = new ColdFusionCommonRulesEngine();
    CommonRulesRepository repo = engine.newRepository();
    assertThat(repo.rules()).hasSize(4);
    assertThat(repo.rule(CommonRulesRepository.RULE_INSUFFICIENT_COMMENT_DENSITY)).isNotNull();
    assertThat(repo.rule(CommonRulesRepository.RULE_DUPLICATED_BLOCKS)).isNotNull();
    assertThat(repo.rule(CommonRulesRepository.RULE_INSUFFICIENT_LINE_COVERAGE)).isNotNull();
    assertThat(repo.rule(CommonRulesRepository.RULE_INSUFFICIENT_BRANCH_COVERAGE)).isNotNull();
  }

}
