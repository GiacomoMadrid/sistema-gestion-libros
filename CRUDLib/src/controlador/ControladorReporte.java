package controlador;

import vista.frmReporte;

/**
 *
 * @author Giacomo
 */
public class ControladorReporte {
    protected frmReporte vista;
    
    public ControladorReporte(frmReporte vista){
        this.vista = vista;
    }
    
    
    
    
    
    
    
    //-------------------------------------------------------------
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        this.vista.txtReporte.setText("");
    }
    
}
