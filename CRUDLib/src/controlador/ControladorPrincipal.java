package controlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import vista.frmPrincipal;
import vista.frmPais;

/**
 *
 * @author Giacomo
 */
public class ControladorPrincipal {
    frmPrincipal vista;
    frmPais vistaP;
    ControladorPais contPais;
    
    public ControladorPrincipal(frmPrincipal v) throws SQLException{
        this.vista = v;
        this.vistaP = new frmPais();
        this.contPais = new ControladorPais(vistaP);
        
        this.vista.btnVerPaises.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contPais.iniciar();                
            }
        });
    
    }
    
    
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
    }
    
}
