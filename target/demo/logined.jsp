<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
   
    if (session == null || session.getAttribute("uname") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    String user = (String) session.getAttribute("uname");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Welcome <%= user %></title>
    <script>
        function showFields(action) {
            document.getElementById('depositForm').style.display = (action === 'deposit') ? 'block' : 'none';
            document.getElementById('balanceForm').style.display = (action === 'balance') ? 'block' : 'none';
            document.getElementById('transferForm').style.display = (action === 'transfer') ? 'block' : 'none';
        }
    </script>
    <link rel="stylesheet" href="css/logined.css">
</head>
<body>
    <h2>Welcome, <%= user %>!</h2>

    <button onclick="showFields('deposit')">Deposit</button>
    <button onclick="showFields('balance')">Check Balance</button>
    <button onclick="showFields('transfer')">Transfer</button>
    <form action="logout" method="post" style="display:inline;">
        <input type="submit" value="Logout">
    </form>

    <hr>

    <!-- Deposit Form -->
    <form id="depositForm" action="bank" method="post" style="display:none;">
        <input type="hidden" name="action" value="deposit">
        <label>Amount to Deposit:</label>
        <input type="number" name="amount" required>
        <button type="submit">Submit Deposit</button>
    </form>

    <!-- Balance Check Form -->
    <form id="balanceForm" action="bank" method="post" style="display:none;">
        <input type="hidden" name="action" value="balance">
        <button type="submit">Check Balance</button>
    </form>

    <!-- Transfer Form -->
    <form id="transferForm" action="bank" method="post" style="display:none;">
        <input type="hidden" name="action" value="transfer">
        <label>Recipient Email:</label>
        <input type="email" name="to_email" required><br>
        <label>Amount to Transfer:</label>
        <input type="number" name="amount" required>
        <button type="submit">Transfer</button>
    </form>
</body>
</html>
