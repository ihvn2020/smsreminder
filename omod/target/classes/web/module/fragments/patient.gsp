<hr>
<div class="row">
    
            <% 
                String allphonenos = "";
                String allappdates = "";
                if(patients!= null){
                    for(int i=0; i<patients.size(); i++)
                    { 
                        if(i == patients.size() - 1){
                            allphonenos = allphonenos+patients.get(i).get("phone_number"); 
                            allappdates = allappdates+patients.get(i).get("next_date"); 
                        }else{
                            allphonenos = allphonenos+patients.get(i).get("phone_number")+","; 
                            allappdates = allappdates+patients.get(i).get("next_date")+","; 
                        }
                    }  
                }
            %>
                    
            <input type="hidden" name="recipients" id="recipients" class="form-control" value="<%=allphonenos%>" >
            <input type="hidden" name="nextdates" id="nextdates" class="form-control" value="<%=allappdates%>">

            
            <div  style="text-align: center;"><button class="btn btn-success center" id="sendSMS">Send SMS</button><br><small style="font-size: 11px; color: orange;">&#9432; Click to Send to All Selected</small></div>
            
            <a href="/smsreminder/numberchecks.page" style="float: right">Phone Number Validation >></a>
            <a href="/smsreminder/invalidnumbers.page" style="float: right; color: red;">Invalid Numbers  | </a>
            
        
            
        <h5 style="text-align: center; color: green; font-size: smaller;">Last SMS Sent Date: <%= dformatter.formatDate(lastSentDate) %></h5>
        <table>
            <thead>
                <tr> 
                    <th>Select</th><th>Pepfar ID</th><th>Hospital No</th><th>Phone number</th><th>Next Appointment Date</th><th>Status/Action</th>
                </tr>
            </thead>
            
            <tbody>
                <% 
                if(patients!= null){
                for(int i=0; i<patients.size(); i++)
                {                   
                    %>
                <tr>
                    <td><input type="checkbox" value="select" onClick="addNumber('<%= patients.get(i).get('phone_number')+"','"+patients.get(i).get('next_date'); %>')" checked ></td>
                    <td><%= patients.get(i).get("pepfar_id"); %></td>
                    <td><%= numbers.get(i).get("hospitalNumber"); %></td>
                        <td><%= patients.get(i).get("phone_number"); %></td>
                        <td><%= dformatter.formatDate(patients.get(i).get("next_date")); %></td>
                        <td>
                            
                            <% if(patients.get(i).get("phone_number")=="" || patients.get(i).get("phone_number").length()<11  || dformatter.isNumeric(patients.get(i).get("phone_number"))==false){ %>
                                <a href="/openmrs/registrationapp/editSection.page?patientId=${patients.get(i).get("patient_id")}&sectionId=contactInfo&appId=referenceapplication.registrationapp.registerPatient" class="btn btn-warning small"  style="color: orange">Edit Invalid Number</a>
                            <% }else{ %>
                                <a href="/openmrs/registrationapp/editSection.page?patientId=${patients.get(i).get("patient_id")}&sectionId=contactInfo&appId=referenceapplication.registrationapp.registerPatient" class="btn btn-success" style="color: green"> &#10004; Valid</a>
                            <% } %>
                        </td>
                </tr>
            <% }} %>
            
            </tbody>
        </table>

        
    
</div>


    
<script type="text/javascript">


    var jq = jQuery;
<% if(patients != null){%>
    jq(document).ready(function(e){
            
        jq("#sendSMS").click(function(e){

            var receivers = jq('#recipients').val();
            var nxtappdates = jq('#nextdates').val();
            // var allPatients = new Array();
            var phoneNumbers1 = "";
            var phoneNumbers2 = "";
            var cleanedPhoneNumber = "";            
                
            var d = new Date();
            var today = new Date(d.getFullYear() + "/" + (d.getMonth()+1) + "/" + d.getDate());
            var patients = []; 
            var nextdates = []; 

            if(receivers==""){
                
                <% for(int i=0; i<patients.size(); i++)
                    { %>
                    // allPatients.push("<%= patients.get(i).get("phone_number") %>");
                        
                    cleanedPhoneNumber = "<%= patients.get(i).get("phone_number") %>";
                        
                        var next_appointment = new Date("<%= patients.get(i).get("next_date") %>");
                    
                        var daydiff = (next_appointment-today) / (1000 * 60 * 60 * 24);
                        var nodays = Math.round(daydiff);
            
                        // alert(nodays+"%");


                
                            if(cleanedPhoneNumber.length>=10){                      
                                // console.log(cleanedPhoneNumber);
            
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
            
                        
            
                <% } %>                
            }else{
            
                // alert(JSON.stringify(nxtappdates));

                patients = receivers.split(",");
                nextdates = nxtappdates.split(",");

                for (let i = 0; i < patients.length; i++) {
                   
                        
                        cleanedPhoneNumber = patients[i];
                        
                        var next_appointment = new Date(nextdates[i]);
                    
                        var daydiff = (next_appointment-today) / (1000 * 60 * 60 * 24);
                        var nodays = Math.round(daydiff);

                       
                        // console.log(cleanedPhoneNumber+" No of Days "+nodays);
                
                            if(cleanedPhoneNumber.length>=10){                      
                                // console.log(cleanedPhoneNumber);
            
                                    cleanedPhoneNumber.indexOf( '0' ) == 0 ? cleanedPhoneNumber = cleanedPhoneNumber.replace( '0', '234' ) : cleanedPhoneNumber;
                                    if(cleanedPhoneNumber.substring(0,3)!='234'){
                                        cleanedPhoneNumber = '234'+cleanedPhoneNumber;
                                    }
            
                                    if(nodays==1){
                                        if(i==patients.length){               
                                            phoneNumbers1 += cleanedPhoneNumber;
                                        }else{
                                            phoneNumbers1 += cleanedPhoneNumber+",";  
                                        }
                                    }else{
                                        if(i==patients.length-1){               
                                            phoneNumbers2 += cleanedPhoneNumber;
                                        }else{
                                            phoneNumbers2 += cleanedPhoneNumber+",";  
                                        }
                                    }
            
                            }
                        
            
                }                     
               

            }
            
            jq.getJSON('${ui.actionLink("sendSms")}', {phoneNumbers1: phoneNumbers1, phoneNumbers2:phoneNumbers2},
                    function(response){
                        console.log(response);
                    
                alert("SMS Sent Successfully, Click OK continue!");
            });
                    
            

        });
       
    });
            
            
    function addNumber(number,nextdate){
        var receivers = jq('#recipients').val();
        var nextdates = jq('#nextdates').val();
            
        
    
        if(jq("#recipients").val().indexOf(','+number) >= 0){
            
            jq('#recipients').val(receivers.replace(','+number,''));
            jq('#nextdates').val(nextdates.replace(','+nextdate,''));
                
        }else if(jq("#recipients").val().indexOf(number+',') >= 0){

            jq('#recipients').val(receivers.replace(number+',',''));
            jq('#nextdates').val(nextdates.replace(nextdate+',',''));
            
        }else if(jq("#recipients").val().indexOf(number) >= 0){            
            
            jq('#recipients').val(receivers.replace(number,''));
            jq('#nextdates').val(nextdates.replace(nextdate,''));
            
        }else{
            if(receivers==""){
                
                jq('#recipients').val(number);
                jq('#nextdates').val(nextdate);
            }else{
                jq('#recipients').val(receivers+','+number);
                jq('#nextdates').val(nextdates+','+nextdate);
            }
            
        }   
        
    }
            
<% } %>
    
       
     
    </script>