
package com.sessions; 

import com.database.DBConnection;
import com.sessions.Session;

import java.sql.*;
import java.time;


public class SessionRepo{
	private final DBConnection dbConnection;

	public SessionRepo(DBConnection dbConnection){
		this.dbConnection = dbConnection;
	}

	public Session startSession( Session session){

		Connection myConn = dbConnection.getConnection();

		try{
			String sql = "INSERT INTO sessions (started_at, notes) VALUES (?,?)";
			PreparedStatement pstmt= myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			LocalDateTime startedAt = session.getStartedAt();
			String notes = session.getNotes();

			pstmt.setObject(1, startedAt);
			pstmt.setObject(2, notes);

			int insertedRows = pstmt.executeUpdate();
			Long generatedId = null;

			if( insertedRows > 0 ){
				ResultSet generatedKey = pstmt.getGeneratedKeys();

				if(generatedKey.next()){
					generatedId = generatedKey.getLong(1); 
				}
			}
			session.setSessionId (generatedId);

			return session;	

			myConn.close();
			
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}
}
