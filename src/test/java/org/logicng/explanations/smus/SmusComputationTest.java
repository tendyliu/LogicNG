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

package org.logicng.explanations.smus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.logicng.TestWithExampleFormulas;
import org.logicng.formulas.Formula;
import org.logicng.handlers.ComputationHandler;
import org.logicng.handlers.OptimizationHandler;
import org.logicng.handlers.SmusHandler;
import org.logicng.handlers.TimeoutHandler;
import org.logicng.handlers.TimeoutOptimizationHandler;
import org.logicng.io.parsers.ParserException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Unit Tests for the class {@link SmusComputation}.
 * @version 2.1.0
 * @since 2.0.0
 */
public class SmusComputationTest extends TestWithExampleFormulas {

    @Test
    public void testFromPaper() throws ParserException {
        final List<Formula> input = Arrays.asList(
                this.f.parse("~s"),
                this.f.parse("s|~p"),
                this.f.parse("p"),
                this.f.parse("~p|m"),
                this.f.parse("~m|n"),
                this.f.parse("~n"),
                this.f.parse("~m|l"),
                this.f.parse("~l")
        );
        final List<Formula> result = SmusComputation.computeSmusForFormulas(input, Collections.emptyList(), this.f);
        assertThat(result).containsExactlyInAnyOrder(this.f.parse("~s"), this.f.parse("s|~p"), this.f.parse("p"));
    }

    @Test
    public void testWithAdditionalConstraint() throws ParserException {
        final List<Formula> input = Arrays.asList(
                this.f.parse("~s"),
                this.f.parse("s|~p"),
                this.f.parse("p"),
                this.f.parse("~p|m"),
                this.f.parse("~m|n"),
                this.f.parse("~n"),
                this.f.parse("~m|l"),
                this.f.parse("~l")
        );
        final List<Formula> result = SmusComputation.computeSmusForFormulas(input, Collections.singletonList(this.f.parse("n|l")), this.f);
        assertThat(result).containsExactlyInAnyOrder(this.f.parse("~n"), this.f.parse("~l"));
    }

    @Test
    public void testSatisfiable() throws ParserException {
        final List<Formula> input = Arrays.asList(
                this.f.parse("~s"),
                this.f.parse("s|~p"),
                this.f.parse("~p|m"),
                this.f.parse("~m|n"),
                this.f.parse("~n"),
                this.f.parse("~m|l")
        );
        assertThatThrownBy(() -> SmusComputation.computeSmusForFormulas(input, Collections.singletonList(this.f.parse("n|l")), this.f))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot compute a smallest MUS for a satisfiable formula set.");
    }

    @Test
    public void testUnsatisfiableAdditionalConstraints() throws ParserException {
        final List<Formula> input = Arrays.asList(
                this.f.parse("~s"),
                this.f.parse("s|~p"),
                this.f.parse("~p|m"),
                this.f.parse("~m|n"),
                this.f.parse("~n|s")
        );
        assertThatThrownBy(() -> SmusComputation.computeSmusForFormulas(input, Arrays.asList(this.f.parse("~a&b"), this.f.parse("a|~b")), this.f))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot compute a smallest MUS for a set of unsatisfiable additional constraints.");
    }

    @Test
    public void testTrivialUnsatFormula() throws ParserException {
        final List<Formula> input = Arrays.asList(
                this.f.parse("~s"),
                this.f.parse("s|~p"),
                this.f.parse("p"),
                this.f.parse("~p|m"),
                this.f.parse("~m|n"),
                this.f.parse("~n"),
                this.f.parse("~m|l"),
                this.f.parse("~l"),
                this.f.parse("a&~a")
        );
        final List<Formula> result = SmusComputation.computeSmusForFormulas(input, Collections.singletonList(this.f.parse("n|l")), this.f);
        assertThat(result).containsExactly(this.f.falsum());
    }

    @Test
    public void testUnsatFormula() throws ParserException {
        final List<Formula> input = Arrays.asList(
                this.f.parse("~s"),
                this.f.parse("s|~p"),
                this.f.parse("p"),
                this.f.parse("~p|m"),
                this.f.parse("~m|n"),
                this.f.parse("~n"),
                this.f.parse("~m|l"),
                this.f.parse("~l"),
                this.f.parse("(a<=>b)&(~a<=>b)")
        );
        final List<Formula> result = SmusComputation.computeSmusForFormulas(input, Collections.singletonList(this.f.parse("n|l")), this.f);
        assertThat(result).containsExactly(this.f.parse("(a<=>b)&(~a<=>b)"));
    }

    @Test
    public void testShorterConflict() throws ParserException {
        final List<Formula> input = Arrays.asList(
                this.f.parse("~s"),
                this.f.parse("s|~p"),
                this.f.parse("p"),
                this.f.parse("p&~s"),
                this.f.parse("~p|m"),
                this.f.parse("~m|n"),
                this.f.parse("~n"),
                this.f.parse("~m|l"),
                this.f.parse("~l")
        );
        final List<Formula> result = SmusComputation.computeSmusForFormulas(input, Collections.emptyList(), this.f);
        assertThat(result).containsExactlyInAnyOrder(this.f.parse("s|~p"), this.f.parse("p&~s"));
    }

    @Test
    public void testCompleteConflict() throws ParserException {
        final List<Formula> input = Arrays.asList(
                this.f.parse("~s"),
                this.f.parse("s|~p"),
                this.f.parse("p|~m"),
                this.f.parse("m|~n"),
                this.f.parse("n|~l"),
                this.f.parse("l|s")
        );
        final List<Formula> result = SmusComputation.computeSmusForFormulas(input, Collections.emptyList(), this.f);
        assertThat(result).containsExactlyInAnyOrderElementsOf(input);
    }

    @Test
    public void testLongConflictWithShortcut() throws ParserException {
        final List<Formula> input = Arrays.asList(
                this.f.parse("~s"),
                this.f.parse("s|~p"),
                this.f.parse("p|~m"),
                this.f.parse("m|~n"),
                this.f.parse("n|~l"),
                this.f.parse("l|s"),
                this.f.parse("n|s")
        );
        final List<Formula> result = SmusComputation.computeSmusForFormulas(input, Collections.emptyList(), this.f);
        assertThat(result).containsExactlyInAnyOrder(this.f.parse("~s"),
                this.f.parse("s|~p"),
                this.f.parse("p|~m"),
                this.f.parse("m|~n"),
                this.f.parse("n|s"));
    }

    @Test
    public void testManyConflicts() throws ParserException {
        final List<Formula> input = Arrays.asList(
                this.f.parse("a"),
                this.f.parse("~a|b"),
                this.f.parse("~b|c"),
                this.f.parse("~c|~a"),
                this.f.parse("a1"),
                this.f.parse("~a1|b1"),
                this.f.parse("~b1|c1"),
                this.f.parse("~c1|~a1"),
                this.f.parse("a2"),
                this.f.parse("~a2|b2"),
                this.f.parse("~b2|c2"),
                this.f.parse("~c2|~a2"),
                this.f.parse("a3"),
                this.f.parse("~a3|b3"),
                this.f.parse("~b3|c3"),
                this.f.parse("~c3|~a3"),
                this.f.parse("a1|a2|a3|a4|b1|x|y"),
                this.f.parse("x&~y"),
                this.f.parse("x=>y")
        );
        final List<Formula> result = SmusComputation.computeSmusForFormulas(input, Collections.emptyList(), this.f);
        assertThat(result).containsExactlyInAnyOrder(this.f.parse("x&~y"), this.f.parse("x=>y"));
    }

    @Test
    public void testCustomTimeoutMhsSmusHandlerLarge() throws ParserException {
//        final CustomTimeoutMhsSmusHandler handler = new CustomTimeoutMhsSmusHandler(3L, 0L);
//        final List<Formula> input = Arrays.asList(
//                this.f.parse("a"),
//                this.f.parse("~a|b"),
//                this.f.parse("~b|c"),
//                this.f.parse("~c|~a"),
//                this.f.parse("a1"),
//                this.f.parse("~a1|b1"),
//                this.f.parse("~b1|c1"),
//                this.f.parse("~c1|~a1"),
//                this.f.parse("a2"),
//                this.f.parse("~a2|b2"),
//                this.f.parse("~b2|c2"),
//                this.f.parse("~c2|~a2"),
//                this.f.parse("a3"),
//                this.f.parse("~a3|b3"),
//                this.f.parse("~b3|c3"),
//                this.f.parse("~c3|~a3"),
//                this.f.parse("a1|a2|a3|a4|b1|x|y"),
//                this.f.parse("x&~y"),
//                this.f.parse("x=>y")
//        );
//        final List<Formula> smus = SmusComputation.computeSmusForFormulas(handler, input, Collections.emptyList(), this.f);
//        assertThat(handler.aborted()).isTrue();
//        assertThat(smus).isNull();
    }

    @Test
    public void testCustomTimeoutMcsSmusHandlerLarge() throws ParserException {
//        final CustomTimeoutMcsSmusHandler handler = new CustomTimeoutMcsSmusHandler(3L, 0L);
//        final List<Formula> input = Arrays.asList(
//                this.f.parse("a"),
//                this.f.parse("~a|b"),
//                this.f.parse("~b|c"),
//                this.f.parse("~c|~a"),
//                this.f.parse("a1"),
//                this.f.parse("~a1|b1"),
//                this.f.parse("~b1|c1"),
//                this.f.parse("~c1|~a1"),
//                this.f.parse("a2"),
//                this.f.parse("~a2|b2"),
//                this.f.parse("~b2|c2"),
//                this.f.parse("~c2|~a2"),
//                this.f.parse("a3"),
//                this.f.parse("~a3|b3"),
//                this.f.parse("~b3|c3"),
//                this.f.parse("~c3|~a3"),
//                this.f.parse("a1|a2|a3|a4|b1|x|y"),
//                this.f.parse("x&~y"),
//                this.f.parse("x=>y")
//        );
//        final List<Formula> smus = SmusComputation.computeSmusForFormulas(handler, input, Collections.emptyList(), this.f);
//        assertThat(handler.aborted()).isTrue();
//        assertThat(smus).isNull();
    }

    @Test
    public void testCustomSmusHandler01() throws ParserException {
        // Lower bound threshold exceeds
//        final SmusHandler handler = new CustomSmusHandler(2, 10, 10);
//        final List<Formula> input = Arrays.asList(
//                this.f.parse("s"),
//                this.f.parse("~s|p"),
//                this.f.parse("~p|m"),
//                this.f.parse("~m|~s"),
//                this.f.parse("s|n"),
//                this.f.parse("~m|l"),
//                this.f.parse("~l")
//        );
//        final List<Formula> smus = SmusComputation.computeSmusForFormulas(handler, input, Collections.emptyList(), this.f);
//        assertThat(handler.aborted()).isTrue();
//        assertThat(smus).isNull();
    }

    @Test
    public void testCustomSmusHandler02() throws ParserException {
        // Minimal hitting set threshold exceeds
//        final SmusHandler handler = new CustomSmusHandler(10, 2, 10);
//        final List<Formula> input = Arrays.asList(
//                this.f.parse("s"),
//                this.f.parse("~s|p"),
//                this.f.parse("~p|m"),
//                this.f.parse("~m|~s"),
//                this.f.parse("s|n"),
//                this.f.parse("~m|l"),
//                this.f.parse("~l")
//        );
//        final List<Formula> smus = SmusComputation.computeSmusForFormulas(handler, input, Collections.emptyList(), this.f);
//        assertThat(handler.aborted()).isTrue();
//        assertThat(smus).isNull();
    }

    @Test
    public void testCustomSmusHandler03() throws ParserException {
        // Minimal correction set threshold exceeds
//        final SmusHandler handler = new CustomSmusHandler(10, 10, 2);
//        final List<Formula> input = Arrays.asList(
//                this.f.parse("s"),
//                this.f.parse("~s|p"),
//                this.f.parse("~p|m"),
//                this.f.parse("~m|~s"),
//                this.f.parse("s|n"),
//                this.f.parse("~m|l"),
//                this.f.parse("~l")
//        );
//        final List<Formula> smus = SmusComputation.computeSmusForFormulas(handler, input, Collections.emptyList(), this.f);
//        assertThat(handler.aborted()).isTrue();
//        assertThat(smus).isNull();
    }

//    static class CustomSmusHandler extends ComputationHandler implements SmusHandler {
//        private final int lowerBoundThreshold;
//        private final int mhsThreshold;
//        private final int mcsThreshold;
//
//        private int numHs;
//        private int numMcs;
//
//        public CustomSmusHandler(final int lowerBoundThreshold, final int mhsThreshold, final int mcsThreshold) {
//            this.lowerBoundThreshold = lowerBoundThreshold;
//            this.mhsThreshold = mhsThreshold;
//            this.mcsThreshold = mcsThreshold;
//        }
//
//        @Override
//        public boolean foundLowerBound(final int lowerBound) {
//            this.aborted = lowerBound >= this.lowerBoundThreshold;
//            return !this.aborted;
//        }
//
//        @Override
//        public boolean computedMinimalHittingSet() {
//            this.aborted = ++this.numHs >= this.mhsThreshold;
//            return !this.aborted;
//        }
//
//        @Override
//        public boolean computedMinimalCorrectionSet() {
//            this.aborted = ++this.numMcs >= this.mcsThreshold;
//            return !this.aborted;
//        }
//
//        @Override
//        public void started() {
//            super.started();
//            this.numHs = 0;
//            this.numMcs = 0;
//        }
//    }

    // Timeout handler with a timeout only for the minimal hitting set optimization
//    static class CustomTimeoutMhsSmusHandler extends TimeoutHandler implements SmusHandler {
//        private TimeoutOptimizationHandler optimizationHandler;
//
//        public CustomTimeoutMhsSmusHandler(final long timeout, final long designatedEnd) {
//            super(timeout, designatedEnd);
//        }
//
//        @Override
//        public boolean aborted() {
//            return super.aborted() || this.optimizationHandler != null && this.optimizationHandler.aborted();
//        }
//
//        @Override
//        public void started() {
//            super.started();
//            this.optimizationHandler = new TimeoutOptimizationHandler(-1, this.designatedEnd);
//        }
//
//        @Override
//        public boolean foundLowerBound(final int lowerBound) {
//            return true;
//        }
//
//        @Override
//        public boolean computedMinimalHittingSet() {
//            return true;
//        }
//
//        @Override
//        public boolean computedMinimalCorrectionSet() {
//            return true;
//        }
//
//        @Override
//        public OptimizationHandler minimalHittingSetOptimizationHandler() {
//            return this.optimizationHandler;
//        }
//
//        @Override
//        public OptimizationHandler minimalCorrectionSetOptimizationHandler() {
//            return null;
//        }
//    }

    // Timeout handler with a timeout only for the minimal correction set optimization
//    static class CustomTimeoutMcsSmusHandler extends TimeoutHandler implements SmusHandler {
//        private TimeoutOptimizationHandler optimizationHandler;
//
//        public CustomTimeoutMcsSmusHandler(final long timeout, final long designatedEnd) {
//            super(timeout, designatedEnd);
//        }
//
//        @Override
//        public boolean aborted() {
//            return super.aborted() || this.optimizationHandler != null && this.optimizationHandler.aborted();
//        }
//
//        @Override
//        public void started() {
//            super.started();
//            this.optimizationHandler = new TimeoutOptimizationHandler(-1, this.designatedEnd);
//        }
//
//        @Override
//        public boolean foundLowerBound(final int lowerBound) {
//            return true;
//        }
//
//        @Override
//        public boolean computedMinimalHittingSet() {
//            return true;
//        }
//
//        @Override
//        public boolean computedMinimalCorrectionSet() {
//            return true;
//        }
//
//        @Override
//        public OptimizationHandler minimalHittingSetOptimizationHandler() {
//            return null;
//        }
//
//        @Override
//        public OptimizationHandler minimalCorrectionSetOptimizationHandler() {
//            return this.optimizationHandler;
//        }
//    }
}
