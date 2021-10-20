/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smsreminder.fragment.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.ParameterStringBuilder;
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
	
	private void sendSMS(String message, String phoneNumbers) {
		try {
			String appId = Long.toString(new Date().getTime());
			
			System.out.println(Long.toString(new Date().getTime()));
			
			URL url = new URL(
			        "https://api2.infobip.com/api/sendsms/plain?user=SteveJ&password=Health%4012345&type=LongSMS&sender=IHVN&SMS&appid="
			                + appId + "&GSM=" + phoneNumbers + "&Text=" + message);
			StringBuilder strBuf = new StringBuilder();
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			Map<String, String> parameters = new HashMap<String, String>();
			con.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
			System.out.println(con.getResponseCode());
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			String output = null;
			while ((output = reader.readLine()) != null)
				strBuf.append(output);
			
			System.out.println(strBuf.toString());
			out.flush();
			out.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public String sendSms(HttpServletRequest request) {
		
		try {
			
			String phoneNumbers1 = request.getParameter("phoneNumbers1");
			String phoneNumbers2 = request.getParameter("phoneNumbers2");
			/*
			String phoneNumbers1 = "2348025254999,2348114452906,23436477122";
			String phoneNumbers2 = "2347067973091,23481884965986";
			*/
			this.sendSMS("A", phoneNumbers1);
			this.sendSMS("AA", phoneNumbers2);
			
			this.updateSentStatus();
			return "true";
		}
		catch (Exception ex) {
			ex.printStackTrace();
			//Logger.getLogger(PatientFragmentController.class.getName()).log(Level.SEVERE, null, ex);
			return "false";
		}
		
	}
	
	public String updateSentStatus() {
		
		Calendar now = Calendar.getInstance();
		
		String today = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE);
		Context.getAdministrationService().setGlobalProperty("last_sms_reminder", today);
		return "complete";
	}
}
