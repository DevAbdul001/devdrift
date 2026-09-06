

CREATE TABLE IF NOT EXISTS sessions (
    session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    started_at DATETIME NOT NULL,
    ended_at DATETIME,
    status ENUM ('STARTED', 'PAUSED', 'ENDED') DEFAULT 'STARTED',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    
    started_active_check INT GENERATED ALWAYS AS (IF(status = 'STARTED', 1, NULL)) VIRTUAL,


    CONSTRAINT unique_single_started_session UNIQUE (started_active_check)
);


CREATE TABLE IF NOT EXISTS session_pauses (
	pause_id BIGINT AUTO_INCREMENT PRIMARY KEY,
	session_id BIGINT NOT NULL,
	started_at DATETIME,
	ended_at DATETiME,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
	FOREIGN KEY (session_id) REFERENCES sessions(session_id)
);

CREATE INDEX session_pause_index ON session_pauses(session_id);
