package org.univ.sparktp.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture)  {
    JsonObject config = config();

    vertx.deployVerticle(HttpVerticle.class, new DeploymentOptions().setConfig(config.getJsonObject("http")));
    vertx.deployVerticle(KafkaClientVerticle.class, new DeploymentOptions().setConfig(config.getJsonObject("kafka")));
    vertx.deployVerticle(DatabaseVerticle.class, new DeploymentOptions().setConfig(config.getJsonObject("database")));
    vertx.deployVerticle(DummyListenerVerticle.class, new DeploymentOptions());
  }
}
