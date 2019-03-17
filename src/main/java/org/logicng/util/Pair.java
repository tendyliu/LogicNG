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

package org.logicng.util;

/**
 * Data structure for a pair.
 * @param <A> the type parameter of the first entry
 * @param <B> the type parameter of the second entry
 * @version 1.2
 * @since 1.0
 */
public class Pair<A, B> {

  protected final A a;
  protected final B b;

  /**
   * Constructs a new pair.
   * @param a the first entry
   * @param b the second entry
   */
  public Pair(final A a, final B b) {
    this.a = a;
    this.b = b;
  }

  /**
   * Returns the first entry of this pair.
   * @return the first entry
   */
  public A first() {
    return a;
  }

  /**
   * Returns the second entry of this pair.
   * @return the second entry
   */
  public B second() {
    return b;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Pair))
      return false;
    Pair<?, ?> pair = (Pair<?, ?>) o;
    if (a != null ? !a.equals(pair.a) : pair.a != null)
      return false;
    return b != null ? b.equals(pair.b) : pair.b == null;
  }

  @Override
  public int hashCode() {
    int result = a != null ? a.hashCode() : 0;
    result = 31 * result + (b != null ? b.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return String.format("<%s, %s>", a, b);
  }
}
