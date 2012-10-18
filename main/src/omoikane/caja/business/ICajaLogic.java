package omoikane.caja.business;

import omoikane.caja.presentation.CajaModel;

/**
 * Created with IntelliJ IDEA.
 * User: Octavio
 * Date: 13/09/12
 * Time: 01:15 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ICajaLogic {

    public void onCaptura(CajaModel model);
    public void buscar(CajaModel model);
    public void calcularCambio(CajaModel model);
    public void terminarVenta(CajaModel model);
    public void onProductListChanged(CajaModel model);
}
