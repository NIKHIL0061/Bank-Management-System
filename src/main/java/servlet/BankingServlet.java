package servlet;

import model.Account;
import util.DataManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/bank")
public class BankingServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("index.html");
            return;
        }

        String username = (String) session.getAttribute("user");
        String action = request.getParameter("action");
        double amount = Double.parseDouble(request.getParameter("amount"));

        Map<String, Account> accounts = DataManager.loadAccounts();
        Account acc = accounts.get(username);

        if (acc != null) {
            if ("deposit".equals(action)) {
                acc.deposit(amount);
            } else if ("withdraw".equals(action)) {
                if (!acc.withdraw(amount)) {
                    response.sendRedirect("dashboard.jsp?error=Insufficient+Funds");
                    return;
                }
            }
            // Save updated account state
            DataManager.saveAccounts(accounts);
            response.sendRedirect("dashboard.jsp?success=Transaction+Complete");
        }
    }
}