
 /* Author Phesus        //////////////////////////////
 *  ORC,ACR             /////////////
 *                     /////////////
 *                    /////////////
 *                   /////////////
 * //////////////////////////////                   */

package omoikane.sistema

class NadesicoX {
    public def server;
    def UIDConn
    def UID
    def UIDRS

    def conectar() {
        server = Nadesico.conectar()
        UID    = server.sesion
        UIDConn= server.conexionMySQL(UID)
    }
    def setQuery(query) {
        UIDRS = server.ejecutaQuery(UID, UIDConn, query)
    }
    Object getAt(int row, int col) {
        def obj = server.getFromResultSet(UID, UIDRS, row, col)
        return obj
    }
    java.util.Hashtable getRow(int row, int nCols) {
        def rowDat = server.getRow(UID, UIDRS, row, nCols)
        return (java.util.Hashtable) rowDat
    }
    int getRowCount() {
        return Integer.valueOf(server.getRowCount(UID, UIDRS))
    }
    def desconectar() {
        server.desconectarMySQL(UID, UIDConn)
        server.desconectar()
    }
}

