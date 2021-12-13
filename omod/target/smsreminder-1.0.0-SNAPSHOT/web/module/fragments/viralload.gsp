<table>
  <tr>
   <th>PepfarID</th>
   <th>Viral Load</th>
  </tr>
  <% if (patientVlData) { %>
     <% patientVlData.each { %>
      <tr>
        <td>${ ui.format(it.pepfarID) }</td>
        <td>${ ui.format(it.vlresult) }</td>
      </tr>
    <% } %>
  <% } else { %>
  <tr>
    <td colspan="2">${ ui.message("general.none") }</td>
  </tr>
  <% } %>
</table>
