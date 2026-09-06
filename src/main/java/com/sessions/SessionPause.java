

import java.util.*;
import java.time; 

public class SessionPause{
	
	private Long pauseId;
	private Long sessionId;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;

	protected SessionPause(){}

	public SessionPause(
		Long pauseId,
		Long sessionId,
		LocalDateTime startedAt,
		LocalDateTime endedAt
			)
	{
		this.pauseId = pauseId;
		this.sessionId = sessionid;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
	}

	// Setters ===============================================================================
	public void setPauseId( Long id ){
		pauseId = id;
	}

	public void setSessionId ( Long id ){
		sessionId = id;
	}

	public void setStartedAt ( LocalDateTime time ){
		startedAt = time;
	}

	public void setEndedAt ( LocalDateTime time ) {
		endedAt = time;
	}

	// Getters ===============================================================================
	public Long getPauseid(){
		return pauseId;
	}

	public Long getSessionId(){ 
		return sessionId 
	}

	public LocalDateTime getStartedAt(){
		return startedAt;
	}

	public LocalDateTime getEndedAt(){
		return endedAt;
	}
}
