/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.smsreminder.fragment.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import liquibase.util.csv.CSVReader;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.openmrs.api.context.Context;
import org.openmrs.module.smsreminder.ParameterStringBuilder;
import org.openmrs.module.smsreminder.api.dao.Database;
import org.openmrs.module.smsreminder.api.dao.DateFormatter;
import org.openmrs.module.smsreminder.api.dao.PatientDao;
import org.openmrs.ui.framework.fragment.FragmentModel;

/**
 * @author Nwokoma
 */
public class PatientFragmentController {
	
	PatientDao patientDao = new PatientDao();
	
	public void controller(FragmentModel model) throws Exception {
		
		String lastSentDate = "";
		Database.initConnection();
		List<Map<String, String>> allPatients = patientDao.getAllPatients();
		lastSentDate = Context.getAdministrationService().getGlobalProperty("last_sms_reminder");
		if (lastSentDate == null) {
			lastSentDate = "1990-01-01";
		}
		
		model.addAttribute("patients", allPatients);
		model.addAttribute("lastSentDate", lastSentDate);
		model.addAttribute("dformatter", DateFormatter.class);
	}
	
	private void sendSMS(String message, String phoneNumbers) {
		try {
			String appId = Long.toString(new Date().getTime());
			
			System.out.println(Long.toString(new Date().getTime()));
			// URL url = new URL("https://app.smartsmssolutions.ng/io/api/client/v1/sms/);
			
			URL url = new URL(
			        "https://api2.infobip.com/api/sendsms/plain?user=SteveJ&password=%40%40Health2345&type=LongSMS&sender=IHVN&SMS&appid="
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
			
			/*
			URL url = new URL("https://app.smartsmssolutions.ng/io/api/client/v1/sms/");
			Map<String, String> params = new LinkedHashMap<String, String>();
			params.put("token", "v20ylRY3Gp6jYEAvpaDtOQQTqwoCqc1n4CUG3IBboIMTciDeVk");
			params.put("sender", "Tony");
			params.put("to", phoneNumbers);
			params.put("message", message);
			params.put("type", "0");
			params.put("ref_id", appId);
			params.put("routing", "2");
			
			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String, String> param : params.entrySet()) {
				if (postData.length() != 0)
					postData.append('&');
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setDoOutput(true);
			conn.getOutputStream().write(postDataBytes);
			
			Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			//for (int c; (c = in.read()) >= 0;)
			//System.out.print((char) c);
			
			StringBuilder sb = new StringBuilder();
			for (int c; (c = in.read()) >= 0;)
				sb.append((char) c);
			String response = sb.toString();
			
			JSONArray ja = new JSONArray(response);
			
			int n = ja.length();
			for (int i = 0; i < n; i++) {
				// GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
				JSONObject jo = ja.getJSONObject(i);
				
				// RETRIEVE EACH JSON OBJECT'S FIELDS
				
				String code = jo.getString("code");
				String messageid = jo.getString("messageid");
				String ref_id = jo.getString("ref_id");
				
				System.out.println(code);
				System.out.println(messageid);
				System.out.println(ref_id);
				
			}
			            */
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public String sendSms(HttpServletRequest request) {
		
		try {
			
			String phoneNumbers1 = request.getParameter("phoneNumbers1");
			String phoneNumbers2 = request.getParameter("phoneNumbers2");
			
			//String phoneNumbers1 = "2348025254999,2348114452906";
			// String phoneNumbers2 = "2347067973091";
			
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
	
	public List<String[]> readAll(Reader reader) throws Exception {
		CSVReader csvReader = new CSVReader(reader);
		List<String[]> list = new ArrayList<String[]>();
		list = csvReader.readAll();
		reader.close();
		csvReader.close();
		return list;
	}
}
