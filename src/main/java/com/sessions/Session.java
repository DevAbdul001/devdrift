

import java.util.*; 
import java.time;


public class Session{
	
	private Long session_id;
	private LocalDateTime started_at;
	private LocalDateTime ended_at;
	private String status;
	private String notes;

	public Session(
		Long sessionId,
		LocalDateTime startedAt,
		LocalDateTime endedAt,
		String status,
		String notes
	){
		this.session_id = sessionId;
		this.started_at = startedAt;
		this.ended_at = ended_at;
		this.status = status;
		this.notes = notes;
	}

	// Setters =============================================================================

	public void setStartedAt( LocalDateTime time){
		startedAt = time;
	}

	public void setEndedAt( LocalDateTime time ){
		endedAt = time;
	}

	public void setNotes ( String newNotes ){
		notes = newNotes;
	}


	// Getters ==============================================================================
	public LocalDateTime getStartedAt(){
		return startedAt;
	}

	public LocalDateTime getEndedAt(){
		return endedAt;
	}

	public String getStatus(){
		return status;
	}

	public String getNotes(){
		return notes;
	}
}
