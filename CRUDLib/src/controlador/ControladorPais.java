package controlador;
import conexion.GlobalException;
import conexion.NoDataException;
import conexion.PaisDAO;
import crudlib.CRUDLib;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    PaisDAO pDao;
    
    public ControladorPais(frmPais v) throws SQLException{
        this.vista = v;
        this.pDao = new PaisDAO();
        
        //---------------------------------------------------------------------
        
        this.vista.btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    crearPais();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorPais.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorPais.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.vista.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    eliminarPais();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorPais.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorPais.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    //*****************************************************************************
    public void mostrarPaises(){
        try{
            DefaultTableModel model = new DefaultTableModel();
            PaisDAO paisDao = new PaisDAO();

            model.addColumn("ID");
            model.addColumn("Nombre");

            Collection<Pais> paises = paisDao.mostrar_todo();
            for(Pais p : paises){
                model.addRow(new Object[]{p.getId(), p.getNombre()});        
            }
            this.vista.tabPais.setModel(model);

            this.vista.tabPais.getColumnModel().getColumn(0).setPreferredWidth(40);
            this.vista.tabPais.getColumnModel().getColumn(0).setMinWidth(40);
            this.vista.tabPais.getColumnModel().getColumn(0).setMaxWidth(40);
            this.vista.tabPais.getColumnModel().getColumn(0).setWidth(40);

        }catch(GlobalException | NoDataException ex){
            JOptionPane.showMessageDialog(vista, "Error al cargar los países: " + ex.getMessage());
        }
    }
    
    public void crearPais() throws GlobalException, NoDataException{
        String str = JOptionPane.showInputDialog("Nombre del nuevo país: ");
        if(str != null && str != ""){
            Pais p = new Pais(str);
            pDao.crear(p);
            mostrarPaises();
            
        }else{
            JOptionPane.showMessageDialog(null, "No se agregó el país.");
        }        
    }
    
    public void eliminarPais() throws GlobalException, NoDataException{
        String str = JOptionPane.showInputDialog("Id del país a eliminar: ");
        if(str != null && str != ""){
            try {
                int id = Integer.parseInt(str);
                Pais p = new Pais(id);
                pDao.eliminar(p);
                mostrarPaises();
                
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "ID Invaálido\nNo se eliminó ningún país.");    
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No se eliminó ningún país.");
        }    
    }
    
    
    
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        mostrarPaises();        
        
    }
    
    
}
