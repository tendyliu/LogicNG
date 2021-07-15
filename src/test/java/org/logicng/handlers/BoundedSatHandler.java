package org.logicng.handlers;

/**
 * Bounded SAT handler for testing purposes.
 * <p>
 * The handler aborts the computation if a certain number of conflicts is reached.
 * @version 2.1.0
 * @since 2.1.0
 */
public class BoundedSatHandler implements SATHandler {
    private final int conflictsLimit;
    private int numConflicts;
    private boolean aborted;

    /**
     * Constructs a new instance with the given conflicts limit.
     * @param conflictsLimit the conflicts limit, if -1 then no limit is set
     */
    public BoundedSatHandler(final int conflictsLimit) {
        this.conflictsLimit = conflictsLimit;
        this.numConflicts = 0;
    }

    @Override
    public boolean aborted() {
        return this.aborted;
    }

    @Override
    public void started() {
        // do nothing
    }

    @Override
    public boolean detectedConflict() {
        this.aborted = conflictsLimit != -1 && ++numConflicts >= conflictsLimit;
        return !aborted;
    }
}
