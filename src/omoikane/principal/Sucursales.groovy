/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.sistema.*;
import javax.swing.*
import javax.swing.table.*
import groovy.inspect.swingui.*
import java.text.*;
import groovy.sql.*;
import javax.swing.event.*;
import java.awt.event.*;
import groovy.swing.*
import java.util.Calendar;

import static omoikane.sistema.Usuarios.*;
import static omoikane.sistema.Permisos.*;

/**
 *
 * @author Usuario
 */
class Sucursales {
    static def abrirSucursal(IDAlmacen = -1) {
        if(cerrojo(PMA_ABRIRSUCURSAL)) {
            if(JOptionPane.showConfirmDialog(null, "¿Habilitar sucursal para realizar ventas?", "Habilitar sucursal para vender", JOptionPane.YES_NO_CANCEL_OPTION)==JOptionPane.YES_OPTION)
            {
                if(cortePendiente(IDAlmacen)) {
                    Dialogos.lanzarAlerta("Está pendiente el corte del día anterior, necesita realizarlo para continuar")
                    return 0
                } else {
                    def serv = Nadesico.conectar()
                    serv.abrirSucursal(IDAlmacen)
                    serv.desconectar()
                    Dialogos.lanzarAlerta("Sucursal habilitada para vender!")
                    return 1
                }
            } else { return 0 }
        }
    }
    static def cajasSucursalCerradas(IDAlmacen) {
        def serv = Nadesico.conectar()
        def res  = serv.getCajasSucursalCerradas(IDAlmacen)
        serv.desconectar()
        res
    }
    static def cortePendiente(IDAlmacen) {
        def serv   = Nadesico.conectar()
        def salida = serv.sucursalCortePendiente(IDAlmacen)
        serv.desconectar()
        return salida
    }
    static def abierta(IDAlmacen) {
        def serv  = Nadesico.conectar()
        def salida= serv.sucursalAbierta(IDAlmacen)
        serv.desconectar()
        salida
    }
    static def existe(IDAlmacen) {
        def serv  = Nadesico.conectar()
        def salida= serv.sucursalExiste(IDAlmacen)
        serv.desconectar()
        salida
    }
    static def cerrar(IDAlmacen) {
        def serv  = Nadesico.conectar()
        def salida= serv.cerrarSucursal(IDAlmacen)
        serv.desconectar()
        salida
    }
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
}