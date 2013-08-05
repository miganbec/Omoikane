package omoikane.artemisa.presentation

import javafx.embed.swing.JFXPanel
import javafx.event.EventHandler
import javafx.scene.control.Hyperlink
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import omoikane.principal.Articulos
import omoikane.principal.Escritorio

import javax.swing.JFrame;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 27/07/13
 * Time: 04:24 PM
 * To change this template use File | Settings | File Templates.
 */

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import omoikane.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired
import omoikane.sistema.Permisos;


public class UIManagerController
        implements Initializable {

    @FXML
    private AnchorPane contentPane;

    @FXML
    private VBox menuVBox;

    public UIManagerController() {
    }


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        assert contentPane != null : "fx:id=\"contentPane\" was not injected: check your FXML file 'UIManagerView.fxml'.";

        Tooltip tooltipPacientes = new Tooltip("hola");

        def menu = [
                [icono: "/omoikane/artemisa/images/address-book.png", vista: "pacientesView", permiso: Permisos.PMA_ABRIRPACIENTES],
                [icono: "/omoikane/artemisa/images/coin.png"        , vista: "cajaClinicaView", permiso: Permisos.PMA_CAJACLINICA],
                [icono: "/omoikane/artemisa/images/aid.png"         , vista: "consumoView", permiso: Permisos.PMA_CONSUMOPACIENTE],
                [icono: "/omoikane/artemisa/images/print.png"       , vista: "reportesView", permiso: Permisos.PMA_ARTEMISA_REPORTES],
                [icono: "/omoikane/artemisa/images/barcode.png"     , comando: { new CatalogoArticulosManager().show() }, permiso: Permisos.PMA_ARTEMISA_ARTICULOS]
        ]

        generarMenu(menu)

    }

    private void generarMenu(ArrayList<LinkedHashMap<String, String>> menu) {
        menu.each { def elemento ->
            Hyperlink link = new Hyperlink("");
            Image ico = new Image(elemento.icono);
            ImageView iv = new ImageView();
            iv.setImage(ico)
            link.setGraphic(iv)
            menuVBox.getChildren().addAll(menuVBox.getChildren().size() - 2, link);
            link.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                void handle(ActionEvent t) {
                    if(elemento.permiso != null)
                        if(!omoikane.sistema.Usuarios.cerrojo((Object) elemento.permiso)) return ;
                    if(elemento.vista != null)
                        cargarVista(elemento.vista);
                    if(elemento.comando != null)
                        elemento.comando()
                }
            })
        }
    }

    public void cargarVista(String vista) {
        Scene cajaClinicaView = (Scene) Principal.applicationContext.getBean( vista );
        Parent parent = cajaClinicaView.getRoot();
        AnchorPane.setBottomAnchor(parent, 0d);
        AnchorPane.setLeftAnchor(parent, 0d);
        AnchorPane.setRightAnchor(parent, 0d);
        AnchorPane.setTopAnchor(parent, 0d);
        Button dummyButton = new Button("");
        contentPane.getChildren().removeAll();
        contentPane.getChildren().addAll(parent, dummyButton);
        contentPane.getChildren().remove(dummyButton);
        parent.setVisible(true);
    }

}
