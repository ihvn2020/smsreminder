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
 * @author Nwokoma
 */

public class PatientDao {
	
	public List<Map<String, String>> getAllPatients() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        List<Map<String, String>> allPatients = new ArrayList<>();

        try {
            
                con = Database.connectionPool.getConnection();
                
                
                String query = "SELECT patient.patient_id, person_attribute.value AS phone_number, obs.value_datetime AS next_date FROM patient "
                        + " JOIN person_attribute ON person_attribute.person_id=patient.patient_id AND person_attribute.person_attribute_type_id=8 "
                        + " JOIN obs ON obs.person_id=patient.patient_id AND obs.concept_id=5096 AND (DATEDIFF(obs.value_datetime, CURDATE()) = 1 OR DATEDIFF(obs.value_datetime, CURDATE()) = 2)"
                        + " where patient.voided=0 LIMIT 100";
                // int i = 1;
                stmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String,String> tempMap = new HashMap<>();
                    tempMap.put("patient_id", rs.getString("patient_id"));
                    tempMap.put("phone_number", rs.getString("phone_number"));
                    tempMap.put("next_date", rs.getString("next_date"));
                    allPatients.add(tempMap);
                }
                return allPatients;

        }
        catch (SQLException ex) {
                Database.handleException(ex);
                return null;
        }
        finally {
                Database.cleanUp(rs, stmt, con);
        }
    }
}
