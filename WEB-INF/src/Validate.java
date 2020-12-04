import java.sql.*;

//https://javarevisited.blogspot.com/2016/09/javasqlsqlexception-no-suitable-driver-mysql-jdbc-localhost.html
public class Validate {
    public static boolean checkUser(String email,String pass) 
    {
        boolean st =false;
        try {

            //loading drivers for mysql
            Class.forName("com.mysql.jdbc.Driver");

            //creating connection with the database
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?verifyServerCertificate=false&useSSL=true","user","studytonight"); //fallo, no usa ssl
            PreparedStatement ps = con.prepareStatement("select * from register where email=? and pass=?");
            ps.setString(1, email);
            ps.setString(2, pass);
            ResultSet rs =ps.executeQuery();
            st = rs.next();

        }
        catch(Exception e) {
            System.out.println("hola!!!!!!");
            e.printStackTrace();
        }
        return st;                 
    }   
}
