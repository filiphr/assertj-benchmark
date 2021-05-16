/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.benchmark;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.JavaFlightRecorderProfiler;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * @author Filip Hrisafov
 */
@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ObjectAssertBenchmark {

    @Param({"long", "objectArray", "booleanArray"})
    private String objectType;

    @Param({"default", "soft"})
    private String assertType;

    protected Object expectedEqualTo = 10L;
    protected Object expectedNotEqualTo = 8L;

    protected AbstractObjectAssert<?, ?> objectAssert;

    @Setup(Level.Trial)
    public void setup() {
        Object actual;
        switch (objectType) {
            case "long":
                actual = 10L;
                expectedEqualTo = 10L;
                expectedNotEqualTo = 8L;
                break;
            case "objectArray":
                actual = new String[] { "Yoda", "Luke" };
                expectedEqualTo = new String[] { "Yoda", "Luke" };
                expectedNotEqualTo = new String[] { "Yoda", "Vader" };
                break;
            case "booleanArray":
                actual = new boolean[] { true, false };
                expectedEqualTo = new boolean[] { true, false };
                expectedNotEqualTo = new boolean[] { true, true };
                break;
            default:
                throw new IllegalStateException("Unknown object type: " + objectType);
        }

        switch (assertType) {
            case "default":
                objectAssert = Assertions.assertThat(actual);
                break;
            case "soft":
                objectAssert = new SoftAssertions().assertThat(actual);
                break;
            default:
                throw new IllegalStateException("Unknown assert type: " + assertType);
        }
    }

    @Benchmark
    public void isEqualTo() {
        objectAssert.isEqualTo(expectedEqualTo);
    }

    @Benchmark
    public void isNotEqualTo() {
        objectAssert.isNotEqualTo(expectedNotEqualTo);
    }

    public static void main(String... args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(ObjectAssertBenchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(1))
                //.addProfiler(JavaFlightRecorderProfiler.class, "dir=./jfr")
                //.addProfiler(StackProfiler.class, "lines=10")
                .jvmArgs("-server")
                .forks(1)
                .resultFormat(ResultFormatType.CSV)
                .build();

        Collection<RunResult> results = new Runner(opts).run();
        for (RunResult result : results) {
            Result<?> r = result.getPrimaryResult();
            System.out.println("API replied benchmark score: "
                    + r.getScore() + " "
                    + r.getScoreUnit() + " over "
                    + r.getStatistics().getN() + " iterations");
        }
    }

}
