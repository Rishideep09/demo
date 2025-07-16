import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

public class AddServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
       res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            String uname = (req.getParameter("email")).toString();
           int  pass =Integer.parseInt(req.getParameter("pass"));
                
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             HttpSession session = req.getSession();
            String query ="Select * from users where email=? and password=?;";
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "1234");
            PreparedStatement pp = con.prepareStatement(query);
            pp.setString(1, uname);
             pp.setInt(2, pass);
            ResultSet rr = pp.executeQuery();

            if(rr.next())
            {
               
                session.setAttribute("uname", uname);
                
                res.sendRedirect("logined.jsp");
            }
            else{
                out.println("Not found");
                res.sendRedirect("register.jsp");
            }

        } 
        catch (NumberFormatException e) {
            out.println("<h1>Invalid input</h1>");
        }
        catch(SQLException e)
        {
            out.println("Sql Exception hii \n "+e.getMessage());
        }
    }
}
