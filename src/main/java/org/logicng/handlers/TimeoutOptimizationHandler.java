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

import java.util.function.Supplier;

/**
 * A optimization handler which cancels the computation process after a given timeout.
 * @version 2.1.0
 * @since 2.1.0
 */
public class TimeoutOptimizationHandler extends TimeoutHandler implements OptimizationHandler {

    private TimeoutSATHandler satHandler;
    private Supplier<Assignment> lastModelProvider;

    /**
     * Constructs a new instance with a given timeout or designated end in milliseconds.
     * If designated end is &gt; 0, the timeout will be ignored and multiple calls to {@link #started()}
     * will not change the time limit.
     * If designated end is &lt;= 0, the time limit of this handler will be reset to {@code System.currentTimeMillis() + timeout}
     * on every call to {@link #started()}.
     * <p>
     * Note that it might take a few milliseconds more until the computation is actually
     * canceled, since the handler depends on the next found model.
     * @param timeout the timeout in milliseconds, ignored if designated end is &gt; 0
     * @param type    the type of the timer, must not be {@code null}
     */
    public TimeoutOptimizationHandler(final long timeout, final TimerType type) {
        super(timeout, type);
    }

    public TimeoutOptimizationHandler(final long timeout) {
        super(timeout);
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
    public boolean aborted() {
        return super.aborted() || Handler.aborted(this.satHandler);
    }

    @Override
    public void started() {
        super.started();
        if (this.satHandler == null || this.type == TimerType.RESTARTING_TIMEOUT) {
            this.satHandler = new TimeoutSATHandler(this.designatedEnd, TimerType.FIXED_END);
        }
        this.lastModelProvider = null;
    }

    @Override
    public boolean foundBetterBound(final Supplier<Assignment> lastModelProvider) {
        this.lastModelProvider = lastModelProvider;
        return !timeLimitExceeded();
    }

    /**
     * Returns the latest intermediate result of the optimization or {@code null} if no
     * such result was yet computed.
     * @return the latest intermediate result
     */
    public Assignment getIntermediateResult() {
        return this.lastModelProvider == null ? null : this.lastModelProvider.get();
    }
}
