package connectiontodatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class jdbcexample {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "root";
        String password = "Hiba101220";

        Connection conn = null;
        Statement stmt = null;

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            System.out.println("Connected!");

            String createUsersBasicTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "name VARCHAR(100), "
                    + "email VARCHAR(100), "
                    + "age INT"
                    + ")";
            stmt.executeUpdate(createUsersBasicTable);
            System.out.println("Table 'users' (basic) created or already exists.");

 
            String insertData = "INSERT INTO users (username, email, age) VALUES "
                    + "('Alice', 'alice@example.com', 25),"
                    + "('Bob', 'bob@example.com', 30)";
            stmt.executeUpdate(insertData);
            System.out.println("Sample data inserted into 'users'.");

   
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            System.out.println("Data in 'users' table:");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("email") + " | " +
                        rs.getInt("age")
                );
            }

         
            String createUserCredentialsTable = "CREATE TABLE IF NOT EXISTS user_credentials ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "username VARCHAR(50) NOT NULL UNIQUE, "
                    + "password VARCHAR(255) NOT NULL"
                    + ")";
            stmt.executeUpdate(createUserCredentialsTable);
            System.out.println("Table 'user_credentials' created successfully.");

            String createClientsTable = "CREATE TABLE IF NOT EXISTS clients ("
                    + "client_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "user_id INT NOT NULL, "
                    + "FOREIGN KEY (user_id) REFERENCES user_credentials(id)"
                    + ")";
            stmt.executeUpdate(createClientsTable);
            System.out.println("Table 'clients' created successfully.");
            
            
            
            String sql = "CREATE TABLE IF NOT EXISTS client_addresses ("
                    + "address_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "client_id INT NOT NULL, "
                    + "address TEXT NOT NULL, "
                    + "FOREIGN KEY (client_id) REFERENCES clients(client_id)"
                    + ")";

         stmt.executeUpdate(sql);
         System.out.println("Table 'client_addresses' created successfully.");

         
         String clientpaymentmethods = "CREATE TABLE IF NOT EXISTS client_payment_methods ("
                 + "id INT AUTO_INCREMENT PRIMARY KEY, "
                 + "client_id INT NOT NULL, "
                 + "payment_method_id INT NOT NULL, "
                 + "FOREIGN KEY (client_id) REFERENCES clients(client_id), "
                 + "FOREIGN KEY (payment_method_id) REFERENCES payment_methods(method_id)"
                 + ")";

      
      stmt.executeUpdate(clientpaymentmethods);
      System.out.println("Table 'client_payment_methods' created successfully.");
      
      
      String paymentmethods = "CREATE TABLE IF NOT EXISTS payment_methods ("
              + "method_id INT AUTO_INCREMENT PRIMARY KEY, "
              + "card_number VARCHAR(20), "
              + "cvv2 VARCHAR(4), "
              + "expiry_month INT, "
              + "expiry_year INT"
              + ")";

 
   stmt.executeUpdate(paymentmethods);
   System.out.println("Table 'payment_methods' created successfully.");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources safely
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    String DataType;
    public void EnterData(String DataType)throws Exception{
        Scanner ent = new Scanner(System.in);
        String name; 
        
        
    }
}
