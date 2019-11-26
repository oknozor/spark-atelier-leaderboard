package org.univ.sparktp.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

// TODO : Have only one verticle at the moment but we will need to deploy another for persistence
// Persistence is needed : in case of crash the application needs to be able to restore its state ? Or not ?
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
