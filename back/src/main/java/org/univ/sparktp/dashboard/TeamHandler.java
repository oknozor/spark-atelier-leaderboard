package org.univ.sparktp.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.univ.sparktp.dashboard.model.Team;


import java.util.logging.Logger;

import static org.univ.sparktp.dashboard.Adresses.*;

class TeamHandler extends AbstractVerticle {
  private static final Logger logger = Logger.getLogger(TeamHandler.class.getName());

  private EventBus eventBus;

  TeamHandler(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  void createTeam(RoutingContext context) {

    String teamName = context.getBodyAsJson().getString("name");
    eventBus.request(DB_CREATE_TEAM, teamName, ar -> {
      if (ar.succeeded()) {
        JsonObject newTeam = (JsonObject) ar.result().body();
        logger.info("forwarding message to eventbus");
        eventBus.publish(TEAM_REGISTRATION_ADDR, JsonObject.mapFrom(newTeam));

        context.response()
               .putHeader("content-type", "application/json")
               .setStatusCode(201)
               .end(newTeam.toString());
      } else {
        notFound(context);
      }
    });
  }

  void getTeams(RoutingContext context) {
    eventBus.request(DB_GET_ALL_TEAMS, "ok", ar -> {
      logger.info("Sending request");

      if (ar.succeeded()) {
        JsonArray teams = (JsonArray) ar.result().body();
        logger.info(teams.toString());

        context.response()
               .putHeader("content-type", "application/json")
               .setStatusCode(200)
               .end(teams.toString());
      } else {
        internalError(context);
      }
    });
  }

  void onStepCompleted(RoutingContext context) {
    String teamId = context.request().getParam("id");
    eventBus.request(DB_TEAM_BY_ID, Integer.parseInt(teamId), ar -> {
      if (ar.succeeded()) {
        JsonObject team = (JsonObject) ar.result().body();
        logger.info(team.toString());
        Team updatedTeam = team.mapTo(Team.class);
        updatedTeam.nextStep();

        eventBus.request(DB_SAVE_TEAM, JsonObject.mapFrom(updatedTeam), update -> {
          if (update.succeeded()) {
            eventBus.publish(STEP_COMPLETION_ADDR, JsonObject.mapFrom(updatedTeam));
            context.response()
                   .setStatusCode(204)
                   .end();
          }
        });

      } else {
        notFound(context);
      }
    });
  }

  void onStepFailed(RoutingContext context) {
    String teamId = context.request().getParam("id");
    eventBus.request(DB_TEAM_BY_ID, Integer.parseInt(teamId), ar -> {
      if (ar.succeeded()) {
        JsonObject team = (JsonObject) ar.result().body();
        eventBus.publish(STEP_FAILURE_ADDR, JsonObject.mapFrom(team));

        context.response()
               .setStatusCode(204)
               .end();
      } else {
        notFound(context);
      }
    });
  }

  private void notFound(RoutingContext context) {
    context.response()
           .setStatusCode(404)
           .end();
  }

  private void internalError(RoutingContext context) {
    context.response()
           .setStatusCode(500)
           .end();
  }
}
