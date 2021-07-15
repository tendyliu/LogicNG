package org.logicng.handlers;

import org.logicng.datastructures.Assignment;

import java.util.function.Supplier;

/**
 * Bounded optimization handler for testing purposes.
 * <p>
 * The handler aborts the optimization if a certain number of found bounds or a certain number of conflicts is reached.
 * @version 2.1.0
 * @since 2.1.0
 */
public class BoundedOptimizationHandler implements OptimizationHandler {
    private final SATHandler satHandler;
    private final int foundBoundsLimit;
    private int numFoundBounds;
    private boolean aborted;

    /**
     * Constructs a new instance with the given bound limits.
     * @param conflictsLimit   the conflicts limit for the SAT handler
     * @param foundBoundsLimit the found bounds limit, if -1 then no limit is set
     */
    public BoundedOptimizationHandler(final int conflictsLimit, final int foundBoundsLimit) {
        this.satHandler = new BoundedSatHandler(conflictsLimit);
        this.foundBoundsLimit = foundBoundsLimit;
        this.numFoundBounds = 0;
    }

    @Override
    public boolean aborted() {
        return satHandler.aborted() || this.aborted;
    }

    @Override
    public void started() {
        // do nothing
    }

    @Override
    public SATHandler satHandler() {
        return satHandler;
    }

    @Override
    public boolean foundBetterBound(final Supplier<Assignment> currentResultProvider) {
        this.aborted = foundBoundsLimit != -1 && ++numFoundBounds >= foundBoundsLimit;
        return !this.aborted;
    }
}
