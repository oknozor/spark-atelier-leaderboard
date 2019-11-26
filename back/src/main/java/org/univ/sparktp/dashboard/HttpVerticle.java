package org.univ.sparktp.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;


import java.util.logging.Logger;

import static org.univ.sparktp.dashboard.Adresses.*;

public class HttpVerticle extends AbstractVerticle {

  private static final Logger logger = Logger.getLogger(HttpVerticle.class.getName());

  @Override
  public void start(Promise<Void> startPromise) {

    logger.info(String.format("Starting Http verticle on port : %d", config().getInteger("port")));

    HttpServer server = vertx.createHttpServer();
    TeamHandler teamHandler = new TeamHandler(vertx.eventBus());

    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());


    JsonObject webSocketConfig = config().getJsonObject("websocket.client");
    // TODO: add sock js url to configuration
    router.route().handler(CorsHandler.create("http://" + webSocketConfig.getString("host") + ":" +  webSocketConfig.getInteger("port"))
                                      .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                                      .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                                      .allowedMethod(io.vertx.core.http.HttpMethod.PUT)
                                      .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                                      .allowCredentials(true)
                                      .allowedHeader("Access-Control-Allow-Method")
                                      .allowedHeader("Access-Control-Allow-Origin")
                                      .allowedHeader("Access-Control-Allow-Credentials")
                                      .allowedHeader("Content-Type"));



    router.post("/teams").handler(teamHandler::createTeam);
    router.get("/teams").handler(teamHandler::getTeams);
    router.post("/teams/:id/completeStep").handler(teamHandler::onStepCompleted);
    router.post("/teams/:id/failStep").handler(teamHandler::onStepFailed);


    BridgeOptions permitted = new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress(STEP_COMPLETION_ADDR))
                                                 .addOutboundPermitted(new PermittedOptions().setAddress(STEP_FAILURE_ADDR))
                                                 .addOutboundPermitted(new PermittedOptions().setAddress(TEAM_REGISTRATION_ADDR))
                                                 .addOutboundPermitted(new PermittedOptions().setAddress(TWITTER_INFO))
                                                 .addInboundPermitted(new PermittedOptions().setAddress(STEP_COMPLETION_ADDR))
                                                 .addInboundPermitted(new PermittedOptions().setAddress(STEP_FAILURE_ADDR))
                                                 .addInboundPermitted(new PermittedOptions().setAddress(TWITTER_INFO))
                                                 .addInboundPermitted(new PermittedOptions().setAddress(TEAM_REGISTRATION_ADDR));

    // Create a bridge between the eventbus and the frontend websocket
    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
    sockJSHandler.bridge(permitted);

    router.route("/eventbus/*").handler(sockJSHandler);

    server.requestHandler(router).listen(config().getInteger("port"));
  }
}

