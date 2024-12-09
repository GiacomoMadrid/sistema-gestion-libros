package controlador;
import conexion.GlobalException;
import conexion.NoDataException;
import conexion.AutorDAO;
import conexion.PaisDAO;
import crudlib.CRUDLib;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import vista.frmAutor;
import modelo.Autor;
import modelo.Pais;
import vista.frmCRUDAutor;
/**
 *
 * @author Giacomo
 */
public class ControladorAutor {
    protected frmAutor vista;
    protected frmCRUDAutor vCrud;
    private AutorDAO aDao;
    private short flag;  
    
    public ControladorAutor(frmAutor v) throws SQLException{
        this.vista = v;
        this.vCrud = new frmCRUDAutor();
        this.aDao = new AutorDAO();
               
              
        //---------------------------------------------------------------------
        
        this.vista.btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag = 0;
                mostrarVistaCRUD(flag);
                
            }
        });
        
        this.vista.btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                flag = 1;
                mostrarVistaCRUD(flag);
            }
        });
        
        this.vista.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    eliminarAutor();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        //---------------------------------------------------------------------
        
        this.vCrud.btnAceptar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    if(flag == 0){
                        crearAutor();
                    }else{
                        actualizarAutor();
                    }
                    vCrud.dispose();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        this.vCrud.btnCancelar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                vCrud.dispose();
            }
        });
        
    }
    
    //*****************************************************************************
    public void mostrarAutores(){
        try{
            
            DefaultTableModel model = new DefaultTableModel();
            
            model.addColumn("ID");
            model.addColumn("Nombres");
            model.addColumn("Apellidos");
            model.addColumn("Pais");
            model.addColumn("Año de Nacimiento");

            Collection<Autor> autores = aDao.mostrar_todo();
            for(Autor a : autores){
                if(a.getAnno_nacimiento() != null){
                    model.addRow(new Object[]{a.getId(), a.getNombres(), 
                        a.getApellidos(), a.getNombrePais(), a.getAnno_nacimiento()});
                }else{
                    model.addRow(new Object[]{a.getId(), a.getNombres(), 
                        a.getApellidos(), a.getNombrePais(), "--"});
                }       
            }
            this.vista.tabPais.setModel(model);

            this.vista.tabPais.getColumnModel().getColumn(0).setPreferredWidth(40);
            this.vista.tabPais.getColumnModel().getColumn(0).setMinWidth(40);
            this.vista.tabPais.getColumnModel().getColumn(0).setMaxWidth(40);
            this.vista.tabPais.getColumnModel().getColumn(0).setWidth(40);

        }catch(GlobalException | NoDataException ex){
            JOptionPane.showMessageDialog(vista, "Error al cargar los autores: " + ex.getMessage());
        }
    }
    
    public void cargarAutor(){            
        try{
            String str = JOptionPane.showInputDialog("Id del autor a actualizar: ");
            int idAutor = Integer.parseInt(str);
            Autor aut = (Autor) aDao.buscar(new Autor(idAutor));
            this.vCrud.txtID.setText(""+aut.getId());
            this.vCrud.txtNombres.setText(aut.getNombres());
            this.vCrud.txtApellidos.setText(aut.getApellidos());
            this.vCrud.txtFecha.setText(""+aut.getAnno_nacimiento());
                        
        }catch(GlobalException | NoDataException | NumberFormatException ex){
            JOptionPane.showMessageDialog(vista, "Error al cargar el autor: " + ex.getMessage());
        }
        
    }
    
    public void crearAutor() throws GlobalException, NoDataException{
        String strNombres = this.vCrud.txtNombres.getText();
        String strApellidos = this.vCrud.txtApellidos.getText();
        String strAnno = this.vCrud.txtFecha.getText();
        Pais pais = (Pais) this.vCrud.cboxPais.getItemAt(this.vCrud.cboxPais.getSelectedIndex());
        Integer anno;
        
        if(strAnno.isBlank() || strAnno.isEmpty()){
            anno = 0;
        }else{
            anno = Integer.parseInt(strAnno);
        }
        
        if((strNombres != null && strNombres != "")
                ||(strApellidos != null && strApellidos != "")
                || (pais != null)){
                                   
            Autor a = new Autor(strNombres, strApellidos, anno, pais);
            aDao.crear(a);
            mostrarAutores();
            
        }else{
            JOptionPane.showMessageDialog(null, "No se agregó el autor.");
        }   
    }
    
    public void eliminarAutor() throws GlobalException, NoDataException{
        String str = JOptionPane.showInputDialog("Id del autor a eliminar: ");
        if(str != null && str != ""){
            try {
                int id = Integer.parseInt(str);
                Autor a = new Autor(id);
                aDao.eliminar(a);
                mostrarAutores();
                
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "ID Inválido\nNo se eliminó ningún autor.");    
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No se eliminó ningún autor.");
        }    
    }
    
    public void actualizarAutor() throws GlobalException, NoDataException{
        String strNombres = this.vCrud.txtNombres.getText();
        String strApellidos = this.vCrud.txtApellidos.getText();
        String strAnno = this.vCrud.txtFecha.getText();
        Pais pais = (Pais) this.vCrud.cboxPais.getItemAt(this.vCrud.cboxPais.getSelectedIndex());
        String str = this.vCrud.txtID.getText();
         if((str != null && str != "")
                || (strNombres != null && strNombres != "")
                ||(strApellidos != null && strApellidos != "")
                || (pais != null)){
            try {
                int id = Integer.parseInt(str); 
                int intAnno = Integer.parseInt(strAnno); 
                
                Autor a = new Autor(id, strNombres, strApellidos, intAnno, pais);
                
                aDao.actualizar(a);
                mostrarAutores();
                
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "ID Invaálido.");    
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No se actualizó ningún autor.");
        } 
    }
    
    public void cargarPaisesACombobox(){
        try{
            PaisDAO paisDao = new PaisDAO();
            Collection<Pais> paises = paisDao.mostrar_todo();
            for(Pais p : paises){
                this.vCrud.cboxPais.addItem(p);        
            }

        }catch(GlobalException | NoDataException ex){
            JOptionPane.showMessageDialog(vista, "Error al cargar los países: " + ex.getMessage());
        }
    
    }
    
    //*****************************************************************************
    
    public void mostrarVistaCRUD(int a){
        this.vCrud.setLocationRelativeTo(null);
        this.vCrud.setVisible(true);
        
        if(a==0){
            limpiarComponentesCRUD();
        
        }else{
            cargarAutor();
        }
        cargarPaisesACombobox();
    }
       
    
    public void limpiarComponentesCRUD(){
        this.vCrud.txtID.setText("Autoincremental");
        this.vCrud.txtNombres.setText("");
        this.vCrud.txtApellidos.setText("");
        this.vCrud.txtFecha.setText("");  
        
        if(this.vCrud.cboxPais.getItemCount()>0){
            this.vCrud.cboxPais.setSelectedIndex(0);
        }else{
            this.vCrud.cboxPais.setSelectedIndex(-1);
        }
    }
    
    
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        mostrarAutores();        
        cargarPaisesACombobox();
    }
    
    
}
