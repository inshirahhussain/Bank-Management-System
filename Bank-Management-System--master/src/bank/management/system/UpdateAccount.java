package bank.management.system;

import javax.swing.*;
import java.sql.*;

public class UpdateAccount {
    public static void updateAccount(String pin) {
        try {
            String newName = JOptionPane.showInputDialog("Enter new name:");

            if (newName == null || newName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Update cancelled or empty input!");
                return;
            }

            // Database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/banksystem", "root", "Admin");

            // Step 1: Get form_no using the pin from signupthree
            String formQuery = "SELECT form_no FROM signupthree WHERE pin = ?";
            PreparedStatement pst1 = conn.prepareStatement(formQuery);
            pst1.setString(1, pin);
            ResultSet rs = pst1.executeQuery();

            if (rs.next()) {
                String formNo = rs.getString("form_no");

                // Step 2: Update name in signup table using form_no
                String updateQuery = "UPDATE signup SET name = ? WHERE form_no = ?";
                PreparedStatement pst2 = conn.prepareStatement(updateQuery);
                pst2.setString(1, newName);
                pst2.setString(2, formNo);

                int result = pst2.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Account name updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update account.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No account found with the provided PIN.");
            }

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
