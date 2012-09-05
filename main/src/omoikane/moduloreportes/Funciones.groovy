package omoikane.moduloreportes

/*@author Phesus-Lab*/

import java.sql.*;
import javax.swing.*
import org.apache.log4j.Logger
import omoikane.sistema.huellas.UserIdentifierOneTouch;

class Funciones {
    public static Logger logger = Logger.getLogger(Funciones.class);
    static def lanzarVentaXLineas(form) //lanza el formulario de ventas por lineas
    {
        form.panelReportes.removeAll();
        def vxl = new ventasXlineas();
        poblarLinea(vxl)
        form.panelReportes.add(vxl);
        form.panelReportes.updateUI();
    }

    static def lanzarVentaXGrupos(form) //lanza el formulario de ventas por lineas
    {
        form.panelReportes.removeAll();
        def vxg = new VentasXGrupos();
        poblarGrupo(vxg)
        form.panelReportes.add(vxg);
        form.panelReportes.updateUI();
    }

    static def lanzarutilidadXLinea(form) //lanza el formulario de ventas por lineas
    {
        form.panelReportes.removeAll();
        def vxl = new utilidadXLinea();
        form.panelReportes.add(vxl);
        form.panelReportes.updateUI();
    }

    static def lanzararticulosGL(form) //lanza el formulario de ventas por lineas
    {
        form.panelReportes.removeAll();
        def vxl = new articulosGL();
        form.panelReportes.add(vxl);
        form.panelReportes.updateUI();
    }

    static def lanzarCosteoGL(form) //lanza el formulario de ventas por lineas
    {
        form.panelReportes.removeAll();
        def vxl = new CosteoGL();
        poblarAlmacen(vxl)
        form.panelReportes.add(vxl);
        form.panelReportes.updateUI();
    }

    static def poblarLinea(vxl) // llena la lista de lineas de la base de datos
    {
        def conn
        def listaNormales = vxl.lista
        def dataListNormal = (new DefaultListModel())
        vxl.barra.setIndeterminate(true);
        Thread.start {
            try{
                conn = Comandos.Enlace(conn);
                def st=conn.createStatement();
                def rs=st.executeQuery("SELECT lineas.id_linea,lineas.descripcion FROM lineas");
                while(rs.next()){
                    dataListNormal.addElement(new ElementoListaLineas(id:rs.getInt(1),nombre:rs.getString(2)));
                }
                vxl.barra.setIndeterminate(false);
                listaNormales.setModel(dataListNormal);
                conn.close()

            }catch(Exception e) {
              //JOptionPane.showMessageDialog(null,"Error con la conexion o no esta conectado");
              logger.error("Error con la conexión o no está conectado", e)
            }
        }

    }

    static def poblarGrupo(vxl) // llena la lista de lineas de la base de datos
    {
        def conn
        def listaNormales = vxl.lista
        def dataListNormal = (new DefaultListModel())
        vxl.barra.setIndeterminate(true);
        Thread.start {
            try{
                conn = Comandos.Enlace(conn);
                def st=conn.createStatement();
                def rs=st.executeQuery("SELECT grupos.id_grupo,grupos.descripcion FROM grupos");
                while(rs.next()){
                    dataListNormal.addElement(new ElementoListaLineas(id:rs.getInt(1),nombre:rs.getString(2)));
                }
                vxl.barra.setIndeterminate(false);
                listaNormales.setModel(dataListNormal);
                conn.close()

            }catch(Exception e) {
              //JOptionPane.showMessageDialog(null,"Error con la conexion o no esta conectado");
              logger.error("Error con la conexión o no está conectado", e)
            }
        }

    }

    static def poblarAlmacen(vxl) // llena la lista de lineas de la base de datos
    {
        def conn
        def listaNormales = vxl.listaA
        def dataListNormal = (new DefaultListModel())
        vxl.barra.setIndeterminate(true);
        Thread.start {
            try{
                conn = Comandos.Enlace(conn);
                def st=conn.createStatement();
                def rs=st.executeQuery("SELECT almacenes.id_almacen,almacenes.descripcion FROM almacenes");
                while(rs.next()){
                    dataListNormal.addElement(new ElementoListaLineas(id:rs.getInt(1),nombre:rs.getString(2)));
                }
                vxl.barra.setIndeterminate(false);
                listaNormales.setModel(dataListNormal);
                conn.close()

            }catch(Exception e) {
              //JOptionPane.showMessageDialog(null,"Error con la conexion o no esta conectado");
              logger.error("Error con la conexión o no está conectado", e)
            }
        }

    }

    static def lanzarReporteVXL(form) {
            form.barra.setIndeterminate(true);
            Thread.start {
            def list
            list = (String) (form.getLineas()*.id)
            list=list.replace('[','')
            list=list.replace(']','')
            def reporte = new Reporte('Plantillas/VentasXLinea.jrxml', [SUBREPORT_DIR:"Plantillas/",FDesde:form.getFechaDesde(),FHasta:form.getFechaHasta(),Lineas:list]);
            reporte.lanzarPreview(form)
            form.barra.setIndeterminate(false);
            }
        }

    static def lanzarReporteVXG(form) {
            form.barra.setIndeterminate(true);
            Thread.start {
            def list
            list = (String) (form.getGrupos()*.id)
            list=list.replace('[','')
            list=list.replace(']','')
            def reporte = new Reporte('Plantillas/VentasXGrupo.jrxml', [SUBREPORT_DIR:"Plantillas/",FDesde:form.getFechaDesde(),FHasta:form.getFechaHasta(),Grupos:list]);
            reporte.lanzarPreview(form)
            form.barra.setIndeterminate(false);
            }
        }

    static def lanzarReporteUXL(form) {
            form.barra.setIndeterminate(true);
            Thread.start {
            def reporte = new Reporte('Plantillas/utilidadXLinea.jrxml',[FDesde:form.getFechaDesde(),FHasta:form.getFechaHasta()]);
            reporte.lanzarPreview(form)
            form.barra.setIndeterminate(false);
            }
        }

    static def lanzarReporteAXL(form) {
            form.barra.setIndeterminate(true);
            Thread.start {
            def list
            list = (String) (form.getLineas()*.id)
            list=list.replace('[','')
            list=list.replace(']','')
            def reporte = new Reporte('Plantillas/ArticulosLineas.jrxml', [SUBREPORT_DIR:"Plantillas/",Lineas:list]);
            reporte.lanzarPreview(form)
            form.barra.setIndeterminate(false);
            }
        }

    static def lanzarReporteAXG(form) {
            form.barra.setIndeterminate(true);
            Thread.start {
            def list
            list = (String) (form.getLineas()*.id)
            list=list.replace('[','')
            list=list.replace(']','')
            def reporte = new Reporte('Plantillas/ArticulosGrupos.jrxml', [SUBREPORT_DIR:"Plantillas/",Grupos:list]);
            reporte.lanzarPreview(form)
            form.barra.setIndeterminate(false);
            }
        }

    static def lanzarReporteCXG(form) {
            form.barra.setIndeterminate(true);
            Thread.start {
            def list
            def alm
            list = (String) (form.getLineas()*.id)
            alm = (String) (form.getAlmacen()*.id)
            alm=alm.replace('[','')
            alm=alm.replace(']','')
            list=list.replace('[','')
            list=list.replace(']','')
            def reporte = new Reporte('Plantillas/CosteoGrupos.jrxml', [SUBREPORT_DIR:"Plantillas/",Grupos:list,Almacen:alm]);
            reporte.lanzarPreview(form)
            form.barra.setIndeterminate(false);
            }
        }

        static def lanzarReporteCXL(form) {
            form.barra.setIndeterminate(true);
            Thread.start {
            def list
            def alm
            list = (String) (form.getLineas()*.id)
            alm = (String) (form.getAlmacen()*.id)
            alm=alm.replace('[','')
            alm=alm.replace(']','')
            list=list.replace('[','')
            list=list.replace(']','')
            def reporte = new Reporte('Plantillas/CosteoLineas.jrxml', [SUBREPORT_DIR:"Plantillas/",Lineas:list,Almacen:alm]);
            reporte.lanzarPreview(form)
            form.barra.setIndeterminate(false);
            }
        }
}

class ElementoListaLineas{ //introduce dos valores a la jlist para guardar el id
    def id
    def nombre
    String toString(){return String.valueOf(nombre)}
}