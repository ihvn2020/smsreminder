
<div style="text-align: center;"><button class="btn btn-success" id="sendSMS" style="text-align: center;">Send SMS</button></div>
<hr>
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
    
                if(nodays==1){
                    var message='A';
                }else{
                    var message='AA';
                }
                    
                    if(cleanedPhoneNumber.length>=10){                      
                        console.log(cleanedPhoneNumber);
    
                            cleanedPhoneNumber.indexOf( '0' ) == 0 ? cleanedPhoneNumber = cleanedPhoneNumber.replace( '0', '234' ) : cleanedPhoneNumber;
                            if(cleanedPhoneNumber.substring(0,3)!='234'){
                                cleanedPhoneNumber = '234'+cleanedPhoneNumber;
                            }
    
                            if(nodays==1){
                                if(i==patients.length-1){               
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
               // }
    
                console.log(phoneNumbers1);
                console.log(phoneNumbers2);
    
           <% } %>
       
           
       jq(document).ready(function(e){
            
            jq("#sendSMS").click(function(e){
                
                // updateSentStatus();
                var appid = Date.now();
                
                var url1 = 'https://api2.infobip.com/api/sendsms/plain?user=SteveJ&password=Health%4012345&type=LongSMS&sender=IHVN&SMSText=A&appid='+appid+'&GSM='+phoneNumbers1;
                
                
    
                var url2 = 'https://api2.infobip.com/api/sendsms/plain?user=SteveJ&password=Health%4012345&type=LongSMS&sender=IHVN&SMSText=A&appid='+appid+'&GSM='+phoneNumbers2;
                
                console.log(url1+","+url2);
                jq.ajax({
                    url: url1,
    
                    dataType: "jsonp",
                    success: function( response ) {
                        console.log( response ); // server response
                    }
    
                });
    
                jq.ajax({
                    url: url2,
    
                    dataType: "jsonp",
                    success: function( response ) {
                        console.log( response ); // server response
                    }
    
                });
    
            });
       
       })
            
            
      function updateSentStatus()
      { 
        jq.ajax({
            url:'${ ui.actionLink("updateSentStatus") }',
            type:"GET",
            data:{},
            success:function(response){
                console.log(response)
                
            },
            error:function(xhr, status, error){
    
            }
        })
      }
    </script>