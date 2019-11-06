package org.univ.sparktp.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

public class DummyListenerVerticle extends AbstractVerticle {
  @Override
  public void start(Future<Void> startFuture) throws Exception {
    EventBus eb = vertx.eventBus();

    eb.consumer(Adresses.STEP_COMPLETION_ADDR, message -> System.out.println("I have received a step completion message: " + message.body()));

    eb.consumer(Adresses.STEP_FAILURE_ADDR, message -> System.out.println("I have received a step failure message: " + message.body()));

    eb.consumer(Adresses.TEAM_REGISTRATION_ADDR, message -> System.out.println("I have received a step registration message: " + message.body()));
  }
}
