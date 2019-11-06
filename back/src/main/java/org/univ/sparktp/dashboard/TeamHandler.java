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

class TeamHandler extends AbstractVerticle {
  private static final Logger logger = Logger.getLogger(TeamHandler.class.getName());

  private EventBus eventBus;

  TeamHandler(EventBus eventBus) {
    this.eventBus = eventBus;}

  void createTeam(RoutingContext context) {

    String teamName = context.getBodyAsJson().getString("name");
    Team newTeam = IN_MEMORY_TEAMS.add(new Team(teamName));

    logger.info("forwarding message to eventbus");
    eventBus.publish(TEAM_REGISTRATION_ADDR, JsonObject.mapFrom(newTeam));

    context.response()
           .putHeader("content-type", "application/json")
           .setStatusCode(201)
           .end(JsonObject.mapFrom(newTeam).toString());
  }

  void getTeams(RoutingContext context) {
    List<Team> teams = IN_MEMORY_TEAMS.getTeams();

    JsonArray jsonResponse = new JsonArray();
    teams.stream()
         .map(JsonObject::mapFrom)
         .forEach(jsonResponse::add);

    context.response()
           .putHeader("content-type", "application/json")
           .setStatusCode(200)
           .end(jsonResponse.toString());
  }

  void onStepCompleted(RoutingContext context) {
    String teamId = context.request().getParam("id");
    Team team = IN_MEMORY_TEAMS.byId(Integer.parseInt(teamId));
    team.nextStep();

    logger.info("forwarding message to eventbus");

    eventBus.publish(STEP_COMPLETION_ADDR, JsonObject.mapFrom(team));

    context.response()
           .setStatusCode(204)
           .end();
  }

  void onStepFailed(RoutingContext context) {
    String teamId = context.request().getParam("id");
    Team team = IN_MEMORY_TEAMS.byId(Integer.parseInt(teamId));

    logger.info("forwarding message to eventbus");

    eventBus.publish(STEP_FAILURE_ADDR, JsonObject.mapFrom(team));

    context.response()
           .setStatusCode(204)
           .end();
  }
}
