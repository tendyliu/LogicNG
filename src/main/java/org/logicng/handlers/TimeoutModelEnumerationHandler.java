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
 * A model enumeration handler which cancels the computation process after a given timeout.
 * @version 2.1.0
 * @since 1.0
 */
public class TimeoutModelEnumerationHandler extends TimeoutHandler implements ModelEnumerationHandler {

    private TimeoutSATHandler satHandler;

    /**
     * Constructs a new instance with a given timeout in milliseconds.
     * <p>
     * Note that it might take a few milliseconds more until the computation is actually
     * canceled, since the handler depends on the next found model.
     * @param timeout the timeout in milliseconds
     */
    public TimeoutModelEnumerationHandler(final long timeout) {
        super(timeout, -1);
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
        return super.aborted() || this.satHandler != null && this.satHandler.aborted();
    }

    @Override
    public void started() {
        super.started();
        this.satHandler = new TimeoutSATHandler(-1, this.designatedEnd);
        this.satHandler.started();
    }

    @Override
    public boolean foundModel(final Assignment assignment) {
        return timeLimitExceeded();
    }
}
