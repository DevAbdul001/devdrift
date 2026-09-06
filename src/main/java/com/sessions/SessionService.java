package com.sessions;

import java.time.LocalDateTime;
import java.util.*;

public class SessionService {

  private final SessionRepo repo;

  public SessionService(SessionRepo repo) {
    this.repo = repo;
  }

  // Start====================================================================================================

  public Session start(String newNotes) {

    Long sessionId = null;
    LocalDateTime startedAt = LocalDateTime.now();
    LocalDateTime endedAt = null;
    String status = null;
    String notes = newNotes;

    Session session = new Session(sessionId, startedAt, endedAt, status, notes);

    try {

      Session newSession = repo.startSession(session);
      return newSession;

    } catch (Exception e) {
      throw e;
    }
  }

  // End=====================================================================================================

  public Session end(Long id) {

    try {
      Session existingSession = repo.getSessionById(id);
      orElseThrow(() -> new RuntimeException("Invalid session id"));

      if (!(Objects.equals(existingSession.getEndedAt(), null))) {
        throw new RuntimeException("Session already ended.");
      }

      Session session = repo.endSession(id);
      return session;
    } catch (Exception e) {
      throw e;
    }
  }
}
