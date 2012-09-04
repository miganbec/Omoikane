/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.moduloreportes

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
            c.printStackTrace();
            JOptionPane.showMessageDialog(null, c);
        }
        return conn;
    }

    static def defineAtributos() {
            login      = String .valueOf(config.loginJasper[0].text())
            password   = String .valueOf(config.passJasper[0].text())
            url        = String .valueOf(config.URLMySQL[0].text())
    }
}
	


