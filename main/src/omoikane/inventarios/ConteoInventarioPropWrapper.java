package omoikane.inventarios;

import javafx.beans.property.adapter.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import omoikane.entities.Usuario;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 05/04/13
 * Time: 22:15
 * To change this template use File | Settings | File Templates.
 */
public class ConteoInventarioPropWrapper {
    private JavaBeanObjectProperty<Date> fecha;
    private JavaBeanObjectProperty<Usuario> usuario;
    private ObservableList<ItemConteoPropWrapper> items;
    private JavaBeanBooleanProperty completado;
    private JavaBeanLongProperty id;

    public ConteoInventario _conteoInventario;
    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ConteoInventarioPropWrapper.class);

    public ConteoInventarioPropWrapper(ConteoInventario conteoInventario) {
        _conteoInventario = conteoInventario;
        try {
            JavaBeanObjectPropertyBuilder<Date> builder = new JavaBeanObjectPropertyBuilder<>();
            builder.bean(conteoInventario);
            builder.name("fecha");
            fecha = builder.build();

            JavaBeanObjectPropertyBuilder<Usuario> builder2 = new JavaBeanObjectPropertyBuilder<>();
            builder2.bean(conteoInventario);
            builder2.name("usuario");
            usuario = builder2.build();

            items = getUpdatedItems();

            JavaBeanBooleanPropertyBuilder builder3 = new JavaBeanBooleanPropertyBuilder();
            builder3.bean(conteoInventario);
            builder3.name("completado");
            completado = builder3.build();

            JavaBeanLongPropertyBuilder builder4 = new JavaBeanLongPropertyBuilder();
            builder4.bean(conteoInventario);
            builder4.name("id");
            id = builder4.build();

        } catch (NoSuchMethodException e) {
            logger.error("Invalid method to wrap", e);
        }
    }

    private ObservableList<ItemConteoPropWrapper> getUpdatedItems() {
        if(items == null) items = FXCollections.observableArrayList();
        items.clear();
        for(ItemConteoInventario item : _conteoInventario.getItems()) {
            items.add(new ItemConteoPropWrapper(item));
        }
        return items;
    }

    public ObservableList<ItemConteoPropWrapper> getItems() {
        return items;
    }

    public JavaBeanObjectProperty<Date> getDate() {
        return fecha;
    }

    public JavaBeanLongProperty getId() {
        return id;
    }

    public JavaBeanBooleanProperty getCompletado() {
        return completado;
    }

    public void setCompletado(Boolean completado1) {
        getCompletado().set(completado1);
    }

    public void addItem(ItemConteoPropWrapper itemConteoPropWrapper) {
        items.add(itemConteoPropWrapper);
        _conteoInventario.getItems().add(itemConteoPropWrapper.getBean());
    }

    public void deleteItem(int idx) {
        items.remove(idx);
        _conteoInventario.getItems().remove(idx);
    }

    public ConteoInventario getBean() {
        return _conteoInventario;
    }
}
