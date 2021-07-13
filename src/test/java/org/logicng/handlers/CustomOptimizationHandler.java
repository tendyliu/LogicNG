package org.logicng.handlers;

import org.logicng.datastructures.Assignment;

import java.util.function.Supplier;

/**
 * Custom optimization handler for testing purposes.
 * <p>
 * The handler aborts the optimization if a certain number of found bounds is reached.
 * @version 2.1.0
 * @since 2.1.0
 */
public class CustomOptimizationHandler implements OptimizationHandler {
    private final int foundBoundsLimit;
    private int numFoundBounds;
    private boolean aborted;

    /**
     * Constructs a new instance with the given bound limit.
     * @param foundBoundsLimit the found bounds limit
     */
    public CustomOptimizationHandler(final int foundBoundsLimit) {
        this.foundBoundsLimit = foundBoundsLimit;
        this.numFoundBounds = 0;
    }

    @Override
    public SATHandler satHandler() {
        return null;
    }

    @Override
    public boolean aborted() {
        return this.aborted;
    }

    @Override
    public boolean foundBetterBound(final Supplier<Assignment> currentResultProvider) {
        this.aborted = ++numFoundBounds >= foundBoundsLimit;
        return !this.aborted;
    }
}
