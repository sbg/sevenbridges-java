/*
 * Copyright 2017 Seven Bridges Genomics, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sevenbridges.apiclient.impl.transfer.atomic;

import java.io.Serializable;

/**
 * One or more variables that together maintain an initially zero {@code long} sum. Value is updated
 * using {@link #set} method, and the set of variables will grow dynamically to accommodate all
 * stripes of the {@code long} value. Maximum number of stripes is {@link Integer#MAX_VALUE}. Method
 * {@link #sum} (or, equivalently, {@link #longValue}) returns the current total combined across the
 * variables maintaining the sum.
 * <p>
 * This class extends {@link Number}, but does <em>not</em> define methods such as {@code equals},
 * {@code hashCode} and {@code compareTo} because instances are expected to be mutated, and so are
 * not useful as collection keys.
 */
public class StripedLongAdder extends Striped64 implements Serializable {

  private static final long serialVersionUID = 2963908554011144983L;

  /**
   * Version of new value wins for use in retryUpdate.
   */
  long fn(long currentValue, long newValue) {
    return newValue;
  }

  /**
   * Creates a new striped setter with initial sum of zero.
   */
  public StripedLongAdder() {
  }

  /**
   * Sets the given value.
   *
   * @param s the cell stripe in which to set value
   * @param x the value to set
   */
  public void set(int s, long x) {
    Cell[] as;
    Cell a;
    if ((as = cells) != null) {
      if (as.length < s + 1 ||
          (a = as[s]) == null ||
          !(a.cas(a.value, x))) {
        retryUpdate(x, s);
      }
    } else {
      retryUpdate(x, s);
    }
  }

  /**
   * Handles cases of updates involving initialization, resizing, creating new Cells, and/or
   * contention. See above for explanation. This method suffers the usual non-modularity problems
   * of optimistic retry code, relying on rechecked sets of reads.
   *
   * @param x the value
   * @param s the cell stripe in which to set value
   */
  final void retryUpdate(long x, int s) {
    boolean collide = false;                // True if last slot nonempty
    for (; ; ) {
      Cell[] as;
      Cell a = null;
      int n;
      long v;
      if ((as = cells) != null && (n = as.length) > 0) {
        if (n > s && (a = as[s]) == null) {
          if (busy == 0) {            // Try to attach new Cell
            Cell r = new Cell(x);   // Optimistically create
            if (busy == 0 && casBusy()) {
              boolean created = false;
              try {               // Recheck under lock
                Cell[] rs;
                if ((rs = cells) != null &&
                    rs.length > 0 &&
                    rs[s] == null) {
                  rs[s] = r;
                  created = true;
                }
              } finally {
                busy = 0;
              }
              if (created) {
                break;
              }
              continue;           // Slot is now non-empty
            }
          }
          collide = false;
        } else if (a != null && a.cas(v = a.value, fn(v, x))) {
          break;
        } else if (cells != as) {
          collide = false;            // At max size or stale
        } else if (!collide) {
          collide = true;
        } else if (busy == 0 && casBusy()) {
          try {
            if (cells == as) {      // Expand table unless stale
              Cell[] rs = new Cell[s + 1];
              for (int i = 0; i < n; ++i) {
                rs[i] = as[i];
              }
              cells = rs;
            }
          } finally {
            busy = 0;
          }
          collide = false;
          continue;                   // Retry with expanded table
        }
      } else if (busy == 0 && cells == as && casBusy()) {
        boolean init = false;
        try {                           // Initialize table
          if (cells == as) {
            Cell[] rs = new Cell[s + 1];
            rs[s] = new Cell(x);
            cells = rs;
            init = true;
          }
        } finally {
          busy = 0;
        }
        if (init) {
          break;
        }
      }
    }
  }

  /**
   * Returns the current sum.  The returned value is <em>NOT</em> an atomic snapshot; invocation
   * in the absence of concurrent updates returns an accurate result, but concurrent updates that
   * occur while the sum is being calculated might not be incorporated.
   *
   * @return the sum
   */
  public long sum() {
    long sum = base;
    Cell[] as = cells;
    if (as != null) {
      int n = as.length;
      for (int i = 0; i < n; ++i) {
        Cell a = as[i];
        if (a != null) {
          sum += a.value;
        }
      }
    }
    return sum;
  }

  /**
   * Resets variables maintaining the sum to zero.  This method may be a useful alternative to
   * creating a new adder, but is only effective if there are no concurrent updates.  Because this
   * method is intrinsically racy, it should only be used when it is known that no threads are
   * concurrently updating.
   */
  public void reset() {
    internalReset(0L);
  }

  /**
   * Returns the String representation of the {@link #sum}.
   *
   * @return the String representation of the {@link #sum}
   */
  public String toString() {
    return Long.toString(sum());
  }

  /**
   * Equivalent to {@link #sum}.
   *
   * @return the sum
   */
  public long longValue() {
    return sum();
  }

  /**
   * Returns the {@link #sum} as an {@code int} after a narrowing primitive conversion.
   */
  public int intValue() {
    return (int) sum();
  }

  /**
   * Returns the {@link #sum} as a {@code float} after a widening primitive conversion.
   */
  public float floatValue() {
    return (float) sum();
  }

  /**
   * Returns the {@link #sum} as a {@code double} after a widening primitive conversion.
   */
  public double doubleValue() {
    return (double) sum();
  }

  private void writeObject(java.io.ObjectOutputStream s)
      throws java.io.IOException {
    s.defaultWriteObject();
    Cell[] as;
    if ((as = cells) != null) {
      s.writeInt(as.length);
      for (Cell a : as) {
        if (a != null) {
          s.writeLong(a.value);
        } else {
          s.writeLong(0);
        }
      }
    } else {
      s.writeInt(0);
    }
    s.writeLong(base);
  }

  private void readObject(java.io.ObjectInputStream s)
      throws java.io.IOException, ClassNotFoundException {
    s.defaultReadObject();
    busy = 0;
    int num = s.readInt();
    Cell[] rs = null;
    if (num > 0) {
      rs = new Cell[num];
      long v;
      for (int i = 0; i < num; i++) {
        v = s.readLong();
        if (v > 0) {
          rs[i] = new Cell(v);
        }
      }
    }
    cells = rs;
    base = s.readLong();
  }
}
