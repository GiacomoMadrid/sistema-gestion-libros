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
    ControladorAutor contAutor;
    
    public ControladorPrincipal(frmPrincipal v) throws SQLException{
        this.vista = v;
        this.vistaP = new frmPais();
        this.vistaE = new frmEditorial();
        this.vistaA = new frmAutor();
        this.contPais = new ControladorPais(vistaP);
        this.contEditorial = new ControladorEditorial(vistaE);
        this.contAutor = new ControladorAutor(vistaA);
        
        this.vista.bgBuscar.add(this.vista.radNombre);
        this.vista.bgBuscar.add(this.vista.radAutor);
        this.vista.bgBuscar.add(this.vista.radEditorial);
        this.vista.bgBuscar.add(this.vista.radPais);
        this.vista.bgBuscar.add(this.vista.radDisponible);
        
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
        
        this.vista.btnVerAutores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contAutor.iniciar();                
            }
        });
    
    }
    
    //*************************************************************************
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
    }
    
}
