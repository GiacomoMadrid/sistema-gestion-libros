package controlador;
import conexion.GlobalException;
import conexion.NoDataException;
import conexion.PaisDAO;
import crudlib.CRUDLib;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vista.frmPais;
import modelo.Pais;
/**
 *
 * @author Giacomo
 */
public class ControladorPais {
    frmPais vista;
    
    public ControladorPais(frmPais v) throws SQLException{
        this.vista = v;
        mostrarPaises();        
               
    }
    
    public void mostrarPaises(){
    try{
        DefaultTableModel model = new DefaultTableModel();
        PaisDAO paisDao = new PaisDAO();
                
        model.addColumn("#");
        model.addColumn("Nombre");
        
        Collection<Pais> paises = paisDao.mostrar_paises();
        for(Pais p : paises){
            model.addRow(new Object[]{p.getId(), p.getNombre()});        
        }
        this.vista.tabPais.setModel(model);
        
    }catch(GlobalException | NoDataException ex){
        JOptionPane.showMessageDialog(vista, "Error al cargar los países: " + ex.getMessage());
    }
}
    
    
    
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        
    }
    
    
}
