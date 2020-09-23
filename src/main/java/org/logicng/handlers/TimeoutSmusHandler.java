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

package org.logicng.handlers;

/**
 * A SMUS handler which cancels the solving process after a given timeout.
 * @version 2.1.0
 * @since 2.1.0
 */
public final class TimeoutSmusHandler extends TimeoutHandler implements SmusHandler {

    private TimeoutOptimizationHandler optimizationHandler;

    /**
     * Constructs a new instance with a given timeout or designated end in milliseconds.
     * If designated end is &gt; 0, the timeout will be ignored and multiple calls to {@link #started()}
     * will not change the time limit.
     * If designated end is &lt;= 0, the time limit of this handler will be reset to {@code System.currentTimeMillies() + timeout}
     * on every call to {@link #started()}.
     * <p>
     * Note that it might take a few milliseconds more until the sat solver is actually
     * canceled, since the handler depends on the solvers call to {@code detectedConflict()}.
     * @param timeout       the timeout in milliseconds, ignored if designated end is &gt; 0
     * @param designatedEnd the designated end time in milliseconds (definition as in {@link System#currentTimeMillis()})
     */
    public TimeoutSmusHandler(final long timeout, final long designatedEnd) {
        super(timeout, designatedEnd);
    }

    @Override
    public boolean aborted() {
        return super.aborted() || this.optimizationHandler != null && this.optimizationHandler.aborted();
    }

    @Override
    public void started() {
        super.started();
        this.optimizationHandler = new TimeoutOptimizationHandler(-1, this.designatedEnd);
    }

    @Override
    public boolean foundLowerBound(final int lowerBound) {
        return timeLimitExceeded();
    }

    @Override
    public boolean computedMinimalHittingSet() {
        return timeLimitExceeded();
    }

    @Override
    public boolean computedMinimalCorrectionSet() {
        return timeLimitExceeded();
    }

    @Override
    public OptimizationHandler minimalHittingSetOptimizationHandler() {
        return optimizationHandler;
    }

    @Override
    public OptimizationHandler minimalCorrectionSetOptimizationHandler() {
        return optimizationHandler;
    }
}
