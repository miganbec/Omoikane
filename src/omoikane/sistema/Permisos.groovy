/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.sistema

/**
 *
 * @author Adan
 */
import static omoikane.sistema.Usuarios.*;

class Permisos {

    static def PMA_ABRIRALMACEN      = SUPERVISOR
    static def PMA_DETALLESALMACEN   = SUPERVISOR
    static def PMA_MODIFICARALMACEN  = ADMINISTRADOR
    static def PMA_ELIMINARALMACEN   = PROPIETARIO

    static def PMA_ABRIRARTICULO       = CAJERO
    static def PMA_DETALLESARTICULO    = SUPERVISOR
    static def PMA_MODIFICARARTICULO   = SUPERVISOR
    static def PMA_ELIMINARARTICULO    = PROPIETARIO

    static def PMA_ABRIRCAJA       = SUPERVISOR
    static def PMA_DETALLESCAJA    = SUPERVISOR
    static def PMA_MODIFICARCAJA   = GERENTE
    static def PMA_ELIMINARCAJA    = PROPIETARIO

    static def PMA_ABRIRUSUARIO       = GERENTE
    static def PMA_DETALLESUSUARIO    = GERENTE
    static def PMA_MODIFICARUSUARIO   = GERENTE
    static def PMA_ELIMINARUSUARIO    = PROPIETARIO

    static def PMA_ABRIRLINEA       = CAJERO
    static def PMA_DETALLESLINEA    = CAJERO
    static def PMA_MODIFICARLINEA   = SUPERVISOR
    static def PMA_ELIMINARLINEA    = PROPIETARIO

    static def PMA_ABRIRGRUPO       = CAJERO
    static def PMA_DETALLESGRUPO    = CAJERO
    static def PMA_MODIFICARGRUPO   = SUPERVISOR
    static def PMA_ELIMINARGRUPO    = PROPIETARIO

    static def PMA_ABRIRCLIENTE       = CAJERO
    static def PMA_DETALLESCLIENTE    = SUPERVISOR
    static def PMA_MODIFICARCLIENTE   = SUPERVISOR
    static def PMA_ELIMINARCLIENTE    = PROPIETARIO

    static def PMA_ABRIRCORTES       = SUPERVISOR
    static def PMA_DETALLESCORTES    = SUPERVISOR
    static def PMA_MODIFICARCORTES   = ADMINISTRADOR
    static def PMA_ELIMINARCORTES    = PROPIETARIO

    static def PMA_ABRIRVENTAS       = SUPERVISOR
    static def PMA_DETALLESVENTAS    = SUPERVISOR
    static def PMA_MODIFICARVENTAS   = ADMINISTRADOR
    static def PMA_ELIMINARVENTAS    = PROPIETARIO

    static def PMA_ABRIRMOVALMACEN       = SUPERVISOR
    static def PMA_DETALLESMOVALMACEN    = SUPERVISOR
    static def PMA_MODIFICARMOVALMACEN   = SUPERVISOR
    static def PMA_ELIMINARMOVALMACEN    = PROPIETARIO

    static def PMA_TOTALVENTA            = SUPERVISOR
    static def PMA_LANZARCAJA            = CAJERO
    static def PMA_ABRIRCAJAS            = SUPERVISOR //INICIAR VENTAS EN CAJA
}


