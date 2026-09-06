package com.sessions;

import java.time.LocalDateTime;
import java.util.*;

public class Session {

  private Long sessionId;
  private LocalDateTime startedAt;
  private LocalDateTime endedAt;
  private String status;
  private String notes;

  protected Session() {}

  public Session(
      Long sessionId, LocalDateTime startedAt, LocalDateTime endedAt, String status, String notes) {
    this.sessionId = sessionId;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
    this.status = status;
    this.notes = notes;
  }

  // Setters =============================================================================
  public void setSessionId(Long id) {
    sessionId = id;
  }

  public void setStartedAt(LocalDateTime time) {
    startedAt = time;
  }

  public void setEndedAt(LocalDateTime time) {
    endedAt = time;
  }

  public void setStatus(String newStatus){ status = newStatus; }

  public void setNotes(String newNotes) {
    notes = newNotes;
  }

  // Getters ==============================================================================
  public Long getSessionId(){
	return sessionId;
  }

  public LocalDateTime getStartedAt() {
    return startedAt;
  }

  public LocalDateTime getEndedAt() {
    return endedAt;
  }

  public String getStatus() {
    return status;
  }

  public String getNotes() {
    return notes;
  }
}
