package com.database;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class DBConnectionTest {

  @Test
  void shouldConnectToDatabase() throws SQLException {

    DBConnection dbConnection = new DBConnection();

    try (Connection connection = dbConnection.getConnection()) {

      assertNotNull(connection);
      assertTrue(connection.isValid(2));
    }
  }
}
