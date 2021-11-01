/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smsreminder.fragment.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.ParameterStringBuilder;
import org.openmrs.module.smsreminder.api.dao.Database;
import org.openmrs.module.smsreminder.api.dao.DateFormatter;
import org.openmrs.module.smsreminder.api.dao.NumberChecksDao;
import org.openmrs.module.smsreminder.api.dao.PatientDao;
import org.openmrs.ui.framework.fragment.FragmentModel;

/**
 * @author Tony
 */
public class NumberchecksFragmentController {
	
	NumberChecksDao numbersDao = new NumberChecksDao();
	
	public void controller(FragmentModel model) {
		
		String lastNumberChecks = "";
		Database.initConnection();
		List<Map<String, String>> allNumbers = numbersDao.getAllNumbers();
		lastNumberChecks = Context.getAdministrationService().getGlobalProperty("last_number_checks");
		if (lastNumberChecks == null) {
			lastNumberChecks = "1990-01-01";
		}
		
		model.addAttribute("numbers", allNumbers);
		model.addAttribute("lastNumberChecks", lastNumberChecks);
		model.addAttribute("dformatter", DateFormatter.class);
	}
	
	private void callNumber(String message, String phoneNumbers) {
		
	}
	
	public String callNumber(HttpServletRequest request) {
		
		this.updateCheckStatus();
		return "A";
		
	}
	
	/*
	private void updateCheck(int phoneNumber, String status, String comment, String checkdated){
	    
	}*/
	public String updateCheckStatus() {
		
		Calendar now = Calendar.getInstance();
		
		String today = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE);
		Context.getAdministrationService().setGlobalProperty("last_number_checks", today);
		return "complete";
	}
}
