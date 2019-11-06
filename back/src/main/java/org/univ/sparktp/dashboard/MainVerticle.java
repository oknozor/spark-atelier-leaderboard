package org.univ.sparktp.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

// TODO : Have only one verticle at the moment but we will need to deploy another for persistence
// Persistence is needed : in case of crash the application needs to be able to restore its state ? Or not ?
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture)  {

    vertx.deployVerticle(HttpVerticle.class, new DeploymentOptions());
    vertx.deployVerticle(DummyListenerVerticle.class, new DeploymentOptions());
  }
}
