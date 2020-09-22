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

    protected final long timeout;
    protected long designatedEnd;

    /**
     * Constructs a new abstract timeout handler with a given timeout or designated end in milliseconds.
     * If designated end is &gt; 0, the timeout will be ignored and multiple calls to {@link #started()}
     * will not change the time limit.
     * If designated end is &lt;= 0, the time limit of this handler will be reset to {@code System.currentTimeMillies() + timeout}
     * on every call to {@link #started()}.
     * @param timeout       the timeout in milliseconds, ignored if designated end is &gt; 0
     * @param designatedEnd the designated end time in milliseconds (definition as in {@link System#currentTimeMillis()})
     */
    public TimeoutHandler(final long timeout, final long designatedEnd) {
        this.timeout = designatedEnd > 0 ? -1 : timeout;
        this.designatedEnd = designatedEnd;
    }

    @Override
    public void started() {
        super.started();
        if (this.timeout > 0) {
            final long start = System.currentTimeMillis();
            this.designatedEnd = start + this.timeout;
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
}
