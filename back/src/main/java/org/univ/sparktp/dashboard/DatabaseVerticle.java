package org.univ.sparktp.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import org.univ.sparktp.dashboard.model.Team;


import java.util.logging.Logger;

import static org.univ.sparktp.dashboard.Adresses.*;

public class DatabaseVerticle extends AbstractVerticle {

  private PgPool client;
  private static final Logger logger = Logger.getLogger(DatabaseVerticle.class.getName());

  @Override
  public void start() {
    Integer port = config().getInteger("port");
    String host = config().getString("host");
    String database = config().getString("database");
    String user = config().getString("user");
    String password = config().getString("password");

    logger.info("Connecting to database " + database + " on " + host + ":" + port);

    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(port)
      .setHost(host)
      .setDatabase(database)
      .setUser(user)
      .setPassword(password);

    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);

    EventBus eventBus = vertx.eventBus();
    client = PgPool.pool(this.vertx, connectOptions, poolOptions);

    MessageConsumer<JsonObject> updateTeamConsumer = eventBus.consumer(DB_SAVE_TEAM);
    MessageConsumer<String> createTeamConsumer = eventBus.consumer(DB_CREATE_TEAM);
    MessageConsumer<String> getAllTeamsConsumer = eventBus.consumer(DB_GET_ALL_TEAMS);
    MessageConsumer<Integer> teamByIdConsumer = eventBus.consumer(DB_TEAM_BY_ID);

    updateTeamConsumer.handler(this::update);
    createTeamConsumer.handler(this::create);
    getAllTeamsConsumer.handler(this::getAll);
    teamByIdConsumer.handler(this::byId);

    logger.info("started database verticle");

  }

  private void byId(Message<Integer> teamByIdCommand) {
    client.preparedQuery("SELECT * FROM team WHERE id=$1", Tuple.of(teamByIdCommand.body()), ar -> {
      if (ar.succeeded()) {

        JsonObject team = teamFromRow(ar);
        teamByIdCommand.reply(team);
      } else {
        logger.info("Failure: " + ar.cause().getMessage());
      }
    });
  }

  private void update(Message<JsonObject> teamMessage) {
    Integer teamId = teamMessage.body().getInteger("id");
    Integer stepId = teamMessage.body().getInteger("currentStepId");

    client.preparedQuery("UPDATE team SET current_step_id = $1 WHERE id = $2 RETURNING *", Tuple.of(stepId, teamId), ar -> {
      if (ar.succeeded()) {

        JsonObject team = teamFromRow(ar);
        teamMessage.reply(team);
      } else {
        logger.info("Failure: " + ar.cause().getMessage());
      }
    });

  }

  private void getAll(Message<String> getAllTeamMessages) {

    client.preparedQuery("SELECT * FROM team", ar -> {
      JsonArray teams = new JsonArray();
      if (ar.succeeded()) {
        RowSet<Row> rows = ar.result();
        logger.info("Got " + rows.size() + " rows ");

        for (Row row : rows) {
          Team team = new Team(row.getInteger(0), row.getString(1), row.getInteger(2));
          teams.add(JsonObject.mapFrom(team));
          logger.info(JsonObject.mapFrom(team).toString());
        }

        getAllTeamMessages.reply(teams);
      } else {
        logger.info("Failure: " + ar.cause().getMessage());
      }

    });
  }

  private void create(Message<String> createTeamCommand) {
    String teamName = createTeamCommand.body();

    client.preparedQuery("INSERT INTO team(name) values($1) RETURNING *", Tuple.of(teamName), ar -> {
      if (ar.succeeded()) {

        logger.info("Team " + teamName + " saved ");
        JsonObject team = teamFromRow(ar);
        logger.info(team.toString());
        createTeamCommand.reply(team);
      } else {
        logger.info("Failure: " + ar.cause().getMessage());
      }
    });

  }

  private JsonObject teamFromRow(AsyncResult<RowSet<Row>> ar) {
    JsonArray teams = new JsonArray();
    RowSet<Row> rows = ar.result();
    for (Row row : rows) {
      Team team = new Team(row.getInteger(0), row.getString(1), row.getInteger(2));
      teams.add(JsonObject.mapFrom(team));
    }

    return teams.getJsonObject(0);
  }
}
