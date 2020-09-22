package org.logicng.handlers;

import org.logicng.datastructures.Assignment;

import java.util.function.Supplier;

/**
 * Interface for an optimization handler.
 * @version 2.1.0
 * @since 2.1.0
 */
public interface OptimizationHandler extends Handler {

    /**
     * Returns a SAT handler which can be used to cancel internal SAT calls of the optimization function.
     * @return a SAT handler
     */
    SATHandler satHandler();

    @Override
    default boolean aborted() {
        return satHandler() != null && satHandler().aborted();
    }

    /**
     * This method is called when the solver found a better bound for the optimization.
     * @param currentResultProvider a provider for the current result, can be used to examine
     *                              the current result or to use this result if the
     *                              optimization should be aborted
     * @return whether the optimization process should be continued or not
     */
    default boolean foundBetterBound(final Supplier<Assignment> currentResultProvider) {
        return true;
    }
}
