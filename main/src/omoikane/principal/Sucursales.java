/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal;

import omoikane.sistema.*;
import javax.swing.*;
import javax.swing.table.*;
import groovy.inspect.swingui.*;
import java.text.*;
import groovy.sql.*;
import javax.swing.event.*;
import java.awt.event.*;
import groovy.swing.*;
import java.util.Calendar;

import static omoikane.sistema.Usuarios.*;
import static omoikane.sistema.Permisos.*;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import omoikane.entities.CorteSucursal;
import omoikane.repository.CorteSucursalRepo;
import omoikane.exceptions.OmoikaneDeprecatedException;
import omoikane.repository.CorteRepo;
import omoikane.entities.Corte;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Usuario
 */
@Repository
@Transactional
public class Sucursales {
    @Autowired
    CorteSucursalRepo corteSucursalRepo;

    @Autowired
    CorteRepo corteRepo;

    public static enum ESTADO
      { CORTEPENDIENTE, HABILITADA; }


    @Transactional
    public ESTADO abrirSucursal() {

        if(isAbierta()) {
            return ESTADO.CORTEPENDIENTE;
        } else {
            CorteSucursal cs = new CorteSucursal();
            corteSucursalRepo.save(cs);
            return ESTADO.HABILITADA;
        }
    }

    public Boolean cajasSucursalCerradas() {
        Boolean res = true;
        List<Corte> ultimosCortes = corteRepo.findLastCortes();

        for(Corte corte : ultimosCortes) {
            res = res && !corte.isAbierto();
        }

        return res;
    }

    @Deprecated
    public Boolean isCortePendiente(Integer IDAlmacen) throws OmoikaneDeprecatedException {
        throw new OmoikaneDeprecatedException("Éste método ya no está de moda pertenece a Omoikane.");
    }
    public Boolean isAbierta() {
        //CorteSucursal corteSucursal = corteSucursalRepo.isSucusalAbierta();
        Boolean isAbierta = corteSucursalRepo.isSucusalAbierta();
        return isAbierta!=null ? isAbierta : false;

    }
    public void cerrar() {
        corteSucursalRepo.cerrarSucursal();
    }

    public CorteSucursal getSesionActual() {
        return corteSucursalRepo.getLastCorteSucursal();
    }
  /*
    static def corte(IDAlmacen) {
        def serv  = Nadesico.conectar()
        def salida= serv.corteSucursal(IDAlmacen)
        serv.desconectar()
        salida
    }
    static def sumaCorte(IDAlmacen, IDCorte) {
        def serv  = Nadesico.conectar()
        def salida= serv.getSumaCorteSucursal(IDAlmacen, IDCorte)
        serv.desconectar()
        salida
    }
    */
}