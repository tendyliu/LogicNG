package org.logicng.util;

import org.logicng.formulas.Formula;
import org.logicng.formulas.Literal;
import org.logicng.formulas.Variable;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A class which contains utility methods for {@link Formula} objects.
 * @version 1.5.1
 * @since 1.5.1
 */
public class FormulaHelper {

  /**
   * Private empty constructor.  Class only contains static utility methods.
   */
  private FormulaHelper() {
    // Intentionally left empty
  }

  /**
   * Returns all variables occurring in the given formulas.
   * @param formulas formulas
   * @return all variables occurring in the given formulas
   */
  public static SortedSet<Variable> variables(final Formula... formulas) {
    final SortedSet<Variable> variables = new TreeSet<Variable>();
    for (final Formula f : formulas) {
      variables.addAll(f.variables());
    }
    return variables;
  }

  /**
   * Returns all variables occurring in the given formulas.
   * @param formulas formulas
   * @return all variables occurring in the given formulas
   */
  public static SortedSet<Variable> variables(final Collection<? extends Formula> formulas) {
    final SortedSet<Variable> variables = new TreeSet<Variable>();
    for (final Formula f : formulas) {
      variables.addAll(f.variables());
    }
    return variables;
  }

  /**
   * Returns all literals occurring in the given formulas.
   * @param formulas formulas
   * @return all literals occurring in the given formulas
   */
  public static SortedSet<Literal> literals(final Formula... formulas) {
    final SortedSet<Literal> literals = new TreeSet<Literal>();
    for (final Formula f : formulas) {
      literals.addAll(f.literals());
    }
    return literals;
  }

  /**
   * Returns all literals occurring in the given formulas.
   * @param formulas formulas
   * @return all literals occurring in the given formulas
   */
  public static SortedSet<Literal> literals(final Collection<? extends Formula> formulas) {
    final SortedSet<Literal> literals = new TreeSet<Literal>();
    for (final Formula f : formulas) {
      literals.addAll(f.literals());
    }
    return literals;
  }
}
