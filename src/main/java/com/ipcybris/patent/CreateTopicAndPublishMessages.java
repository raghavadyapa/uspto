import com.google.pubsub.v1.ProjectTopicName;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.TimeUnit;

//package com.ipcybris.patent;


import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.TimeUnit;


import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
//import com.google.api.gax.rpc.ApiException;
import com.google.pubsub.v1.*;
//import com.google.pubsub.v1.*;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
//import com.google.pubsub.v1.TopicA
import com.google.pubsub.v1.PubsubMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A snippet for Google Cloud Pub/Sub showing how to create a Pub/Sub topic and asynchronously
 * publish messages to it.

public class CreateTopicAndPublishMessages {

    public static void createTopic() throws Exception {
        ProjectTopicName topic = ProjectTopicName.of("my-project-id", "my-topic-id");
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            topicAdminClient.createTopic(topic);
        }
    }

    public static void publishMessages() throws Exception {
        // [START pubsub_publish]
        ProjectTopicName topicName = ProjectTopicName.of("my-project-id", "my-topic-id");
        Publisher publisher = null;
        List<ApiFuture<String>> messageIdFutures = new ArrayList<>();

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            List<String> messages = Arrays.asList("first message", "second message");

            // schedule publishing one message at a time : messages get automatically batched
            for (String message : messages) {
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                // Once published, returns a server-assigned message id (unique within the topic)
                ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
                messageIdFutures.add(messageIdFuture);
            }
        } finally {
            // wait on any pending publish requests.
            List<String> messageIds = ApiFutures.allAsList(messageIdFutures).get();

            for (String messageId : messageIds) {
                System.out.println("published with message ID: " + messageId);
            }

            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
        // [END pubsub_publish]
    }

    public static void publishMessagesWithErrorHandler() throws Exception {
        // [START pubsub_publish_error_handler]
        ProjectTopicName topicName = ProjectTopicName.of("my-project-id", "my-topic-id");
        Publisher publisher = null;

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            List<String> messages = Arrays.asList("first message", "second message");

            for (final String message : messages) {
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                // Once published, returns a server-assigned message id (unique within the topic)
                ApiFuture<String> future = publisher.publish(pubsubMessage);

                // Add an asynchronous callback to handle success / failure
                ApiFutures.addCallback(
                        future,
                        new ApiFutureCallback<String>() {

                            @Override
                            public void onFailure(Throwable throwable) {
                                if (throwable instanceof ApiException) {
                                    ApiException apiException = ((ApiException) throwable);
                                    // details on the API exception
                                    System.out.println(apiException.getStatusCode().getCode());
                                    System.out.println(apiException.isRetryable());
                                }
                                System.out.println("Error publishing message : " + message);
                            }

                            @Override
                            public void onSuccess(String messageId) {
                                // Once published, returns server-assigned message ids (unique within the topic)
                                System.out.println(messageId);
                            }
                        },
                        MoreExecutors.directExecutor());
            }
        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
        // [END pubsub_publish_error_handler]
    }

    public static void main(String... args) throws Exception {
        createTopic();
        publishMessages();
    }
}


*/
