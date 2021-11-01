<div class="row">
    
        <h5 style="text-align: center; color: green; font-size: smaller;">Last Phone Number Validation: <%= dformatter.formatDate(lastNumberChecks) %></h5>
        <div style="text-align: center"><button class="btn btn-success" id="saveChanges">Save Changes</button></div>

        <table>
            <thead>
                <tr> 
                    <th>Patient ID</th><th>Phone number</th><th>Comment/Remarks</th><th>Status/Action</th>
                </tr>
            </thead>
            
            <tbody>
                <% for(int i=0; i<numbers.size(); i++)
                { %>
                <tr>
                    <td><%= numbers.get(i).get("patient_id"); %></td>
                    <td><a href="tel:<%= numbers.get(i).get("phone_number"); %>"><%= numbers.get(i).get("phone_number"); %> &phone;</a></td>
                    <td><input id="comment" placeholder="Enter Comments"/></td>
        
                    <td>
                        <% if(numbers.get(i).get("phone_number")=="" || numbers.get(i).get("phone_number").length()<10 || dformatter.isNumeric(numbers.get(i).get("phone_number"))==false){ %>
                            <a href="/openmrs/registrationapp/editSection.page?patientId=${numbers.get(i).get("patient_id")}&sectionId=contactInfo&appId=referenceapplication.registrationapp.registerPatient" class="btn btn-warning">Edit Invalid Number</a>
                        <% }else{ %>
                            <a href="/openmrs/registrationapp/editSection.page?patientId=${numbers.get(i).get("patient_id")}&sectionId=contactInfo&appId=referenceapplication.registrationapp.registerPatient" class="btn btn-success" style="color: green"> &#10004; Valid</a>
                        <% } %>
                    </td>
                        
                </tr>
            <% } %>
            
            </tbody>
        </table>

    
    
</div>


    
