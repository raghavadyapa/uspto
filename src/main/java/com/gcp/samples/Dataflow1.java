package com.gcp.samples;


import com.twitter.samples.PubsubToDataflow;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.joda.time.Duration;

public class Dataflow1  {


  public static void main(String[] args) {

    int numShards = 1;

    TestOptions options = PipelineOptionsFactory
            .fromArgs(args)
            .withValidation()
            .as(TestOptions.class);


    options.setStreaming(true);

    Pipeline pipeline = Pipeline.create(options);

            pipeline.apply("Read PubSub Messages", PubsubIO.readStrings().fromTopic(options.getInputTopic()))
                    .apply(Window.into(FixedWindows.of(Duration.standardMinutes(options.getWindowSize()))))
                    .apply("", TextIO.write().to(options.getOutputLocation()).withWindowedWrites().withNumShards(options.getWindowSize()));

    pipeline.run().waitUntilFinish();

  }


}
