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

package com.torodb.backend.derby.driver;

import com.torodb.backend.derby.driver.OfficialDerbyDriver;
import com.torodb.backend.derby.driver.DerbyDbBackendConfigBuilder;
import com.google.inject.Guice;
import com.torodb.core.bundle.BundleConfigImpl;
import com.torodb.core.supervision.SupervisorDecision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

public class DriverDerbyTest {

  private DataSource dataSource;

  @Before
  public void setUp() throws Exception {
    OfficialDerbyDriver derbyDriver = new OfficialDerbyDriver();
    BundleConfigImpl genericConfig = new BundleConfigImpl(
        Guice.createInjector(),
        (supervised, error) -> SupervisorDecision.IGNORE
    );
    dataSource = derbyDriver.getConfiguredDataSource(
        new DerbyDbBackendConfigBuilder(genericConfig)
            .setReservedReadPoolSize(0)
            .setDbPort(0)
            .setDbName("torodb")
            .setConnectionPoolSize(0)
            .setConnectionPoolTimeout(0)
            .setInMemory(true)
            .setEmbedded(true)
            .build(),
        "torod");
  }

  @Test
  public void testDDLRollback() throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "CREATE TABLE test (a int)")) {
        preparedStatement.executeUpdate();
      }
      connection.commit();
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "INSERT INTO test VALUES (1)")) {
        preparedStatement.executeUpdate();
      }
      try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM test")) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          Assert.assertTrue(resultSet.next());
          Assert.assertFalse(resultSet.next());
        }
      }
      connection.rollback();
      try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM test")) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          Assert.assertFalse(resultSet.next());
        }
      }
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "INSERT INTO test VALUES (1)")) {
        preparedStatement.executeUpdate();
      }
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "CREATE TABLE test2 (b varchar(128))")) {
        preparedStatement.executeUpdate();
      }
      try (PreparedStatement preparedStatement = connection.prepareStatement(
          "ALTER TABLE test ADD COLUMN b varchar(128)")) {
        preparedStatement.executeUpdate();
      }
      try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM test")) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          Assert.assertTrue(resultSet.next());
          Assert.assertFalse(resultSet.next());
        }
      }
      connection.rollback();
      try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM test")) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          Assert.assertFalse(resultSet.next());
        }
      }
      try (ResultSet resultSet = connection.getMetaData().getTables("%", "%", "test2", null)) {
        Assert.assertFalse(resultSet.next());
      }
      try (ResultSet resultSet = connection.getMetaData().getColumns("%", "%", "test", "b")) {
        Assert.assertFalse(resultSet.next());
      }
      connection.rollback();
      connection.setAutoCommit(true);
    }
  }
}
