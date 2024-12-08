package crudlib;

import conexion.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import vista.frmPrincipal;
import controlador.ControladorPrincipal;


/**
 *
 * @author Giacomo
 */
public class CRUDLib {
    public static void main(String[] args) throws SQLException{
        frmPrincipal vista = new frmPrincipal();
        ControladorPrincipal contPrincipal = new ControladorPrincipal (vista);
        
        contPrincipal.iniciar();
        
        
    }
    
}
