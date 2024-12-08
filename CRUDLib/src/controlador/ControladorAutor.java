package controlador;
import conexion.GlobalException;
import conexion.NoDataException;
import conexion.AutorDAO;
import crudlib.CRUDLib;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vista.frmAutor;
import modelo.Autor;
/**
 *
 * @author Giacomo
 */
public class ControladorAutor {
    frmAutor vista;
    AutorDAO pDao;
    
    public ControladorAutor(frmAutor v) throws SQLException{
        this.vista = v;
        this.pDao = new AutorDAO();
        
        //---------------------------------------------------------------------
        
        this.vista.btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    crearPais();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.vista.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    eliminarPais();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.vista.btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    actualizarPais();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        
        
    }
    
    //*****************************************************************************
    public void mostrarPaises(){
        try{
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            DefaultTableModel model = new DefaultTableModel();
            AutorDAO paisDao = new AutorDAO();

            model.addColumn("ID");
            model.addColumn("Nombres");
            model.addColumn("Apellidos");
            model.addColumn("Fecha de Nacimiento");
            model.addColumn("Pais de Nacimiento");

            Collection<Autor> autores = paisDao.mostrar_todo();
            for(Autor a : autores){
                model.addRow(new Object[]{a.getId(), a.getNombres(), 
                    a.getApellidos(), formatDate.format(a.getFecha_nacimiento()), a.getPais()});        
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
            Autor a = new Autor(str);
            pDao.crear(a);
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
                Autor a = new Autor(id);
                pDao.eliminar(a);
                mostrarPaises();
                
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "ID Inválido\nNo se eliminó ningún país.");    
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No se eliminó ningún país.");
        }    
    }
    
    public void actualizarPais() throws GlobalException, NoDataException{
        String str = JOptionPane.showInputDialog("Id del país a actualizar: ");
        if(str != null && str != ""){
            try {
                int id = Integer.parseInt(str);                
                String nombre = JOptionPane.showInputDialog("Nuevo nombre para el pais: ");                
                Autor a = new Autor(id, nombre);
                
                pDao.actualizar(a);
                mostrarPaises();
                
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "ID Invaálido.");    
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No se actualizó ningún país.");
        }    
    }
    
    
    
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        mostrarPaises();        
        
    }
    
    
}
