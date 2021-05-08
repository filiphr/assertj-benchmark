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

import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
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
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
public class ObjectAssertBenchmark {

    @Param({"default", "soft"})
    private String type;

    protected Long actual = 10L;
    protected Long expectedEqualTo = 10L;
    protected Long expectedNotEqualTo = 8L;

    protected AbstractLongAssert<?> longAssert;

    @Setup(Level.Trial)
    public void setup() {
        switch (type) {
            case "default":
                longAssert = Assertions.assertThat(actual);
                break;
            case "soft":
                longAssert = new SoftAssertions().assertThat(actual);
                break;
            default:
                throw new IllegalStateException("Unknown type: " + type);
        }
    }

    @Benchmark
    public AbstractLongAssert<?> baseline() {
        return longAssert;
    }

    @Benchmark
    public AbstractLongAssert<?> isEqualTo() {
        return longAssert.isEqualTo(expectedEqualTo);
    }

    @Benchmark
    public AbstractLongAssert<?> isNotEqualTo() {
        return longAssert.isNotEqualTo(expectedNotEqualTo);
    }

    public static void main(String... args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(ObjectAssertBenchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(1))
                .jvmArgs("-server")
                .forks(1)
                .resultFormat(ResultFormatType.TEXT)
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
