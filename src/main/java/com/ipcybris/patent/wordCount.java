package com.ipcybris.patent;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import java.util.Arrays;
import java.util.List;

public class wordCount {
    public static void main(String... args) {
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline pipeline = Pipeline.create(options);

        PCollection<String> lines = pipeline.apply("read from file", TextIO.read().from("C:\\Users\\techolution\\Documents\\sample.zip"));
        PCollection<List<String>> wordsperline = lines.apply(MapElements.via(new SimpleFunction<String, List<String>>() {
            @Override
            public List<String> apply(String input) {
                return Arrays.asList(input.split(" "));
            }
        }));

        PCollection<String> words = wordsperline.apply(Flatten.<String>iterables());

        PCollection<KV<String, Long>> wordCount = words.apply(Count.<String>perElement());

        wordCount.apply(MapElements.via(new SimpleFunction<KV<String, Long>, String>() {
            @Override
            public String apply(KV<String, Long> input) {
                return String.format("%s: %d", input.getKey(), input.getValue());
            }
        })).apply(TextIO.write().to("C:\\Users\\techolution\\Documents\\test").withoutSharding());


        pipeline.run().waitUntilFinish();

    }
}
