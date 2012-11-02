package omoikane.repository;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import javax.swing.*;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 01/11/12
 * Time: 19:47
 * To change this template use File | Settings | File Templates.
 */
public class CreateFixturesFromDB {

    /**
     * Cambiar los datos de la conexión de acuerdo a la base de datos que se quiera usar como muestra
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        String url  = JOptionPane.showInputDialog("Escriba la URL de MySQL incluyendo driver");
        String user = JOptionPane.showInputDialog("Escriba el nombre de usuario de la BD");
        String pass = JOptionPane.showInputDialog("Escriba la clave de la BD");
        String dir  = "test/src/omoikane/repository/sampleData.xml";

        Class driverClass = Class.forName("com.mysql.jdbc.Driver");
        Connection jdbcConnection = DriverManager.getConnection(
                url, user, pass);
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        // Datos seleccionados considerando la base de datos de Omoikane 1.3.3 -> 1.4.0 alpha
        QueryDataSet partialDataSet = new QueryDataSet(connection);
        partialDataSet.addTable("articulos"        , "SELECT * FROM articulos         ORDER BY id_articulo DESC LIMIT 100");
        partialDataSet.addTable("base_para_precios", "SELECT * FROM base_para_precios ORDER BY id_articulo DESC LIMIT 100");
        FlatXmlDataSet.write(partialDataSet, new FileOutputStream(dir));
        System.out.println("{}");
        JOptionPane.showMessageDialog(null, "Proceso completado. BD importada en "+dir);

    }
}
