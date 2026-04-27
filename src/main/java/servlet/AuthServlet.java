package servlet;

import model.Account;
import util.DataManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Map<String, Account> accounts = DataManager.loadAccounts();

        if ("register".equals(action)) {
            if (accounts.containsKey(username)) {
                response.sendRedirect("register.html?error=User+already+exists");
            } else {
                accounts.put(username, new Account(username, password));
                DataManager.saveAccounts(accounts);
                response.sendRedirect("index.html?success=Account+created.+Please+login.");
            }
        } else if ("login".equals(action)) {
            Account acc = accounts.get(username);
            if (acc != null && acc.getPassword().equals(password)) {
                // Create a session for the logged-in user
                HttpSession session = request.getSession();
                session.setAttribute("user", acc.getUsername());
                response.sendRedirect("dashboard.jsp");
            } else {
                response.sendRedirect("index.html?error=Invalid+credentials");
            }
        } else if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();
            response.sendRedirect("index.html?success=Logged+out+successfully");
        }
    }
    
    // Allow logout via GET request as well
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}