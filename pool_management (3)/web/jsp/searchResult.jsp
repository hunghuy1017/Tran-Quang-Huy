<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Event, models.SwimmingPool, models.Employee" %>
<%
    String keyword = (String) request.getAttribute("keyword");
    List<Event> events = (List<Event>) request.getAttribute("events");
    List<SwimmingPool> pools = (List<SwimmingPool>) request.getAttribute("pools");
    List<Employee> trainers = (List<Employee>) request.getAttribute("trainers");
%>

<h2>Kết quả tìm kiếm cho: "<%= keyword %>"</h2>

<h3>Sự kiện:</h3>
<ul>
<% for(Event e : events) { %>
    <li><strong><%= e.getTitle() %></strong> - <%= e.getEventDate() %></li>
<% } %>
</ul>

<h3>Hồ bơi:</h3>
<ul>
<% for(SwimmingPool p : pools) { %>
    <li><strong><%= p.getName() %></strong> - <%= p.getLocation() %></li>
<% } %>
</ul>

<h3>Huấn luyện viên:</h3>
<ul>
<% for(Employee t : trainers) { %>
    <li><strong><%= t.getFullName() %></strong> - <%= t.getEmail() %></li>
<% } %>
</ul>
