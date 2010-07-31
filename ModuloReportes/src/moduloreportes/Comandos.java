/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moduloreportes;

/**
 *
 * @author Phesus-Lab
 */
import java.sql.*;
import javax.swing.*;


public class Comandos {

    static String bd = "Omoikane";
    static String login = "ZooMMX";
    static String password = "felpas24";
    static String url = "jdbc:mysql://supermedina.com/" + bd + "?useCompression=true";

    public static Connection Enlace(Connection conn) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, login, password);
        } catch (ClassNotFoundException c) {
            JOptionPane.showMessageDialog(null, c);
        }
        return conn;
    }
}