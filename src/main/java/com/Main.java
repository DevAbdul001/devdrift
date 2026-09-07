package com;

import com.cli.CLI;
import com.database.DBConnection;
import com.sessions.SessionRepo;
import com.sessions.SessionService;

public class Main {

  public static void main(String[] args) {

    DBConnection dbConnection = new DBConnection();

    SessionRepo sessionRepo = new SessionRepo(dbConnection);

    SessionService sessionService = new SessionService(sessionRepo);

    CLI cli = new CLI(sessionService);

    cli.run();
  }
}
