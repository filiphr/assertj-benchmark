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

This will generate the results in a csv file called results.csv.

You can plot this in an image using `gnuplot` by executing `gnuplot benchmark.plt`. This will output `results.png`.
You can also use `gnuplot -e "results='<result input>'" -e "output='<result output>'" benchmark.plt` to pass custom result input and custom result output.

