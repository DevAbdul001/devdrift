package com.database;

import java.sql.*;
import java.util.*;

public class DBConnection {

  String url = System.getenv("DEVDRIFT_URL");
  String user = System.getenv("DEVDRIFT_USER");
  String password = System.getenv("DEVDRIFT_PASSWORD");

  public Connection getConnection() {

    try {
      Connection myConn = DriverManager.getConnection(url, user, password);
      return myConn;

    } catch (Exception e) {
      throw new RuntimeException("Something went wrong!");
    }
  }
}
