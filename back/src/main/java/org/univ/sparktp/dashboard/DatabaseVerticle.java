package org.univ.sparktp.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public class DatabaseVerticle extends AbstractVerticle {
  @Override
  public void start() {
    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(config().getInteger("port"))
      .setHost(config().getString("host"))
      .setDatabase(config().getString("database"))
      .setUser(config().getString("user"))
      .setPassword(config().getString("password"));

    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);

    PgPool client = PgPool.pool(this.vertx, connectOptions, poolOptions);
  }
}
