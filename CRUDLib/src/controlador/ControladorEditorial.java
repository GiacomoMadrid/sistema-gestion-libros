package controlador;
import conexion.GlobalException;
import conexion.NoDataException;
import conexion.EditorialDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vista.frmEditorial;
import modelo.Editorial;
/**
 *
 * @author Giacomo
 */
public class ControladorEditorial {
    frmEditorial vista;
    EditorialDAO edDao;
    
    public ControladorEditorial(frmEditorial v) throws SQLException{
        this.vista = v;
        this.edDao = new EditorialDAO();
        
        //---------------------------------------------------------------------
        
        this.vista.btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    crearEditorial();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorEditorial.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorEditorial.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.vista.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    eliminarEditorial();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorEditorial.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorEditorial.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.vista.btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    actualizarEditorial();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorEditorial.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorEditorial.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        
        
    }
    
    //*****************************************************************************
    public void mostrarEditoriales(){
        try{
            DefaultTableModel model = new DefaultTableModel();
            EditorialDAO ediDao = new EditorialDAO();

            model.addColumn("ID");
            model.addColumn("Nombre");

            Collection<Editorial> editoriales = ediDao.mostrar_todo();
            for(Editorial ed : editoriales){
                model.addRow(new Object[]{ed.getId(), ed.getNombre()});        
            }
            this.vista.tabPais.setModel(model);

            this.vista.tabPais.getColumnModel().getColumn(0).setPreferredWidth(40);
            this.vista.tabPais.getColumnModel().getColumn(0).setMinWidth(40);
            this.vista.tabPais.getColumnModel().getColumn(0).setMaxWidth(40);
            this.vista.tabPais.getColumnModel().getColumn(0).setWidth(40);

        }catch(GlobalException | NoDataException ex){
            JOptionPane.showMessageDialog(vista, "Error al cargar los editoriales: " + ex.getMessage());
        }
    }
    
    public void crearEditorial() throws GlobalException, NoDataException{
        String str = JOptionPane.showInputDialog("Nombre de la nueva editorial: ");
        if(str != null && str != ""){
            Editorial ed = new Editorial(str);
            edDao.crear(ed);
            mostrarEditoriales();
            
        }else{
            JOptionPane.showMessageDialog(null, "No se agregó la editorial.");
        }        
    }
    
    public void eliminarEditorial() throws GlobalException, NoDataException{
        String str = JOptionPane.showInputDialog("Id de la editorial a eliminar: ");
        if(str != null && str != ""){
            try {
                int id = Integer.parseInt(str);
                Editorial ed = new Editorial(id);
                edDao.eliminar(ed);
                mostrarEditoriales();
                
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "ID Inválido\nNo se eliminó ninguna editorial.");    
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No se eliminó ninguna editorial.");
        }    
    }
    
    public void actualizarEditorial() throws GlobalException, NoDataException{
        String str = JOptionPane.showInputDialog("Id de la editorial a actualizar: ");
        if(str != null && str != ""){
            try {
                int id = Integer.parseInt(str);                
                String nombre = JOptionPane.showInputDialog("Nuevo nombre para la editorial: ");                
                Editorial p = new Editorial(id, nombre);
                
                edDao.actualizar(p);
                mostrarEditoriales();
                
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "ID Invaálido.");    
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No se actualizó ninguna editorial.");
        }    
    }
    
    
    
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        mostrarEditoriales();        
        
    }
    
    
}
