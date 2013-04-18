package omoikane.etiquetas;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import omoikane.caja.presentation.ProductoModel;
import omoikane.producto.Articulo;
import omoikane.repository.ProductoRepo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.synyx.hades.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 12/03/13
 * Time: 02:45
 * To change this template use File | Settings | File Templates.
 */
public class ImpresionEtiquetasModel {
    private StringProperty producto;
    private ObjectProperty<Long> cantidad;
    private StringProperty codigo;
    private ProductoRepo productoRepo;

    public ImpresionEtiquetasModel(ProductoRepo productoRepo) {
        producto = new SimpleStringProperty("");
        cantidad = new SimpleObjectProperty<Long>( new Long(1));
        codigo = new SimpleStringProperty("");
        this.productoRepo = productoRepo;
    }

    public ImpresionEtiquetasModel(String producto,Long cantidad,String codigo) {
        this.producto = new SimpleStringProperty(producto);
        this.cantidad = new SimpleObjectProperty<Long>( cantidad);
        this.codigo = new SimpleStringProperty(codigo);
    }


    public String getProducto() {
        return producto.get();
    }

    public void setProducto(String producto) {
        this.producto.set(producto);
    }

    public Long getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(Long cantidad) {
        this.cantidad.set(cantidad);
    }

    public String getCodigo() {
        return codigo.get();
    }

    public void setCodigo(String codigo) {
        /*
        List<Articulo> productos = productoRepo.findByCodigo(codigo);
        if (!productos.isEmpty())
        {
            setProducto(productos.get(0).getDescripcion());
        }
        */
        this.codigo.set(codigo) ;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImpresionEtiquetasModel impresionEtiquetasModel = (ImpresionEtiquetasModel) o;

        if (!codigo.get().equals(impresionEtiquetasModel.getCodigo())) return false;

        return true;
    }
}
