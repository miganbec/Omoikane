package omoikane.etiquetas.presentation;

import omoikane.etiquetas.ImpresionEtiquetasModel;
import omoikane.producto.Articulo;
import omoikane.repository.ProductoRepo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 14/03/13
 * Time: 03:51
 * To change this template use File | Settings | File Templates.
 */
public class EditableTableCell<S extends Object, T extends String> extends AbstractEditableTableCell<S, T> {

    private ProductoRepo productoRepo;
    private ImpresionEtiquetasModel model;

    public EditableTableCell(ImpresionEtiquetasModel model, ProductoRepo productoRepo) {
        this.model = model;
        this.productoRepo = productoRepo;
    }

    @Override
    protected String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
    @Override
    protected void commitHelper( boolean losingFocus ) {
        if (textField == null)
            return;

        List<Articulo> articulos =  productoRepo.findByCodigo(textField.getText());
        if (articulos.isEmpty()) {
            if( losingFocus ) {
                commitEdit(((T) "Producto no encontrado"));
                cancelEdit();
            }
        } else  {
            model.setProducto(articulos.get(0).getDescripcion());
            commitEdit(((T) textField.getText()));
        }

    }

}