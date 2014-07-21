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
package org.sonar.coldfusion.model;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.impl.Parser;
import org.junit.Test;
import org.sonar.coldfusion.CFConfiguration;
import org.sonar.coldfusion.api.CFKeyword;
import org.sonar.coldfusion.api.CFPunctuator;
import org.sonar.coldfusion.api.CFTokenType;
import org.sonar.coldfusion.parser.CFGrammar;
import org.sonar.coldfusion.parser.CFParser;

import static org.fest.assertions.Assertions.assertThat;

public class ASTMakerTest {

  private final Parser p = CFParser.create(new CFConfiguration(Charsets.UTF_8));

  @Test
  public void variable() {
    AstNode astNode = p.parse("variables.var1 = 16;").getFirstDescendant(CFGrammar.VARIABLE);
    VariableTree tree = (VariableTree) ASTMaker.create().makeFrom(astNode);
    assertThat(tree.name()).isEqualTo("variables");
    assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);

    astNode = p.parse("local.var1 = 16;").getFirstDescendant(CFGrammar.VARIABLE);
    tree = (VariableTree) ASTMaker.create().makeFrom(astNode);
    assertThat(tree.name()).isEqualTo("local");
    assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);

    astNode = p.parse("this.var1 = 16;").getFirstDescendant(CFGrammar.VARIABLE);
    tree = (VariableTree) ASTMaker.create().makeFrom(astNode);
    assertThat(tree.name()).isEqualTo("this");
    assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);

    astNode = p.parse("var1 = 16;").getFirstDescendant(CFGrammar.VARIABLE);
    tree = (VariableTree) ASTMaker.create().makeFrom(astNode);
    assertThat(tree.name()).isEqualTo("var1");
    assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);

    astNode = p.parse("var1;").getFirstDescendant(CFGrammar.VARIABLE);
    tree = (VariableTree) ASTMaker.create().makeFrom(astNode);
    assertThat(tree.name()).isEqualTo("var1");
    assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  }

  @Test
  public void implicitArray() {
    AstNode astNode = p.parse("a = [ 42 , a ];").getFirstDescendant(CFGrammar.IMPLICIT_ARRAY);
    ImplicitArrayTree tree = (ImplicitArrayTree) ASTMaker.create().makeFrom(astNode);
    assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
    assertThat(tree.expressions()).hasSize(1);
  }

  @Test
  public void implicitStruct() {
    AstNode astNode = p.parse("a = {test: test1, test2: test3};").getFirstDescendant(CFGrammar.IMPLICIT_STRUCT);
    ImplicitStructTree tree = (ImplicitStructTree) ASTMaker.create().makeFrom(astNode);
    assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
    assertThat(tree.expressions()).hasSize(1);
  }

  @Test
  public void root() {
    AstNode astNode = p.parse("component {}");
    ProgramTree tree = (ProgramTree) ASTMaker.create().makeFrom(astNode);
    assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
    assertThat(tree.sourceElements()).hasSize(1);
  }

  // @Test
  // public void returnStatement() {
  //   AstNode astNode = p.parse("return ;").getFirstDescendant(CFGrammar.RETURN_STATEMENT);
  //   ReturnStatementTree tree = (ReturnStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expression()).isNull();

  //   astNode = p.parse("return true ;").getFirstDescendant(CFGrammar.RETURN_STATEMENT);
  //   tree = (ReturnStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expression()).isNotNull();
  // }

  // @Test
  // public void literal() {
  //   AstNode astNode = p.parse("null").getFirstDescendant(CFGrammar.LITERAL);
  //   LiteralTree tree = (LiteralTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  // }

  // @Test
  // public void componentDeclaration() {
  //   AstNode astNode = p.parse("component {}").getFirstDescendant(CFGrammar.COMPONENT_DECLARATION);
  //   ComponentDeclarationTree tree = (ComponentDeclarationTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expressions()).hasSize(1);
  // }

  // @Test
  // public void groupingOperator() {
  //   AstNode astNode = p.parse("true;").getFirstDescendant(CFGrammar.PRIMARY_EXPRESSION);
  //   assertThat(ASTMaker.create().makeFrom(astNode)).isInstanceOf(LiteralTree.class);

  //   astNode = p.parse("(true);").getFirstDescendant(CFGrammar.PRIMARY_EXPRESSION);
  //   ParenthesizedTree tree = (ParenthesizedTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expression()).isNotNull();
  // }

  // @Test
  // public void objectLiteral() {
  //   AstNode astNode = p.parse("a = { p : 42 };").getFirstDescendant(CFGrammar.OBJECT_LITERAL);
  //   ObjectLiteralTree tree = (ObjectLiteralTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.propertyAssignments()).isNotEmpty();

  //   astNode = p.parse("a = { p : 42 };").getFirstDescendant(CFGrammar.OBJECT_LITERAL);
  //   tree = (ObjectLiteralTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.propertyAssignments()).isNotEmpty();
  // }

  // @Test
  // public void propertyAssignment() {
  //   // property
  //   AstNode astNode = p.parse("a = { p : 42 };").getFirstDescendant(CFGrammar.PROPERTY_ASSIGNMENT);
  //   PropertyAssignmentTree tree = (PropertyAssignmentTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(tree.propertyName()).isNotNull();
  //   assertThat(tree.expression()).isNotNull();
  //   assertThat(tree.propertySetParameters()).isNull();
  //   assertThat(tree.body()).isNull();
  //   // getter
  //   astNode = p.parse("a = { get p() {} };").getFirstDescendant(CFGrammar.PROPERTY_ASSIGNMENT);
  //   tree = (PropertyAssignmentTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(tree.propertyName()).isNotNull();
  //   assertThat(tree.expression()).isNull();
  //   assertThat(tree.propertySetParameters()).isNull();
  //   assertThat(tree.body()).isNotNull();
  //   // setter
  //   astNode = p.parse("a = { set p(v) {} };").getFirstDescendant(CFGrammar.PROPERTY_ASSIGNMENT);
  //   tree = (PropertyAssignmentTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(tree.propertyName()).isNotNull();
  //   assertThat(tree.expression()).isNull();
  //   assertThat(tree.propertySetParameters()).isNotEmpty();
  //   assertThat(tree.body()).isNotNull();
  // }

  // @Test
  // public void memberExpression() {
  //   // new
  //   AstNode astNode = p.parse("new foo();").getFirstDescendant(CFGrammar.MEMBER_EXPRESSION);
  //   NewOperatorTree newOperatorTree = (NewOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) newOperatorTree).astNode).isSameAs(astNode);
  //   assertThat(newOperatorTree.constructor()).isNotNull();
  //   assertThat(newOperatorTree.arguments()).isEmpty();
  //   // index
  //   astNode = p.parse("foo[0];").getFirstDescendant(CFGrammar.MEMBER_EXPRESSION);
  //   IndexAccessTree indexAccessTree = (IndexAccessTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(indexAccessTree.expression()).isNotNull();
  //   assertThat(indexAccessTree.index()).isNotNull();
  //   // property
  //   astNode = p.parse("foo.bar;").getFirstDescendant(CFGrammar.MEMBER_EXPRESSION);
  //   PropertyAccessTree propertyAccessTree = (PropertyAccessTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(propertyAccessTree.expression()).isNotNull();
  //   assertThat(propertyAccessTree.identifier()).isNotNull();
  // }

  // @Test
  // public void newExpression() {
  //   AstNode astNode = p.parse("foo;").getFirstDescendant(CFGrammar.NEW_EXPRESSION);
  //   assertThat(ASTMaker.create().makeFrom(astNode)).isInstanceOf(VariableTree.class);

  //   astNode = p.parse("new foo;").getFirstDescendant(CFGrammar.NEW_EXPRESSION);
  //   NewOperatorTree tree = (NewOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.constructor()).isNotNull();
  //   assertThat(tree.arguments()).isEmpty();
  // }

  // @Test
  // public void callExpression() {
  //   AstNode astNode = p.parse("foo();").getFirstDescendant(CFGrammar.CALL_EXPRESSION);
  //   FunctionCallTree tree = (FunctionCallTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expression()).isNotNull();
  //   assertThat(tree.arguments()).isEmpty();

  //   astNode = p.parse("foo()(true);").getFirstDescendant(CFGrammar.CALL_EXPRESSION);
  //   tree = (FunctionCallTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expression()).isInstanceOf(FunctionCallTree.class);
  //   assertThat(tree.arguments()).hasSize(1);

  //   astNode = p.parse("foo()[0];").getFirstDescendant(CFGrammar.CALL_EXPRESSION);
  //   IndexAccessTree indexAccessTree = (IndexAccessTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(indexAccessTree.expression()).isNotNull();
  //   assertThat(indexAccessTree.index()).isNotNull();

  //   astNode = p.parse("foo().bar;").getFirstDescendant(CFGrammar.CALL_EXPRESSION);
  //   PropertyAccessTree propertyAccessTree = (PropertyAccessTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(indexAccessTree.expression()).isNotNull();
  //   assertThat(propertyAccessTree.identifier()).isNotNull();
  // }

  // @Test
  // public void postfixExpression() {
  //   AstNode astNode = p.parse("1;").getFirstDescendant(CFGrammar.POSTFIX_EXPRESSION);
  //   assertThat(ASTMaker.create().makeFrom(astNode)).isInstanceOf(LiteralTree.class);

  //   astNode = p.parse("1++;").getFirstDescendant(CFGrammar.POSTFIX_EXPRESSION);
  //   UnaryOperatorTree tree = (UnaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.operand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.INC);
  // }

  // @Test
  // public void unaryExpression() {
  //   AstNode astNode = p.parse("true;").getFirstDescendant(CFGrammar.UNARY_EXPRESSION);
  //   assertThat(ASTMaker.create().makeFrom(astNode)).isInstanceOf(LiteralTree.class);

  //   astNode = p.parse("typeof true;").getFirstDescendant(CFGrammar.UNARY_EXPRESSION);
  //   UnaryOperatorTree tree = (UnaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.operand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFKeyword.TYPEOF);
  // }

  // @Test
  // public void multiplicativeExpression() {
  //   AstNode astNode = p.parse("true * true").getFirstDescendant(CFGrammar.MULTIPLICATIVE_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.STAR);
  //   assertThat(tree.rightOperand()).isNotNull();

  //   astNode = p.parse("true / true * true").getFirstDescendant(CFGrammar.MULTIPLICATIVE_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.DIV);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  // }

  // @Test
  // public void additiveExpression() {
  //   AstNode astNode = p.parse("true + true").getFirstDescendant(CFGrammar.ADDITIVE_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.PLUS);
  //   assertThat(tree.rightOperand()).isNotNull();

  //   astNode = p.parse("true - true + true").getFirstDescendant(CFGrammar.ADDITIVE_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.MINUS);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);
  // }

  // @Test
  // public void shiftExpression() {
  //   AstNode astNode = p.parse("true << true").getFirstDescendant(CFGrammar.SHIFT_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.SL);
  //   assertThat(tree.rightOperand()).isNotNull();

  //   astNode = p.parse("true >> true << true").getFirstDescendant(CFGrammar.SHIFT_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.SR);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);
  // }

  // @Test
  // public void relationalExpression() {
  //   AstNode astNode = p.parse("true < true").getFirstDescendant(CFGrammar.RELATIONAL_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.LT);
  //   assertThat(tree.rightOperand()).isNotNull();

  //   astNode = p.parse("true instanceof true < true").getFirstDescendant(CFGrammar.RELATIONAL_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFKeyword.INSTANCEOF);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);
  // }

  // @Test
  // public void equalityExpression() {
  //   AstNode astNode = p.parse("true == true").getFirstDescendant(CFGrammar.EQUALITY_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.EQUAL);
  //   assertThat(tree.rightOperand()).isNotNull();

  //   astNode = p.parse("true != true == true").getFirstDescendant(CFGrammar.EQUALITY_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.NOTEQUAL);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);
  // }

  // @Test
  // public void bitwiseAndExpression() {
  //   AstNode astNode = p.parse("true & true").getFirstDescendant(CFGrammar.BITWISE_AND_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.AND);
  //   assertThat(tree.rightOperand()).isNotNull();

  //   astNode = p.parse("true & true & true").getFirstDescendant(CFGrammar.BITWISE_AND_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.AND);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);
  // }

  // @Test
  // public void bitwiseXorExpression() {
  //   AstNode astNode = p.parse("true ^ true").getFirstDescendant(CFGrammar.BITWISE_XOR_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.XOR);
  //   assertThat(tree.rightOperand()).isNotNull();

  //   astNode = p.parse("true ^ true ^ true").getFirstDescendant(CFGrammar.BITWISE_XOR_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.XOR);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);
  // }

  // @Test
  // public void bitwiseOrExpression() {
  //   AstNode astNode = p.parse("true | true").getFirstDescendant(CFGrammar.BITWISE_OR_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.OR);
  //   assertThat(tree.rightOperand()).isNotNull();

  //   astNode = p.parse("true | true | true").getFirstDescendant(CFGrammar.BITWISE_OR_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.OR);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);
  // }

  // @Test
  // public void logicalAndExpression() {
  //   AstNode astNode = p.parse("true && true").getFirstDescendant(CFGrammar.EQUIVALENT_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.ANDOPERATOR);
  //   assertThat(tree.rightOperand()).isNotNull();

  //   astNode = p.parse("true && true && true").getFirstDescendant(CFGrammar.EQUIVALENT_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.ANDOPERATOR);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);


  //   astNode = p.parse("true AND true").getFirstDescendant(CFGrammar.EQUIVALENT_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.ANDOPERATOR);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);


  //   astNode = p.parse("true AND true").getFirstDescendant(CFGrammar.EQUIVALENT_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.ANDOPERATOR);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);
  // }

  // @Test
  // public void logicalOrExpression() {
  //   AstNode astNode = p.parse("true || true").getFirstDescendant(CFGrammar.LOGICAL_OR_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.OROR);
  //   assertThat(tree.rightOperand()).isNotNull();

  //   astNode = p.parse("true || true || true").getFirstDescendant(CFGrammar.LOGICAL_OR_EXPRESSION);
  //   tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.OROR);
  //   assertThat(tree.rightOperand()).isInstanceOf(BinaryOperatorTree.class);
  // }

  // @Test
  // public void conditionalExpression() {
  //   AstNode astNode = p.parse("true ? false : true").getFirstDescendant(CFGrammar.CONDITIONAL_EXPRESSION);
  //   ConditionalOperatorTree tree = (ConditionalOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.condition()).isNotNull();
  //   assertThat(tree.thenExpression()).isNotNull();
  //   assertThat(tree.elseExpression()).isNotNull();
  // }

  // @Test
  // public void assignmentExpression() {
  //   AstNode astNode = p.parse("true = true").getFirstDescendant(CFGrammar.ASSIGNMENT_EXPRESSION);
  //   BinaryOperatorTree tree = (BinaryOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.leftOperand()).isNotNull();
  //   assertThat(tree.operator()).isEqualTo(CFPunctuator.EQU);
  //   assertThat(tree.rightOperand()).isNotNull();
  // }

  // @Test
  // public void expression() {
  //   AstNode astNode = p.parse("true").getFirstDescendant(CFGrammar.EXPRESSION);
  //   assertThat(ASTMaker.create().makeFrom(astNode)).isInstanceOf(LiteralTree.class);

  //   astNode = p.parse("true, true").getFirstDescendant(CFGrammar.EXPRESSION);
  //   CommaOperatorTree tree = (CommaOperatorTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expressions()).hasSize(2);
  // }

  // @Test
  // public void block() {
  //   AstNode astNode = p.parse("{ }").getFirstDescendant(CFGrammar.BLOCK);
  //   BlockTree tree = (BlockTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.statements()).isEmpty();

  //   astNode = p.parse("{ ; }").getFirstDescendant(CFGrammar.BLOCK);
  //   tree = (BlockTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.statements()).hasSize(1);

  //   astNode = p.parse("{ ; ; }").getFirstDescendant(CFGrammar.BLOCK);
  //   tree = (BlockTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.statements()).hasSize(2);

  //   astNode = p.parse("{ function fun() { } }").getFirstDescendant(CFGrammar.BLOCK);
  //   tree = (BlockTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(tree.statements().get(0)).isInstanceOf(FunctionTree.class);
  // }

  // @Test
  // public void variableStatement() {
  //   AstNode astNode = p.parse("var a;").getFirstDescendant(CFGrammar.VARIABLE_STATEMENT);
  //   VariableStatementTree tree = (VariableStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.declarations()).hasSize(1);

  //   astNode = p.parse("var a, b;").getFirstDescendant(CFGrammar.VARIABLE_STATEMENT);
  //   tree = (VariableStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.declarations()).hasSize(2);
  // }

  // @Test
  // public void variableDeclaration() {
  //   AstNode astNode = p.parse("var a;").getFirstDescendant(CFGrammar.VARIABLE_DECLARATION);
  //   VariableDeclarationTree tree = (VariableDeclarationTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.identifier()).isNotNull();
  //   assertThat(tree.initialiser()).isNull();

  //   astNode = p.parse("var a = true;").getFirstDescendant(CFGrammar.VARIABLE_DECLARATION);
  //   tree = (VariableDeclarationTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.identifier()).isNotNull();
  //   assertThat(tree.initialiser()).isNotNull();
  // }

  // @Test
  // public void breakStatement() {
  //   AstNode astNode = p.parse("break").getFirstDescendant(CFGrammar.BREAK_STATEMENT);
  //   BreakStatementTree tree = (BreakStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.label()).isNull();

  //   astNode = p.parse("break label").getFirstDescendant(CFGrammar.BREAK_STATEMENT);
  //   tree = (BreakStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.label()).isNotNull();
  // }

  // @Test
  // public void continueStatement() {
  //   AstNode astNode = p.parse("continue").getFirstDescendant(CFGrammar.CONTINUE_STATEMENT);
  //   ContinueStatementTree tree = (ContinueStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.label()).isNull();

  //   astNode = p.parse("continue label").getFirstDescendant(CFGrammar.CONTINUE_STATEMENT);
  //   tree = (ContinueStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.label()).isNotNull();
  // }

  // @Test
  // public void debuggerStatement() {
  //   AstNode astNode = p.parse("debugger").getFirstDescendant(CFGrammar.DEBUGGER_STATEMENT);
  //   DebuggerStatementTree tree = (DebuggerStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree).isNotNull();
  // }

  // @Test
  // public void emptyStatement() {
  //   AstNode astNode = p.parse(";").getFirstDescendant(CFGrammar.EMPTY_STATEMENT);
  //   EmptyStatementTree tree = (EmptyStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree).isNotNull();
  // }

  // @Test
  // public void expressionStatement() {
  //   AstNode astNode = p.parse("true ;").getFirstDescendant(CFGrammar.EXPRESSION_STATEMENT);
  //   ExpressionStatementTree tree = (ExpressionStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expression()).isNotNull();
  // }

  // @Test
  // public void ifStatement() {
  //   AstNode astNode = p.parse("if (true) { ; ; }").getFirstDescendant(CFGrammar.IF_STATEMENT);
  //   IfStatementTree tree = (IfStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.condition()).isNotNull();
  //   assertThat(tree.thenStatement()).isNotNull();
  //   assertThat(tree.elseStatement()).isNull();
  // }

  // @Test
  // public void labelledStatement() {
  //   AstNode astNode = p.parse("label : ;").getFirstDescendant(CFGrammar.LABELLED_STATEMENT);
  //   LabelledStatementTree tree = (LabelledStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.label()).isNotNull();
  // }


  // @Test
  // public void throwStatement() {
  //   AstNode astNode = p.parse("throw e ;").getFirstDescendant(CFGrammar.THROW_STATEMENT);
  //   ThrowStatementTree tree = (ThrowStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expression()).isNotNull();
  // }

  // @Test
  // public void whileStatement() {
  //   AstNode astNode = p.parse("while (true) ;").getFirstDescendant(CFGrammar.WHILE_STATEMENT);
  //   WhileStatementTree tree = (WhileStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.condition()).isNotNull();
  //   assertThat(tree.statement()).isNotNull();
  // }

  // @Test
  // public void doWhileStatement() {
  //   AstNode astNode = p.parse("do {} while (true) ;").getFirstDescendant(CFGrammar.DO_WHILE_STATEMENT);
  //   DoWhileStatementTree tree = (DoWhileStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.statement()).isNotNull();
  //   assertThat(tree.condition()).isNotNull();
  // }

  // @Test
  // public void forStatement() {
  //   AstNode astNode = p.parse("for ( ; ; ) ;").getFirstDescendant(CFGrammar.FOR_STATEMENT);
  //   ForStatementTree tree = (ForStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.initVariables()).isNull();
  //   assertThat(tree.initExpression()).isNull();
  //   assertThat(tree.condition()).isNull();
  //   assertThat(tree.incrementExpression()).isNull();
  //   assertThat(tree.statement()).isNotNull();

  //   astNode = p.parse("for ( i = 0 ; i < 10 ; i++ ) ;").getFirstDescendant(CFGrammar.FOR_STATEMENT);
  //   tree = (ForStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.initVariables()).isNull();
  //   assertThat(tree.initExpression()).isNotNull();
  //   assertThat(tree.condition()).isNotNull();
  //   assertThat(tree.incrementExpression()).isNotNull();
  //   assertThat(tree.statement()).isNotNull();

  //   astNode = p.parse("for ( var i = 0 ; i < 10 ; i++ ) ;").getFirstDescendant(CFGrammar.FOR_STATEMENT);
  //   tree = (ForStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.initVariables()).hasSize(1);
  //   assertThat(tree.initExpression()).isNull();
  //   assertThat(tree.condition()).isNotNull();
  //   assertThat(tree.incrementExpression()).isNotNull();
  //   assertThat(tree.statement()).isNotNull();
  // }

  // @Test
  // public void forInStatement() {
  //   AstNode astNode = p.parse("for ( i in a ) ;").getFirstDescendant(CFGrammar.FOR_IN_STATEMENT);
  //   ForInStatementTree tree = (ForInStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.initVariables()).isNull();
  //   assertThat(tree.leftHandSideExpression()).isNotNull();
  //   assertThat(tree.expression()).isNotNull();
  //   assertThat(tree.statement()).isNotNull();

  //   astNode = p.parse("for ( var i in a ) ;").getFirstDescendant(CFGrammar.FOR_IN_STATEMENT);
  //   tree = (ForInStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.initVariables()).hasSize(1);
  //   assertThat(tree.leftHandSideExpression()).isNull();
  //   assertThat(tree.expression()).isNotNull();
  //   assertThat(tree.statement()).isNotNull();
  // }

  // @Test
  // public void withStatement() {
  //   AstNode astNode = p.parse("with ( e ) ;").getFirstDescendant(CFGrammar.WITH_STATEMENT);
  //   WithStatementTree tree = (WithStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expression()).isNotNull();
  //   assertThat(tree.statement()).isNotNull();
  // }

  // @Test
  // public void switchStatement() {
  //   AstNode astNode = p.parse("switch (a) { case 1: break; case 2: break; default: break; }").getFirstDescendant(CFGrammar.SWITCH_STATEMENT);
  //   SwitchStatementTree tree = (SwitchStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.cases()).hasSize(3);
  // }

  // @Test
  // public void caseClause() {
  //   AstNode astNode = p.parse("switch (a) { case 1: ; case 2: ; default: ; }").getFirstDescendant(CFGrammar.CASE_CLAUSE);
  //   CaseClauseTree tree = (CaseClauseTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expression()).isNotNull();
  //   assertThat(tree.statements()).hasSize(1);
  // }

  // @Test
  // public void defaultClause() {
  //   AstNode astNode = p.parse("switch (a) { case 1: ; default: ; }").getFirstDescendant(CFGrammar.DEFAULT_CLAUSE);
  //   CaseClauseTree tree = (CaseClauseTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.expression()).isNull();
  //   assertThat(tree.statements()).hasSize(1);
  // }

  // @Test
  // public void tryStatement() {
  //   AstNode astNode = p.parse("try { } catch(e) { }").getFirstDescendant(CFGrammar.TRY_STATEMENT);
  //   TryStatementTree tree = (TryStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.block()).isNotNull();
  //   assertThat(tree.catchBlock()).isNotNull();
  //   assertThat(tree.finallyBlock()).isNull();

  //   astNode = p.parse("try { } finally { }").getFirstDescendant(CFGrammar.TRY_STATEMENT);
  //   tree = (TryStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.block()).isNotNull();
  //   assertThat(tree.catchBlock()).isNull();
  //   assertThat(tree.finallyBlock()).isNotNull();

  //   astNode = p.parse("try { } catch(e) { } finally { }").getFirstDescendant(CFGrammar.TRY_STATEMENT);
  //   tree = (TryStatementTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.block()).isNotNull();
  //   assertThat(tree.catchBlock()).isNotNull();
  //   assertThat(tree.finallyBlock()).isNotNull();
  // }


  // @Test
  // public void functionDeclaration() {
  //   AstNode astNode = p.parse("function fun() {}").getFirstDescendant(CFGrammar.FUNCTION_DECLARATION);
  //   FunctionTree tree = (FunctionTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.identifier()).isNotNull();
  //   assertThat(tree.formalParameterList()).isEmpty();
  //   assertThat(tree.body()).isEmpty();
  // }

  // @Test
  // public void functionExpression() {
  //   AstNode astNode = p.parse("f = function() {}").getFirstDescendant(CFGrammar.FUNCTION_EXPRESSION);
  //   FunctionTree tree = (FunctionTree) ASTMaker.create().makeFrom(astNode);
  //   assertThat(((TreeImpl) tree).astNode).isSameAs(astNode);
  //   assertThat(tree.identifier()).isNull();
  //   assertThat(tree.formalParameterList()).isEmpty();
  //   assertThat(tree.body()).isEmpty();
  // };

}
