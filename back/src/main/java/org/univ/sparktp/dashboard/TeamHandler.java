package org.univ.sparktp.dashboard;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.univ.sparktp.dashboard.model.Team;


import java.util.List;
import java.util.logging.Logger;

import static org.univ.sparktp.dashboard.Adresses.*;

class TeamHandler {
  private static final Logger logger = Logger.getLogger(TeamHandler.class.getName());

  private Vertx vertx;

  TeamHandler(Vertx vertx) {
    this.vertx = vertx;
  }

  void createTeam(RoutingContext context) {

    String teamName = context.getBodyAsJson().getString("name");
    Team newTeam = IN_MEMORY_TEAMS.add(new Team(teamName));

    logger.info("forwarding message to eventbus");
    vertx.eventBus().send(TEAM_REGISTRATION_ADDR, context.getBodyAsJson());

    HttpServerResponse response = context.response();
    response.putHeader("content-type", "application/json");
    response.setStatusCode(201);

    response.end(JsonObject.mapFrom(newTeam).toString());
  }

  void getTeams(RoutingContext context) {
    List<Team> teams = IN_MEMORY_TEAMS.getTeams();

    JsonArray jsonResponse = new JsonArray();
    teams.stream()
         .map(JsonObject::mapFrom)
         .forEach(jsonResponse::add);

    HttpServerResponse response = context.response();
    response.putHeader("content-type", "application/json");
    response.setStatusCode(200);

    response.end(jsonResponse.toString());
  }

  void onStepCompleted(RoutingContext context) {
    String teamId = context.request().getParam("id");

    logger.info("forwarding message to eventbus");
    vertx.eventBus().send(STEP_COMPLETION_ADDR, stepCompletionMessage(teamId));

    HttpServerResponse response = context.response();
    response.putHeader("content-type", "application/json");
    response.setStatusCode(204);
    response.end();
  }

  void onStepFailed(RoutingContext context) {
    String teamId = context.request().getParam("id");

    logger.info("forwarding message to eventbus");

    vertx.eventBus().send(STEP_FAILURE_ADDR, stepCompletionMessage(teamId));

    HttpServerResponse response = context.response();
    response.putHeader("content-type", "application/json");
    response.setStatusCode(204);
    response.end();
  }

  private JsonObject stepCompletionMessage(String id) {
    return new JsonObject()
      .put("teamId", Integer.parseInt(id));
  }
}
