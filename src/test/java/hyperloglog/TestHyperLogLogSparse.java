/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hyperloglog;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class TestHyperLogLogSparse {

  // 5% tolerance for long range bias and 0.5% for short range bias
  private float longRangeTolerance = 5.0f;
  private float shortRangeTolerance = 0.5f;

  private int size;

  public TestHyperLogLogSparse(int n) {
    this.size = n;
  }

  @Parameters
  public static Collection<Object[]> data() {
    Object[][] data = new Object[][] { { 2 }, { 10 }, { 100 }, { 1000 }, { 10000 }, { 100000 },
        { 1000000 } };
    return Arrays.asList(data);
  }

  @Test
  public void testHLLAdd() {
    Random rand = new Random(size);
    HyperLogLog hll = HyperLogLog.builder().build();
    int size = 100;
    for (int i = 0; i < size; i++) {
      hll.addLong(rand.nextLong());
    }
    double threshold = size > 40000 ? longRangeTolerance : shortRangeTolerance;
    double delta = threshold * size / 100;
    assertEquals((double) size, (double) hll.count(), delta);
  }

  @Test
  public void testHLLAddHalfDistinct() {
    Random rand = new Random(size);
    HyperLogLog hll = HyperLogLog.builder().build();
    int unique = size / 2;
    Set<Long> hashset = new HashSet<Long>();
    for (int i = 0; i < size; i++) {
      long val = rand.nextInt(unique);
      hashset.add(val);
      hll.addLong(val);
    }
    double threshold = size > 40000 ? longRangeTolerance : shortRangeTolerance;
    double delta = threshold * hashset.size() / 100;
    assertEquals((double) hashset.size(), (double) hll.count(), delta);
  }
}
