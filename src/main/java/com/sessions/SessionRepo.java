package com.sessions;

import com.database.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;

public class SessionRepo {
  private final DBConnection dbConnection;

  public SessionRepo(DBConnection dbConnection) {
    this.dbConnection = dbConnection;
  }

  public Session startSession(Session session) throws SQLException {

    Connection myConn = dbConnection.getConnection();

    try {
      String sql = "INSERT INTO sessions (started_at, notes) VALUES (?,?)";
      PreparedStatement pstmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      LocalDateTime startedAt = session.getStartedAt();
      String notes = session.getNotes();

      pstmt.setObject(1, startedAt);
      pstmt.setObject(2, notes);

      int insertedRows = pstmt.executeUpdate();
      Long generatedId = null;

      if (insertedRows > 0) {
        ResultSet generatedKey = pstmt.getGeneratedKeys();

        if (generatedKey.next()) {
          generatedId = generatedKey.getLong(1);
        }
      }
      session.setSessionId(generatedId);

      myConn.close();
      return session;
    } catch (SQLException e) {
      throw e;
    }
  }

  public Session getSessionById(Long id) throws SQLException {

    Connection myConn = dbConnection.getConnection();
    Session session = new Session();

    try (myConn) {
      String sql =
          "SELECT session_id, started_at, ended_at, status, notes FROM sessions WHERE session_id = ?";

      try (PreparedStatement pstmt = myConn.prepareStatement(sql)) {
        pstmt.setLong(1, id);

        try (ResultSet rs = pstmt.executeQuery()) {
          if (rs.next()) {
            session.setSessionId(rs.getLong("session_id"));
            session.setStartedAt(rs.getObject("started_at", LocalDateTime.class));
            session.setEndedAt(rs.getObject("ended_at", LocalDateTime.class));
            session.setStatus(rs.getString("status"));
            session.setNotes(rs.getString("notes"));
          }
        }
      }

      return session;
    } catch (SQLException e) {
      throw e;
    }
  }

  public Session endSession(Long id) throws SQLException {

    Connection myConn = dbConnection.getConnection();
    Session session = new Session();

    try (myConn) {
      myConn.setAutoCommit(false);
      String updateSql = "UPDATE sessions SET ended_at = NOW() WHERE session_id = ?";
      String selectSql =
          "SELECT session_id, started_at, status, ended_at, notes FROM sessions WHERE session_id = ?";

      try (PreparedStatement updatePstmt = myConn.prepareStatement(updateSql)) {
        updatePstmt.setLong(1, id);
        updatePstmt.executeUpdate();
      }

      try (PreparedStatement selectPstmt = myConn.prepareStatement(selectSql)) {
        selectPstmt.setLong(1, id);

        try (ResultSet rs = selectPstmt.executeQuery()) {
          if (rs.next()) {

            session.setSessionId(rs.getLong("session_id"));
            session.setStartedAt(rs.getObject("started_at", LocalDateTime.class));
            session.setEndedAt(rs.getObject("ended_at", LocalDateTime.class));
            session.setStatus(rs.getString("status"));
            session.setNotes(rs.getString("notes"));
          }
        }
      }

      myConn.commit();
      return session;

    } catch (SQLException e) {
      myConn.rollback();
      throw e;
    }
  }
}
