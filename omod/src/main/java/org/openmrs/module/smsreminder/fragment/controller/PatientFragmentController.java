/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smsreminder.fragment.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.api.dao.Database;
import org.openmrs.module.smsreminder.api.dao.PatientDao;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

/**
 * @author Nwokoma
 */
public class PatientFragmentController {
	
	PatientDao patientDao = new PatientDao();
	
	public void controller(FragmentModel model) {
		
		String lastSentDate = "";
		Database.initConnection();
		List<Map<String, String>> allPatients = patientDao.getAllPatients();
		lastSentDate = Context.getAdministrationService().getGlobalProperty("last_sms_reminder");
		if (lastSentDate == null) {
			lastSentDate = "1990-01-01";
		}
		
		model.addAttribute("patients", allPatients);
		model.addAttribute("lastSentDate", lastSentDate);
	}
	
	public String updateSentStatus(HttpServletRequest request) {
		
		Calendar now = Calendar.getInstance();
		
		String today = now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DATE);
		Context.getAdministrationService().setGlobalProperty("last_sms_reminder", today);
		return "complete";
	}
}
