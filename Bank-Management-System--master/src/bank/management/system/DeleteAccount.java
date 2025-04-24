package bank.management.system;

import javax.swing.*;
import java.sql.*;

public class DeleteAccount {
    public static void deleteAccount(String pin) {
        try {
            // Database connection setup
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/banksystem", "root", "Admin");

            // Step 1: Find the form_no from signupthree using the provided PIN
            String getFormNo = "SELECT form_no FROM signupthree WHERE pin = ?";
            PreparedStatement pst1 = conn.prepareStatement(getFormNo);
            pst1.setString(1, pin);
            ResultSet rs = pst1.executeQuery();

            if (rs.next()) {
                String formNo = rs.getString("form_no");

                // Step 2: Delete related data from all tables using form_no
                String[] deleteQueries = {
                        "DELETE FROM signup WHERE form_no = ?",
                        "DELETE FROM signuptwo WHERE form_no = ?",
                        "DELETE FROM signupthree WHERE form_no = ?",
                        "DELETE FROM login WHERE form_no = ?"
                };

                for (String query : deleteQueries) {
                    PreparedStatement pst = conn.prepareStatement(query);
                    pst.setString(1, formNo);
                    pst.executeUpdate();
                }

                // Step 3: Delete related bank transactions using the pin
                String deleteBankQuery = "DELETE FROM bank WHERE pin = ?";
                PreparedStatement pstBank = conn.prepareStatement(deleteBankQuery);
                pstBank.setString(1, pin);
                pstBank.executeUpdate();

                JOptionPane.showMessageDialog(null, "Account Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Account not found!");
            }

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
