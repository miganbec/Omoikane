package omoikane.etiquetas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import omoikane.etiquetas.presentation.EditableTableCell;
import omoikane.etiquetas.presentation.NumericEditableTableCell;
import omoikane.principal.Principal;
import omoikane.producto.*;
import omoikane.repository.ProductoRepo;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 11/03/13
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class ImpresionEtiquetasController implements Initializable {

    public Logger logger = Logger.getLogger(getClass());

    private ImpresionEtiquetasModel model;

    private ProductoRepo productoRepo;

    ObservableList<ImpresionEtiquetasModel> articulos;

    @FXML private TableColumn<ImpresionEtiquetasModel, String> codigoCol;
    @FXML private TableColumn<ImpresionEtiquetasModel, Long> cantidadCol;
    @FXML private TableColumn<ImpresionEtiquetasModel, String> productoCol;

    @FXML //  fx:id="addButton"
    private Button addButton;

    @FXML //  fx:id="removeButton"
    private Button removeButton;

    @FXML // fx:id="seguienteButton"
    private Button seguienteButton;

    @FXML //  fx:id="tipoEtiqueta"
    private ComboBox tipoEtiqueta;

    @FXML //  fx:id="contenidoTable"
    private TableView<ImpresionEtiquetasModel> tablaContenido;

    private List<Articulo> articulosToReport;

    @FXML
    private void actionAdd() {
        ImpresionEtiquetasModel impresionEtiquetasModel = new ImpresionEtiquetasModel(productoRepo);
        lastModel = impresionEtiquetasModel;
        tablaContenido.getItems().add(impresionEtiquetasModel);
    }

    @FXML
    private void actionRemove() {
        ImpresionEtiquetasModel impresionEtiquetasModel = tablaContenido.selectionModelProperty().get().getSelectedItem();
        if(impresionEtiquetasModel != null) {
            tablaContenido.getItems().remove(impresionEtiquetasModel);
        }
    }

    @FXML
    private void actionSeguiente() {
        articulosToReport = new ArrayList<Articulo>();
        for (ImpresionEtiquetasModel articuloModel: tablaContenido.getItems()) {
            List<Articulo> articuloList = productoRepo.findByCodigo(articuloModel.getCodigo());
            if (articuloList.isEmpty())
                continue;
            Articulo articulo = productoRepo.findByCodigo(articuloModel.getCodigo()).get(0);
            Long cantidad = articuloModel.getCantidad();
            int i= 0;
            while ( cantidad.compareTo(new Long(i)) > 0) {
                articulosToReport.add(articulo);
                i++;
            }
        }
        if (!articulosToReport.isEmpty()) {
            EtiquetaGenerator eg = new EtiquetaGenerator();
            String te = (String)tipoEtiqueta.getValue();
//            eg.generate("");
            if (tipoEtiqueta.getValue() != null) {
                if (tipoEtiqueta.getValue().equals("Big label"))
                {
                    eg.generate("omoikane/etiquetas/reportes/bigLabel.jrxml",articulosToReport);
                }
                else if (tipoEtiqueta.getValue().equals("Label Printer")) {
                    eg.generate("omoikane/etiquetas/reportes/labelPrint.jrxml",articulosToReport);
                }else if (tipoEtiqueta.getValue().equals("Standard")) {
                    eg.generate("omoikane/etiquetas/reportes/standardLabel.jrxml",articulosToReport);
                }
            }
        }

    }

    public static void main(String args[]) { }

    public ImpresionEtiquetasController() {
        ApplicationContext applicationContext = Principal.applicationContext;
        productoRepo = (ProductoRepo)applicationContext.getBean("productoRepo");
    }

    private ImpresionEtiquetasModel lastModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //To change body of implemented methods use File | Settings | File Templates.
        assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'ImpressionEtiquetasView.fxml'.";
        assert removeButton != null : "fx:id=\"removeButton\" was not injected: check your FXML file 'ImpressionEtiquetasView.fxml'.";
        assert tablaContenido != null : "fx:id=\"contenidoTable\" was not injected: check your FXML file 'ImpressionEtiquetasView.fxml'.";
        assert tipoEtiqueta != null : "fx:id=\"tipoEtiqueta\" was not injected: check your FXML file 'ImpressionEtiquetasView.fxml'.";
        final ImpresionEtiquetasModel model = new ImpresionEtiquetasModel(productoRepo);
        lastModel = model;
        setModel(model);

        Callback<TableColumn<ImpresionEtiquetasModel,String>, TableCell<ImpresionEtiquetasModel,String>> editableFactory = new Callback<TableColumn<ImpresionEtiquetasModel,String>, TableCell<ImpresionEtiquetasModel,String>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditableTableCell(lastModel, productoRepo);
            }
        };

        Callback<TableColumn<ImpresionEtiquetasModel,Long>, TableCell<ImpresionEtiquetasModel,Long>> numericFactory = new Callback<TableColumn<ImpresionEtiquetasModel,Long>, TableCell<ImpresionEtiquetasModel,Long>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new NumericEditableTableCell();
            }
        };

        cantidadCol.setCellFactory(numericFactory);
        cantidadCol.setCellValueFactory(new PropertyValueFactory<ImpresionEtiquetasModel, Long>("cantidad"));
        cantidadCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ImpresionEtiquetasModel, Long>> () {
            public void handle(TableColumn.CellEditEvent<ImpresionEtiquetasModel, Long> t) {
                ((ImpresionEtiquetasModel) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setCantidad(new Long(t.getNewValue()));
            }
        }
        );

        productoCol.setCellValueFactory(new PropertyValueFactory<ImpresionEtiquetasModel, String>("producto"));
        codigoCol.setCellValueFactory(new PropertyValueFactory<ImpresionEtiquetasModel, String>("codigo"));
        codigoCol.setCellFactory(editableFactory);
        codigoCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ImpresionEtiquetasModel, String>> () {
            @Override
            public void handle(TableColumn.CellEditEvent<ImpresionEtiquetasModel, String> t) {
                ((ImpresionEtiquetasModel) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setCodigo(t.getNewValue());

//                fillProducto(t);
                //for table view refresh
                tablaContenido.getColumns().get(0).setVisible(false);
                tablaContenido.getColumns().get(0).setVisible(true);

            }
        }
        );

        articulos = FXCollections.observableArrayList();
        tablaContenido.setItems(articulos);

//        Articulo producto = productoRepo.readByPrimaryKey(productoId);

    }

    public void setModel(final ImpresionEtiquetasModel model) {
        this.model = model;

        codigoCol.setCellValueFactory(
                new PropertyValueFactory<ImpresionEtiquetasModel, String>("codigo")
        );
        productoCol.setCellValueFactory(
                new PropertyValueFactory<ImpresionEtiquetasModel, String>("producto")
        );

        cantidadCol.setCellValueFactory(
                new PropertyValueFactory<ImpresionEtiquetasModel, Long>("cantidad")
        );
    }

}
