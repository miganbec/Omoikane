/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package omoikane.principal

import omoikane.sistema.*

/**
 *
 * @author Usuario
 */
class Caja {
    static def IDAlmacen = 1
    static def escritorio = omoikane.principal.Principal.escritorio

	static def lanzar() {
        def form = new omoikane.formularios.Caja()
        escritorio.getPanelEscritorio().add(form)
        Herramientas.centrarVentana(form);
        form.setVisible(true);
        Herramientas.iconificable(form)
        Herramientas.setColumnsWidth(form.tablaVenta, [0.48,0.13,0.13,0.13,0.13]);

        form.toFront()
        try { form.setSelected(true) } catch(Exception e) { Dialogos.lanzarDialogoError(null, "Error al iniciar formulario caja", Herramientas.getStackTraceString(e)) }
        form.txtCaptura.requestFocus()
        def date = Calendar.getInstance()
        form.txtFecha.text = date.get(date.DAY_OF_MONTH) + "-" + (date.get(date.MONTH)+1) + "-" + date.get(date.YEAR)

        def serv = Nadesico.conectar()
        def round = { cant -> return (Math.round(cant*100)/100) }
        def cifra = { cant -> return String.format("\$%,.2f", cant) }
        def aDoble= { cant -> return cant.replace("\$", '').replace(",", '') as Double }
        def sumarTodo = {
            def dat   = form.modelo.getDataVector()
            def sumas = [0.0,0.0,0.0]
            dat.each { linea ->
                sumas[0] += linea[1] as Double; sumas[1] += linea[3] as Double; sumas[2] += aDoble(linea[4])
            }
            form.txtNArticulos.text = sumas[0]
            form.txtSubtotal.text   = cifra (sumas[2])
            form.txtTotal.text      = cifra (sumas[2])
            form.txtDescuento.text  = cifra (sumas[1])
        }
        def addArtic = { codigo ->
            try {
                def captura = form.txtCaptura.text.split("\\*")
                def cantidad= captura.size()==1?1:captura[0..captura.size()-2].inject(1) { acum, i -> acum*(i as Double) }
                def art     = serv.codigo2Articulo(IDAlmacen, captura[captura.size()-1])
                if(art == null || art == 0) { Dialogos.lanzarAlerta("Artículo no encontrado!!"); } else {
                    def total   = cifra(cantidad * art.costo)
                    form.txtCaptura.text = ""
                    form.modelo.addRow([art.descripcion,cantidad,art.costo,art.descuento,total].toArray())
                    sumarTodo()
                    form.repaint()
                }
            } catch(e) { Dialogos.error("Error al obtener información de nadesico!", e) }
        }
        form.txtCaptura.keyPressed = { e -> 
            if(e.keyCode==e.VK_ENTER) addArtic(form.txtCaptura.text)
            //Al presionar   F2: (lanzarCatalogoDialogo)
            if(e.keyCode == e.VK_F2) { form.btnCatalogo.doClick() }

          }
        def catArticulos = { def retorno = Articulos.lanzarDialogoCatalogo() as String; return retorno==null?"":retorno }
        form.btnCatalogo.actionPerformed = { e -> Thread.start { form.txtCaptura.text = form.txtCaptura.text + catArticulos(); form.txtCaptura.requestFocus() } }
    }
}
