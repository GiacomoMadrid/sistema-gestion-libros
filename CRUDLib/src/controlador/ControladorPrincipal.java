package controlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import vista.frmEditorial;
import vista.frmPrincipal;
import vista.frmAutor;
import vista.frmPais;

/**
 *
 * @author Giacomo
 */
public class ControladorPrincipal {
    frmPrincipal vista;
    frmPais vistaP;
    frmEditorial vistaE;
    frmAutor vistaA;
    
    ControladorPais contPais;
    ControladorEditorial contEditorial;
    //ControladorAutor contAutor;
    
    public ControladorPrincipal(frmPrincipal v) throws SQLException{
        this.vista = v;
        this.vistaP = new frmPais();
        this.vistaE = new frmEditorial();
        this.contPais = new ControladorPais(vistaP);
        this.contEditorial = new ControladorEditorial(vistaE);
        //this.contAutor = new ControladorAutor(vistaA);
        
        this.vista.btnVerPaises.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contPais.iniciar();                
            }
        });
        
        this.vista.btnVerEditoriales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contEditorial.iniciar();                
            }
        });
    
    }
    
    
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
    }
    
}
