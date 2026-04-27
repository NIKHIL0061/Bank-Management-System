<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Account" %>
<%@ page import="util.DataManager" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.html");
        return;
    }
    String username = (String) session.getAttribute("user");
    Map<String, Account> accounts = DataManager.loadAccounts();
    Account currentUser = accounts.get(username);
    List<String> history = currentUser.getTransactions();

    // Count deposits and withdrawals
    int deposits = 0, withdrawals = 0;
    double totalIn = 0, totalOut = 0;
    for (String tx : history) {
        String txLower = tx.toLowerCase();
        if (txLower.contains("deposit")) { deposits++; totalIn += extractAmount(tx); }
        else if (txLower.contains("withdraw")) { withdrawals++; totalOut += extractAmount(tx); }
    }
%>
<%!
    private double extractAmount(String tx) {
        try {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\$?([\\d,]+\\.?\\d*)").matcher(tx);
            if (m.find()) return Double.parseDouble(m.group(1).replace(",",""));
        } catch (Exception ignored) {}
        return 0;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>NexusBank — Dashboard</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>🏦</text></svg>">
</head>
<body class="dashboard-body">

<!-- ── TOP NAVIGATION BAR ── -->
<nav class="topbar">
  <a href="dashboard.jsp" class="topbar-brand">
    <div class="topbar-logo-icon">🏦</div>
    <span class="topbar-logo-text">Nexus<span>Bank</span></span>
  </a>

  <div class="topbar-nav">
    <a href="dashboard.jsp" class="active">Dashboard</a>
    <a href="dashboard.jsp">Transfers</a>
    <a href="dashboard.jsp">Statements</a>
    <a href="dashboard.jsp">Support</a>
  </div>

  <div class="topbar-actions">
    <div class="topbar-avatar"><%= currentUser.getUsername().substring(0,1).toUpperCase() %></div>
    <span class="topbar-username"><%= currentUser.getUsername() %></span>
    <a href="auth?action=logout" class="btn-logout">⏻ Logout</a>
  </div>
</nav>

<!-- ── MAIN CONTENT ── -->
<main class="dashboard-main">

  <!-- Alerts -->
  <%
    String error   = request.getParameter("error");
    String success = request.getParameter("success");
    if (error   != null) out.print("<div class='alert alert-error'>⚠️ "   + error   + "</div>");
    if (success != null) out.print("<div class='alert alert-success'>✅ " + success + "</div>");
  %>

  <!-- Welcome -->
  <div class="dash-welcome">
    <h1>Good day, <%= currentUser.getUsername() %> 👋</h1>
    <p>Here's a summary of your NexusBank account.</p>
  </div>

  <!-- Stats Row -->
  <div class="stats-row">

    <!-- Balance Card -->
    <div class="balance-card">
      <div class="balance-card-label">Available Balance</div>
      <div class="balance-amount">
        <span class="balance-currency">₹</span><%= String.format("%,.2f", currentUser.getBalance()) %>
      </div>
      <div class="balance-change">▲ Account Active</div>
      <div class="balance-account-no">XXXX XXXX <%= Math.abs(username.hashCode() % 10000) %></div>
    </div>

    <!-- Deposits -->
    <div class="stat-card">
      <div class="stat-card-icon icon-green">💰</div>
      <div class="stat-card-label">Total Deposits</div>
      <div class="stat-card-value"><%= deposits %></div>
      <div class="stat-card-sub">transactions</div>
    </div>

    <!-- Withdrawals -->
    <div class="stat-card">
      <div class="stat-card-icon icon-blue">💸</div>
      <div class="stat-card-label">Withdrawals</div>
      <div class="stat-card-value"><%= withdrawals %></div>
      <div class="stat-card-sub">transactions</div>
    </div>

    <!-- Total Txns -->
    <div class="stat-card">
      <div class="stat-card-icon icon-amber">📊</div>
      <div class="stat-card-label">Total Activity</div>
      <div class="stat-card-value"><%= history.size() %></div>
      <div class="stat-card-sub">all time</div>
    </div>

  </div>

  <!-- Actions + Transactions -->
  <div class="content-row">

    <!-- Quick Actions Panel -->
    <div class="panel">
      <div class="panel-header">
        <span class="panel-title">Quick Actions</span>
      </div>
      <div class="panel-body">

        <div class="action-tabs">
          <button class="action-tab active" onclick="switchTab('deposit', this)">⬆ Deposit</button>
          <button class="action-tab"        onclick="switchTab('withdraw', this)">⬇ Withdraw</button>
        </div>

        <!-- Deposit Form -->
        <div class="action-form-section active" id="tab-deposit">
          <form action="bank" method="POST">
            <input type="hidden" name="action" value="deposit">
            <label class="form-label">Amount to Deposit</label>
            <div class="amount-input-wrap">
              <span class="amount-prefix">₹</span>
              <input class="amount-input" type="number" name="amount" step="0.01" min="1"
                     placeholder="0.00" required>
            </div>
            <div style="font-size: 12px; color: var(--gray-300); margin-bottom: 16px;">
              Funds will be credited instantly
            </div>
            <button type="submit" class="btn btn-success">⬆ Deposit Funds</button>
          </form>
        </div>

        <!-- Withdraw Form -->
        <div class="action-form-section" id="tab-withdraw">
          <form action="bank" method="POST">
            <input type="hidden" name="action" value="withdraw">
            <label class="form-label">Amount to Withdraw</label>
            <div class="amount-input-wrap">
              <span class="amount-prefix">₹</span>
              <input class="amount-input" type="number" name="amount" step="0.01" min="1"
                     placeholder="0.00" required data-type="withdraw">
            </div>
            <div style="font-size: 12px; color: var(--gray-300); margin-bottom: 16px;">
              Available: ₹<%= String.format("%,.2f", currentUser.getBalance()) %>
            </div>
            <button type="submit" class="btn btn-danger">⬇ Withdraw Funds</button>
          </form>
        </div>

        <!-- Security note -->
        <div style="margin-top: 20px; padding: 12px 14px; background: var(--gray-50); border-radius: 10px; font-size: 12px; color: var(--gray-500); display: flex; align-items: center; gap: 8px;">
          🔒 All transactions are secured with 256-bit encryption
        </div>

      </div>
    </div>

    <!-- Transaction History Panel -->
    <div class="panel">
      <div class="panel-header">
        <span class="panel-title">Recent Transactions</span>
        <span class="panel-badge"><%= history.size() %> total</span>
      </div>
      <div class="panel-body" style="padding: 0;">

        <%
          if (history.isEmpty()) {
        %>
          <div class="tx-empty">
            <div class="tx-empty-icon">📭</div>
            <div class="tx-empty-text">No transactions yet.<br>Make your first deposit above!</div>
          </div>
        <%
          } else {
        %>
          <table class="tx-table">
            <thead>
              <tr>
                <th style="width:44px;"></th>
                <th>Description</th>
                <th>Status</th>
                <th style="text-align:right;">Amount</th>
              </tr>
            </thead>
            <tbody>
              <%
                int shown = 0;
                for (int i = history.size() - 1; i >= 0 && shown < 15; i--, shown++) {
                    String tx = history.get(i);
                    boolean isDeposit = tx.toLowerCase().contains("deposit");
                    String icon      = isDeposit ? "⬆" : "⬇";
                    String iconClass = isDeposit ? "tx-icon-credit" : "tx-icon-debit";
                    String amtClass  = isDeposit ? "credit" : "debit";
                    String sign      = isDeposit ? "+" : "-";
              %>
              <tr>
                <td>
                  <div class="tx-icon <%= iconClass %>"><%= icon %></div>
                </td>
                <td>
                  <div class="tx-desc"><%= isDeposit ? "Deposit" : "Withdrawal" %></div>
                  <div class="tx-date"><%= tx.contains("|") ? tx.split("\\|")[1].trim() : "Bank Transaction" %></div>
                </td>
                <td>
                  <span class="tx-badge success">Completed</span>
                </td>
                <td>
                  <div class="tx-amount <%= amtClass %>">
                    <%= sign %>₹<%= tx.replaceAll(".*?(\\d+\\.?\\d*).*","$1") %>
                  </div>
                </td>
              </tr>
              <% } %>
            </tbody>
          </table>
        <% } %>

      </div>
    </div>

  </div>

  <!-- Security Footer -->
  <div class="security-bar">
    🔒 &nbsp;256-bit SSL &nbsp;|&nbsp; 🛡️ RBI Regulated &nbsp;|&nbsp;
    🏦 NexusBank Pvt. Ltd. &nbsp;|&nbsp; IFSC: NEXUS0001234 &nbsp;|&nbsp;
    📞 1800-XXX-XXXX (Toll Free)
  </div>

</main>

<script>
  function switchTab(name, btn) {
    document.querySelectorAll('.action-form-section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.action-tab').forEach(b => b.classList.remove('active'));
    document.getElementById('tab-' + name).classList.add('active');
    btn.classList.add('active');
  }
</script>

</body>
</html>
