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

import org.logicng.datastructures.Assignment;

/**
 * A MaxSAT handler which cancels the solving process after a given timeout.
 * @version 2.1.0
 * @since 1.0
 */
public final class TimeoutMaxSATHandler extends TimeoutHandler implements MaxSATHandler {

    private TimeoutSATHandler satHandler;
    private int currentLb;
    private int currentUb;

    /**
     * Constructs a new instance with a given timeout or designated end in milliseconds.
     * If designated end is &gt; 0, the timeout will be ignored and multiple calls to {@link #started()}
     * will not change the time limit.
     * If designated end is &lt;= 0, the time limit of this handler will be reset to {@code System.currentTimeMillies() + timeout}
     * on every call to {@link #started()}.
     * <p>
     * Note that it might take a few milliseconds more until the solver is actually canceled,
     * since the handler depends on the solvers call to {@code foundApproximation()} or {@link SATHandler#detectedConflict()}.
     * @param timeout       the timeout in milliseconds, ignored if designated end is &gt; 0
     * @param designatedEnd the designated end time in milliseconds (definition as in {@link System#currentTimeMillis()})
     */
    public TimeoutMaxSATHandler(final long timeout, final long designatedEnd) {
        super(timeout, designatedEnd);
        this.currentLb = -1;
        this.currentUb = -1;
    }

    @Override
    public void started() {
        super.started();
        this.satHandler = new TimeoutSATHandler(-1, this.designatedEnd);
        this.currentLb = -1;
        this.currentUb = -1;
    }

    @Override
    public boolean aborted() {
        return super.aborted() || this.satHandler != null && this.satHandler.aborted();
    }

    /**
     * Returns a SAT handler which can be used to cancel internal SAT calls of the model enumeration process.
     * Note that this handler will only be available after the first call to {@link #started()}.
     * @return the SAT handler
     */
    @Override
    public SATHandler satHandler() {
        return this.satHandler;
    }

    @Override
    public boolean foundLowerBound(final int lowerBound, final Assignment model) {
        this.currentLb = lowerBound;
        return timeLimitExceeded();
    }

    @Override
    public boolean foundUpperBound(final int upperBound, final Assignment model) {
        this.currentUb = upperBound;
        return timeLimitExceeded();
    }

    @Override
    public void finishedSolving() {
        // nothing to do here
    }

    @Override
    public int lowerBoundApproximation() {
        return this.currentLb;
    }

    @Override
    public int upperBoundApproximation() {
        return this.currentUb;
    }
}
