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

package org.logicng.bdds.datastructures;

import org.logicng.formulas.Variable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A node in a BDD.
 * @version 1.4.0
 * @since 1.4.0
 */
public final class BDDInnerNode implements BDDNode {

  private final Variable var;
  private final BDDNode low;
  private final BDDNode high;

  /**
   * Constructor for a new inner BDD node holding a variable.
   * @param var  the variable
   * @param low  the low child node
   * @param high the high child node
   */
  public BDDInnerNode(final Variable var, final BDDNode low, final BDDNode high) {
    this.var = var;
    this.low = low;
    this.high = high;
  }

  @Override
  public Variable label() {
    return this.var;
  }

  @Override
  public boolean isInnerNode() {
    return true;
  }

  @Override
  public BDDNode low() {
    return this.low;
  }

  @Override
  public BDDNode high() {
    return this.high;
  }

  @Override
  public Set<BDDNode> nodes() {
    final Set<BDDNode> res = new HashSet<BDDNode>(Collections.singleton(this));
    res.addAll(this.low.nodes());
    res.addAll(this.high.nodes());
    return res;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final BDDInnerNode that = (BDDInnerNode) o;
    if (this.var != null ? !this.var.equals(that.var) : that.var != null) return false;
    if (this.low != null ? !this.low.equals(that.low) : that.low != null) return false;
    return this.high != null ? this.high.equals(that.high) : that.high == null;
  }

  @Override
  public int hashCode() {
    int result = this.var != null ? this.var.hashCode() : 0;
    result = 31 * result + (this.low != null ? this.low.hashCode() : 0);
    result = 31 * result + (this.high != null ? this.high.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "<" + this.var + " | low=" + this.low + " high=" + this.high + ">";
  }
}
