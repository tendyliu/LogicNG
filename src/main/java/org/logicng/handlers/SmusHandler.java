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
 * Interface for a handler for the smallest MUS computation.
 * @version 2.1.0
 * @since 2.1.0
 */
public interface SmusHandler extends Handler {

    /**
     * This method is called when the SMUS procedure found a new lower bound for the smallest MUS.
     * @param lowerBound the found lower bound
     * @return whether the search should be continued or not
     */
    boolean foundLowerBound(int lowerBound);

    /**
     * This method is called when the SMUS procedure computed the next minimal hitting set.
     * @return whether the search should be continued or not
     */
    default boolean computedMinimalHittingSet() {
        return true;
    }

    /**
     * This method is called when the SMUS procedure computed the next minimal correction set.
     * @return whether the search should be continued or not
     */
    default boolean computedMinimalCorrectionSet() {
        return true;
    }

    /**
     * Returns an optimization handler which can be used to cancel internal minimal hitting set computation.
     * @return an optimization handler
     */
    default OptimizationHandler minimalHittingSetOptimizationHandler() {
        return null;
    }

    /**
     * Returns an optimization handler which can be used to cancel internal minimal correction set computation.
     * @return an optimization handler
     */
    default OptimizationHandler minimalCorrectionSetOptimizationHandler() {
        return null;
    }
}