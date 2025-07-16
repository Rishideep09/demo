import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html"); // ✅ CORRECT content type
        PrintWriter out = res.getWriter();

        try {
            String uname = req.getParameter("username");
            String email = req.getParameter("email");
            int pass = Integer.parseInt(req.getParameter("passw"));

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "1234");

            String query = "INSERT INTO users VALUES (?, ?, ?)";
            PreparedStatement pp = con.prepareStatement(query);
            pp.setString(1, uname);
            pp.setString(2, email);
            pp.setInt(3, pass);

            int rr = pp.executeUpdate();

            if (rr > 0) 
            {
              
                HttpSession session = req.getSession();
                session.setAttribute("uname", uname);
                session.setAttribute("pass", pass);
                PreparedStatement pp1=con.prepareStatement("Select  account_num from accounts where email=? and password=?;");
                pp1.setString(1,uname);
                pp1.setInt(2, pass);
                ResultSet rr1= pp1.executeQuery();
                if(rr1.next())
                {
                    res.sendRedirect("logined.jsp");
                }
                else
                {
                    
        String query2 = "Select account_num from accounts order by account_num desc limit 1;";
        Statement st =  con.createStatement();
        ResultSet rr2 = st.executeQuery(query2);
        long acc;
        if(rr2.next()) 
        { 
         acc =   rr2.getLong("account_num")+1;
          }
          else{
             acc= 10000110;
          }

          String ss= "Insert into accounts(account_num,name,email,password,balance) values(?,?,?,?,?);";
          PreparedStatement pp2 = con.prepareStatement(ss);
          pp2.setLong(1, acc);
          pp2.setString(2,uname);
          pp2.setString(3,email);
          pp2.setInt(4, pass);
          pp2.setDouble(5,1000);
          int k = pp2.executeUpdate();
          if(k>0)
          {
                out.println("<script type='text/javascript'>");
                out.println("alert('Registration Successful');");
                 out.println("window.location.href = '" + req.getContextPath() + "/index.jsp';");
                out.println("</script>");

          }
          else {
    out.println("<script type='text/javascript'>");
    out.println("alert('Contact Admin Rishi');");
    out.println("window.location.href = '" + req.getContextPath() + "/register.jsp';");
    out.println("</script>");
}


        }

               
            } 
            else 
            {
                out.println("<script type='text/javascript'>");
                out.println("alert('Registration FAILED');");
                // out.println("window.location.href = 'register.jsp';");
                out.println("</script>");
            }

        } catch (NumberFormatException e) {
            out.println("<h1>Invalid input</h1>");
        } catch (SQLException | ClassNotFoundException e)
         {
            out.println("<script type='text/javascript'>");
            out.println("alert('Database error: " + e.getMessage().replace("'", "\\'") + "');");
            // out.println("window.location.href = 'register.jsp';");
            out.println("</script>");
        }
    }
}

