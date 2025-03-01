///////////////////////////////////////////////////////////////////////////
//                   __                _      _   ________               //
//                  / /   ____  ____ _(_)____/ | / / ____/               //
//                 / /   / __ \/ __ `/ / ___/  |/ / / __                 //
//                / /___/ /_/ / /_/ / / /__/ /|  / /_/ /                 //
//               /_____/\____/\__, /_/\___/_/ |_/\____/                  //
//                           /____/                                      //
//                                                                       //
//               The Next Generation Logic Library                       //
//                                                                       //
///////////////////////////////////////////////////////////////////////////
//                                                                       //
//  Copyright 2015-20xx Christoph Zengler                                //
//                                                                       //
//  Licensed under the Apache License, Version 2.0 (the "License");      //
//  you may not use this file except in compliance with the License.     //
//  You may obtain a copy of the License at                              //
//                                                                       //
//  http://www.apache.org/licenses/LICENSE-2.0                           //
//                                                                       //
//  Unless required by applicable law or agreed to in writing, software  //
//  distributed under the License is distributed on an "AS IS" BASIS,    //
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or      //
//  implied.  See the License for the specific language governing        //
//  permissions and limitations under the License.                       //
//                                                                       //
///////////////////////////////////////////////////////////////////////////

package org.logicng.knowledgecompilation.bdds;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.logicng.datastructures.Assignment;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PseudoBooleanParser;
import org.logicng.knowledgecompilation.bdds.orderings.VariableOrdering;
import org.logicng.predicates.satisfiability.TautologyPredicate;

/**
 * Unit tests for the BDD generation in the {@link org.logicng.formulas.Formula} class.
 * @version 2.0.0
 * @since 1.4.0
 */
public class FormulaBDDTest {

    @Test
    public void testSimpleCases() {
        final FormulaFactory f = new FormulaFactory();
        BDD bdd = f.verum().bdd();
        assertThat(bdd.isTautology()).isTrue();
        bdd = f.falsum().bdd();
        assertThat(bdd.isContradiction()).isTrue();
        bdd = f.variable("A").bdd();
        assertThat(bdd.enumerateAllModels()).containsExactly(new Assignment(f.variable("A")));
    }

    @Test
    public void testBDDGeneration() throws ParserException {
        final FormulaFactory f = new FormulaFactory();
        final PseudoBooleanParser p = new PseudoBooleanParser(f);
        final Formula formula = p.parse("(A => ~B) & ((A & C) | (D & ~C)) & (A | Y | X) & (Y <=> (X | (W + A + F < 1)))");
        final BDD bddNoOrder = formula.bdd();
        final BDD bddBfs = formula.bdd(VariableOrdering.BFS);
        final BDD bddDfs = formula.bdd(VariableOrdering.DFS);
        final BDD bddMin2Max = formula.bdd(VariableOrdering.MIN2MAX);
        final BDD bddMax2Min = formula.bdd(VariableOrdering.MAX2MIN);

        assertThat(bddNoOrder.nodeCount()).isEqualTo(13);
        assertThat(bddBfs.nodeCount()).isEqualTo(14);
        assertThat(bddDfs.nodeCount()).isEqualTo(13);
        assertThat(bddMin2Max.nodeCount()).isEqualTo(14);
        assertThat(bddMax2Min.nodeCount()).isEqualTo(22);

        final TautologyPredicate tautology = new TautologyPredicate(f);
        assertThat(f.equivalence(bddNoOrder.cnf(), formula).holds(tautology)).isTrue();
        assertThat(f.equivalence(bddBfs.cnf(), formula).holds(tautology)).isTrue();
        assertThat(f.equivalence(bddDfs.cnf(), formula).holds(tautology)).isTrue();
        assertThat(f.equivalence(bddMin2Max.cnf(), formula).holds(tautology)).isTrue();
        assertThat(f.equivalence(bddMax2Min.cnf(), formula).holds(tautology)).isTrue();
    }

    @Test
    public void testNonNnfs() throws ParserException {
        final FormulaFactory f = new FormulaFactory();
        assertThat(f.parse("A + 2*B - C = 1").bdd()).isNotNull();
        assertThat(f.parse("(A & B & C | D & E & F) & (A - 2*B -D <= 0) | (C + 3*D - F > 0)").bdd()).isNotNull();
    }
}
