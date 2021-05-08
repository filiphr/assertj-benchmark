# AssertJ Benchmark

This repo contains different benchmarks for AssertJ using [JMH](https://github.com/openjdk/jmh).

## Running the benchmark

Before running the benchmark we need to build the jar with:

```shell
./mvnw clean package
```

Once the package goal is run there will be a `benchmark.jar` created under `target`.

You can then run the benchmark with:

```shell
java -jar target/benchmark.jar -rff results.csv -rf csv
```