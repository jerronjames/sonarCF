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
import org.sonar.api.rules.AnnotationRuleParser;
import org.sonar.api.rules.Rule;
import org.sonar.coldfusion.checks.CheckList;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class ColdFusionRuleRepositoryTest {

  @Test
  public void test() {
    ColdFusionRuleRepository ruleRepository = new ColdFusionRuleRepository(new AnnotationRuleParser());
    assertThat(ruleRepository.getKey()).isEqualTo("coldfusion");
    assertThat(ruleRepository.getName()).isEqualTo("SonarQube");
    List<Rule> rules = ruleRepository.createRules();
    assertThat(rules.size()).isEqualTo(CheckList.getChecks().size());
  }

}
