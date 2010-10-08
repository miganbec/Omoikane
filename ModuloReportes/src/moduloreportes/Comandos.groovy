/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package moduloreportes

/**
 *
 * @author Octavio
 */

import java.sql.*;
import javax.swing.*;

public class Comandos {

    static def config
    public static String login 
    public static String password 
    public static String url

    public static Connection Enlace(Connection conn) throws SQLException {
        try {
            config = new moduloreportes.Config()
            defineAtributos()
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, login, password);
        } catch (ClassNotFoundException c) {
            JOptionPane.showMessageDialog(null, c);
        }
        return conn;
    }

    static def defineAtributos() {
            login      = String .valueOf(config.login[0].text())
            password   = String .valueOf(config.password[0].text())
            url        = String .valueOf(config.url[0].text())
    }
}
	


