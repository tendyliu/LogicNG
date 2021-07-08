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
 * An abstract timeout handler.
 * @version 2.1.0
 * @since 1.6.2
 */
public abstract class TimeoutHandler extends ComputationHandler {

    protected long timeout;
    protected final TimerType type;
    protected long designatedEnd;

    /**
     * Constructs a new abstract timeout handler with a given timeout and a timeout type.
     * @param timeout the timeout in milliseconds, its meaning is defined by the timeout type
     * @param type    the type of the timer, must not be {@code null}
     */
    public TimeoutHandler(final long timeout, final TimerType type) {
        this.type = type;
        this.timeout = type == TimerType.FIXED_END ? 0 : timeout;
        this.designatedEnd = type == TimerType.FIXED_END ? timeout : 0;
    }

    public TimeoutHandler(final long timeout) {
        this(timeout, TimerType.SINGLE_TIMEOUT);
    }

    @Override
    public void started() {
        super.started();
        if (this.type == TimerType.RESTARTING_TIMEOUT || this.designatedEnd == 0) {
            this.designatedEnd = System.currentTimeMillis() + this.timeout;
        }
    }

    /**
     * Tests if the current time exceeds the timeout limit.
     * @return {@code true} if the current time exceeds the timeout limit, otherwise {@code false}
     */
    protected boolean timeLimitExceeded() {
        this.aborted = System.currentTimeMillis() >= this.designatedEnd;
        return !this.aborted;
    }

    public enum TimerType {
        /**
         * Simple timeout which is started when {@link Handler#started()} is called.
         * <p>
         * Multiple calls to {@link Handler#started()} do not restart the timeout.
         */
        SINGLE_TIMEOUT,

        /**
         * Timeout which is restarted on every call to {@link Handler#started()}.
         */
        RESTARTING_TIMEOUT,

        /**
         * Timeout which is interpreted as fixed point in time (in milliseconds) at
         * which the computation should be aborted.
         * <p>
         * The method {@link Handler#started()} must still be called, but does not have
         * an effect on the timeout.
         */
        FIXED_END
    }
}
