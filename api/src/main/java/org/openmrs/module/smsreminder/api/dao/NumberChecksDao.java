/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smsreminder.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tony
 */
public class NumberChecksDao {
	
	PreparedStatement stmt = null;
	
	ResultSet rs = null;
	
	Connection con = null;
	
	public List<Map<String, String>> getAllNumbers() {
        
        List<Map<String, String>> allNumbers= new ArrayList<>();

        try {
            
                con = Database.connectionPool.getConnection();
                
                
                String query = "SELECT DISTINCT patient.patient_id, person_attribute.value AS phone_number FROM patient, phonechecks_comments.comment AS comment "
                        + " LEFT JOIN person_attribute ON person_attribute.person_id=patient.patient_id AND person_attribute.person_attribute_type_id=8 "
                        + " LEFT JOIN phonechecks_comments ON phonechecks_comments.patient_id=patient.patient_id "
                        + " where patient.voided=0 AND person_attribute.voided=0";
                // int i = 1;
                stmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String,String> tempMap = new HashMap<>();
                    tempMap.put("patient_id", rs.getString("patient_id"));
                    tempMap.put("phone_number", rs.getString("phone_number"));
                    tempMap.put("comment", rs.getString("comment"));
                    allNumbers.add(tempMap);
                }
                return allNumbers;

        }
        catch (SQLException ex) {
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
	
	public void addComments(String comment, int patient_id) {
		
		try {
			
			con = Database.connectionPool.getConnection();
			
			String query = "INSERT INTO phonechecks_comments  (patient_id, comment) VALUES (?,?)";
			int i = 1;
			
			stmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			stmt.setInt(i++, patient_id);
			stmt.setString(i++, comment);
			// stmt.setString(i++, query);    
			
			System.out.println("Now Print Query!");
			stmt.execute();
		}
		catch (SQLException ex) {
			Database.handleException(ex);
		}
		finally {
			Database.cleanUp(rs, stmt, con);
		}
	}
}
