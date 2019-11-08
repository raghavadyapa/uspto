package com.gcp.samples;

import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.options.ValueProvider;

public interface TestOptions extends PipelineOptions, StreamingOptions {

  ValueProvider<String> getInputTopic();
  void setInputTopic(ValueProvider<String> value);


  Integer getWindowSize();
  void setWindowSize(Integer value);

  ValueProvider<String> getOutputLocation();
  void setOutputLocation(ValueProvider<String> value);

}
