package org.univ.sparktp.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.univ.sparktp.dashboard.model.twitter.Tweet;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class KafkaClientVerticle extends AbstractVerticle {

  private static final Logger logger = Logger.getLogger(KafkaClientVerticle.class.getName());

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    EventBus eb = vertx.eventBus();

    Map<String, String> config = new HashMap<>();
    config.put("bootstrap.servers", "localhost:9092");
    config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    config.put("group.id", "twitter_group");
    config.put("auto.offset.reset", "earliest");
    config.put("enable.auto.commit", "false");

    KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);

    consumer.handler(record -> {
      Tweet tweet = Tweet.fromJson(new JsonObject(record.value()));

      // Message with id 0 are errored so we ignore them
      if (tweet.getId() != 0) {
        // For now we just send raw tweet to the event bus
        // We might want to enrich the data later on
        eb.publish(Adresses.TWITTER_INFO, JsonObject.mapFrom(tweet));
        logger.info(tweet.toString());
      }
    });

    consumer.subscribe("twitter");
  }
}
