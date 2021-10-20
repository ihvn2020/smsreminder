
<div style="text-align: center;"><button class="btn btn-success" id="sendSMS" style="text-align: center;">Send SMS</button></div>
<hr>
<h3>Last Sent Date: <%=lastSentDate%></h3>
<table>
    <thead>
        <tr>
            <th>Patient ID</th><th>Phone number</th><th>Next Appointment Date</th>
        </tr>
    </thead>
    
    <tbody>
        <% for(int i=0; i<patients.size(); i++)
        { %>
           <tr>
               <td><%= patients.get(i).get("patient_id"); %></td>
                <td><%= patients.get(i).get("phone_number"); %></td>
                <td><%= patients.get(i).get("next_date"); %></td>
           </tr>
       <% } %>
    </tbody>
</table>

    
<script type="text/javascript">
    var jq = jQuery;
       // var allPatients = new Array();
       var phoneNumbers1 = "";
       var phoneNumbers2 = "";
       var cleanedPhoneNumber = "";
       
        
       var d = new Date();
       var today = new Date(d.getFullYear() + "/" + (d.getMonth()+1) + "/" + d.getDate());
        
        <% for(int i=0; i<patients.size(); i++)
             { %>
               // allPatients.push("<%= patients.get(i).get("phone_number") %>");
                
               cleanedPhoneNumber = "<%= patients.get(i).get("phone_number") %>";
                
                var next_appointment = new Date("<%= patients.get(i).get("next_date") %>");
            
                var daydiff = (next_appointment-today) / (1000 * 60 * 60 * 24);
                var nodays = Math.round(daydiff);
        
                    if(cleanedPhoneNumber.length>=10){                      
                        console.log(cleanedPhoneNumber);
    
                            cleanedPhoneNumber.indexOf( '0' ) == 0 ? cleanedPhoneNumber = cleanedPhoneNumber.replace( '0', '234' ) : cleanedPhoneNumber;
                            if(cleanedPhoneNumber.substring(0,3)!='234'){
                                cleanedPhoneNumber = '234'+cleanedPhoneNumber;
                            }
    
                            if(nodays==1){
                                if(<%=i %>==<%=patients.size%>-1){               
                                    phoneNumbers1 += cleanedPhoneNumber;
                                }else{
                                    phoneNumbers1 += cleanedPhoneNumber+",";  
                                }
                            }else{
                                if(<%=i %>==<%=patients.size%>-1){               
                                    phoneNumbers2 += cleanedPhoneNumber;
                                }else{
                                    phoneNumbers2 += cleanedPhoneNumber+",";  
                                }
                            }
    
                    }
               // }
    
                console.log(phoneNumbers1);
                console.log(phoneNumbers2);
    
           <% } %>
       
           
       jq(document).ready(function(e){
            
            jq("#sendSMS").click(function(e){
                
                jq.getJSON('${ui.actionLink("sendSms")}', {phoneNumbers1: phoneNumbers1, phoneNumbers2:phoneNumbers2},
                    function(response){
                        console.log(response);
                });
        
 
            });
       
       })
            
     
    </script>