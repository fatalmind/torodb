/*
 * ToroDB
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.torodb.mongodb.repl;

import com.eightkdata.mongowp.client.wrapper.MongoClientConfiguration;
import com.google.common.base.Preconditions;
import com.torodb.core.bundle.BundleConfig;
import com.torodb.core.logging.LoggerFactory;
import com.torodb.core.metrics.ToroMetricRegistry;
import com.torodb.mongodb.core.MongoDbCoreBundle;
import com.torodb.mongodb.repl.filters.ReplicationFilters;

import java.util.Optional;

public class MongoDbReplConfigBuilder {

  private MongoDbCoreBundle coreBundle;
  private MongoClientConfiguration mongoClientConfiguration;
  private ReplicationFilters replicationFilters;
  private String replSetName;
  private ConsistencyHandler consistencyHandler;
  private Optional<ToroMetricRegistry> metricRegistry;
  private LoggerFactory loggerFactory;
  private final BundleConfig generalConfig;

  public MongoDbReplConfigBuilder(BundleConfig generalConfig) {
    this.generalConfig = generalConfig;
  }

  public MongoDbReplConfigBuilder setCoreBundle(MongoDbCoreBundle coreBundle) {
    this.coreBundle = coreBundle;
    return this;
  }

  public MongoDbReplConfigBuilder setMongoClientConfiguration(
      MongoClientConfiguration mongoClientConfiguration) {
    this.mongoClientConfiguration = mongoClientConfiguration;
    return this;
  }

  public MongoDbReplConfigBuilder setReplicationFilters(ReplicationFilters replicationFilters) {
    this.replicationFilters = replicationFilters;
    return this;
  }

  public MongoDbReplConfigBuilder setReplSetName(String replSetName) {
    this.replSetName = replSetName;
    return this;
  }

  public MongoDbReplConfigBuilder setConsistencyHandler(ConsistencyHandler consistencyHandler) {
    this.consistencyHandler = consistencyHandler;
    return this;
  }

  public MongoDbReplConfigBuilder setMetricRegistry(Optional<ToroMetricRegistry> metricRegistry) {
    this.metricRegistry = metricRegistry;
    return this;
  }

  public MongoDbReplConfigBuilder setLoggerFactory(LoggerFactory loggerFactory) {
    this.loggerFactory = loggerFactory;
    return this;
  }

  public MongoDbReplConfig build() {
    Preconditions.checkNotNull(coreBundle, "core bundle must be not null");
    Preconditions.checkNotNull(mongoClientConfiguration, "mongo client configuration must be not "
        + "null");
    Preconditions.checkNotNull(replicationFilters, "replication filters must be not null");
    Preconditions.checkNotNull(replSetName, "replSetName must be not null");
    Preconditions.checkNotNull(consistencyHandler, "consistency handler must be not null");
    Preconditions.checkNotNull(generalConfig, "general config must be not null");
    Preconditions.checkNotNull(metricRegistry, "metric registry must be not null");
    Preconditions.checkNotNull(loggerFactory, "logger factory must be not null");

    return new MongoDbReplConfig(coreBundle, mongoClientConfiguration, replicationFilters,
        replSetName, consistencyHandler, metricRegistry, loggerFactory, generalConfig);
  }

}
