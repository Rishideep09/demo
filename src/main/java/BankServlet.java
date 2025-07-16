import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/bank")
public class BankServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("uname") == null) {
            out.println("<script>alert('Please log in first.'); window.location='index.jsp';</script>");
            return;
        }

        String email = session.getAttribute("uname").toString();
        String action = req.getParameter("action");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "1234");

            switch (action) {
                case "deposit":
                    int depositAmount = Integer.parseInt(req.getParameter("amount"));
                    PreparedStatement dp = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE email = ?");
                    dp.setInt(1, depositAmount);
                    dp.setString(2, email);
                    dp.executeUpdate();
                    out.println("<script>alert('Deposited ₹" + depositAmount + " successfully.'); window.location='logined.jsp';</script>");
                    break;

                case "balance":
                    PreparedStatement bp = con.prepareStatement("SELECT balance FROM accounts WHERE email = ?");
                    bp.setString(1, email);
                    ResultSet brs = bp.executeQuery();
                    if (brs.next()) {
                        int balance = brs.getInt("balance");
                        out.println("<script>alert('Your current balance is ₹" + balance + "'); window.location='logined.jsp';</script>");
                    }
                    break;

                case "transfer":
                    String toEmail = req.getParameter("to_email");
                    int transferAmount = Integer.parseInt(req.getParameter("amount"));

                    con.setAutoCommit(false);
                    PreparedStatement withdraw = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE email = ? AND balance >= ?");
                    withdraw.setInt(1, transferAmount);
                    withdraw.setString(2, email);
                    withdraw.setInt(3, transferAmount);
                    int withdrawSuccess = withdraw.executeUpdate();

                    PreparedStatement deposit = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE email = ?");
                    deposit.setInt(1, transferAmount);
                    deposit.setString(2, toEmail);
                    int depositSuccess = deposit.executeUpdate();

                    if (withdrawSuccess > 0 && depositSuccess > 0) {
                        con.commit();
                        out.println("<script>alert('Transferred ₹" + transferAmount + " to " + toEmail + "'); window.location='logined.jsp';</script>");
                    } else {
                        con.rollback();
                        out.println("<script>alert('Transfer failed. Check balance or recipient email.'); window.location='logined.jsp';</script>");
                    }

                    con.setAutoCommit(true);
                    break;

                default:
                    out.println("<script>alert('Invalid action'); window.location='logined.jsp';</script>");
            }

        } catch (Exception e) {
            out.println("<script>alert('Error: " + e.getMessage().replace("'", "") + "'); window.location='logined.jsp';</script>");
        }
    }
}
