package controlador;
import conexion.AutorDAO;
import conexion.EditorialDAO;
import conexion.EjemplarDAO;
import conexion.GlobalException;
import conexion.NoDataException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Autor;
import modelo.Editorial;
import modelo.Ejemplar;
import vista.frmEditorial;
import vista.frmPrincipal;
import vista.frmAutor;
import vista.frmCRUDEjemplar;
import vista.frmPais;

/**
 *
 * @author Giacomo
 */
public class ControladorPrincipal {
    protected frmPrincipal vista;
    protected frmPais vistaP;
    protected frmEditorial vistaE;
    protected frmAutor vistaA;
    protected frmCRUDEjemplar vCrud;
    
    private ControladorPais contPais;
    private ControladorEditorial contEditorial;
    private ControladorAutor contAutor;
    
    private EjemplarDAO ejDao;
    private AutorDAO aDao;
    private EditorialDAO eDao;
    
    short flag;
    
    public ControladorPrincipal(frmPrincipal v) throws SQLException{
        this.vista = v;
        this.vistaP = new frmPais();
        this.vistaE = new frmEditorial();
        this.vistaA = new frmAutor();
        this.vCrud = new frmCRUDEjemplar();
        
        this.contPais = new ControladorPais(vistaP);
        this.contEditorial = new ControladorEditorial(vistaE);
        this.contAutor = new ControladorAutor(vistaA);        
        this.aDao = new AutorDAO();
        this.eDao = new EditorialDAO();
        this.ejDao = new EjemplarDAO();
        
        this.vista.bgBuscar.add(this.vista.radNombre);
        this.vista.bgBuscar.add(this.vista.radAutor);
        this.vista.bgBuscar.add(this.vista.radEditorial);
        this.vista.bgBuscar.add(this.vista.radPais);
        this.vista.bgBuscar.add(this.vista.radDisponible);
        
        //---------------------------------------------------------------------
                
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
                limpiarComponentesCRUD();
                flag = 1;
                mostrarVistaCRUD(flag);
            }
        });
        
        this.vista.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    eliminarEjemplar();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorAutor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        //---------------------------------------------------------------------
        
        this.vista.btnSolicitar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    solicitarLibro();
                } catch (GlobalException ex) {
                    Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoDataException ex) {
                    Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
        });
        
        this.vista.btnReporte.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //TO DO
            }
        });
        
        this.vista.btnMostrarTodo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                mostrarTodo();
            }
        });
        
        this.vista.btnBuscar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //TO DO
            }
        });
        
        //---------------------------------------------------------------------
        
        this.vCrud.btnAceptar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    if(flag == 0){
                        crearEjemplar();
                    }else{
                        actualizarEjemplar();
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
    
    //*************************************************************************
    public void mostrarTodo(){
        try{
            
            DefaultTableModel model = new DefaultTableModel();

            model.addColumn("ID");
            model.addColumn("Título");
            model.addColumn("Autor");
            model.addColumn("Editorial");
            model.addColumn("Año de Publicacion");
            model.addColumn("Pais");
            model.addColumn("Disponible");

            Collection<Ejemplar> ejemplares = ejDao.mostrar_todo();
            for(Ejemplar ejem : ejemplares){
                if(ejem.getAnno_publicacion() != null){
                    model.addRow(new Object[]{ejem.getId(),  ejem.getTitulo(), 
                        ejem.getNombreAutor(), ejem.getNombreEditorial(),  
                        ejem.getAnno_publicacion(), ejem.getAutor().getNombrePais(),
                        ejem.getDisponibilidad()});
                }else{
                    model.addRow(new Object[]{ejem.getId(),  ejem.getTitulo(), 
                        ejem.getNombreAutor(), ejem.getNombreEditorial(),  
                        "--", ejem.getAutor().getNombrePais(),
                        ejem.getDisponibilidad()});
                }
            }
            this.vista.tabPrincipal.setModel(model);

            this.vista.tabPrincipal.getColumnModel().getColumn(0).setPreferredWidth(40);
            this.vista.tabPrincipal.getColumnModel().getColumn(0).setMinWidth(40);
            this.vista.tabPrincipal.getColumnModel().getColumn(0).setMaxWidth(40);
            this.vista.tabPrincipal.getColumnModel().getColumn(0).setWidth(40);

        }catch(GlobalException | NoDataException ex){
            JOptionPane.showMessageDialog(vista, "Error al cargar los autores: " + ex.getMessage());
        }
    }
    
    public void cargarEjemplar(){            
        try{
            String str = JOptionPane.showInputDialog("Id del autor a actualizar: ");
            int idEj = Integer.parseInt(str);
            Ejemplar aut = (Ejemplar) ejDao.buscar(new Ejemplar(idEj));
            this.vCrud.txtID.setText(""+aut.getId());
            this.vCrud.txtTitulo.setText(aut.getTitulo());
            this.vCrud.txtAnno.setText(""+aut.getAnno_publicacion());
            this.vCrud.chkDisponible.setSelected(aut.isDisponible()==1);
                        
        }catch(GlobalException | NoDataException | NumberFormatException ex){
            JOptionPane.showMessageDialog(vista, "Error al cargar el autor: " + ex.getMessage());
        }
        
    }
    
    public void crearEjemplar() throws GlobalException, NoDataException{
        this.vCrud.chkDisponible.setSelected(true);
        this.vCrud.chkDisponible.setEnabled(false);
        
        String strTitulo = this.vCrud.txtTitulo.getText();
        String strAnno = this.vCrud.txtAnno.getText();
        Autor autor = (Autor) this.vCrud.cboxAutor.getItemAt(this.vCrud.cboxAutor.getSelectedIndex());
        Editorial editorial = (Editorial) this.vCrud.cboxEditorial.getItemAt(this.vCrud.cboxEditorial.getSelectedIndex());
        boolean disp = this.vCrud.chkDisponible.isSelected();
        
        Integer anno;
        
        if(strAnno.isBlank() || strAnno.isEmpty()){
            anno = 0;
        }else{
            anno = Integer.parseInt(strAnno);
        }
        
        if((strTitulo != null && strTitulo!= "")
                ||(autor != null)
                ||(editorial != null)){
                                   
            Ejemplar ejm = new Ejemplar(autor, editorial, strTitulo, 1, anno);
            ejDao.crear(ejm);
            mostrarTodo();
            
        }else{
            JOptionPane.showMessageDialog(null, "No se agregó el autor.");
        }   
                        
    }
        
    public void eliminarEjemplar() throws GlobalException, NoDataException{
        String str = JOptionPane.showInputDialog("Id del autor a eliminar: ");
        if(str != null && str != ""){
            try {
                int id = Integer.parseInt(str);
                Ejemplar a = new Ejemplar(id);
                ejDao.eliminar(a);
                mostrarTodo();
                
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "ID Inválido\nNo se eliminó ningún autor.");    
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No se eliminó ningún autor.");
        }    
    }
    
    public void actualizarEjemplar() throws GlobalException, NoDataException{
        this.vCrud.chkDisponible.setSelected(false);
        this.vCrud.chkDisponible.setEnabled(true);
        
        this.vCrud.chkDisponible.setSelected(true);
        this.vCrud.chkDisponible.setEnabled(false);
        
        String str = this.vCrud.txtID.getText();
        String strTitulo = this.vCrud.txtTitulo.getText();
        String strAnno = this.vCrud.txtAnno.getText();
        Autor autor = (Autor) this.vCrud.cboxAutor.getItemAt(this.vCrud.cboxAutor.getSelectedIndex());
        Editorial editorial = (Editorial) this.vCrud.cboxEditorial.getItemAt(this.vCrud.cboxEditorial.getSelectedIndex());
        boolean disp = this.vCrud.chkDisponible.isSelected();
        short dispo;
        
        Integer anno;        
        if(strAnno.isBlank() || strAnno.isEmpty()){
            anno = 0;
        }else{
            anno = Integer.parseInt(strAnno);
        }
        
        if((strTitulo != null && strTitulo!= "")
                ||(autor != null)
                ||(editorial != null)){
            
             try {
                if(disp){
                    dispo = 1;
                }else{
                    dispo = 0;
                }
                 
                int id = Integer.parseInt(str);                       
                Ejemplar ejm = new Ejemplar(id, autor, editorial, strTitulo, dispo, anno);
                ejDao.actualizar(ejm);
                mostrarTodo();
                
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "ID Invaálido.");    
            }
        }else{
            JOptionPane.showMessageDialog(null, "No se agregó el autor.");
        }         
             
    }
    
    public void buscarPorAutor(int id)throws GlobalException, NoDataException{
        Autor aut = new Autor(id);
        mostrarLista(ejDao.buscar_por_autor(aut));    
    }
    
    public void buscarPorEditorial(int id)throws GlobalException, NoDataException{
        Editorial ed = new Editorial(id);
        mostrarLista(ejDao.buscar_por_editorial(ed));    
    }
    
    public void buscarPorDisponibiliad(int id)throws GlobalException, NoDataException{
        Ejemplar ej = new Ejemplar(id);
        mostrarLista(ejDao.buscar_por_disponibilidad(ej));    
    }
    
    public void buscarPorTitulo(String nom)throws GlobalException, NoDataException{
        Ejemplar ej = new Ejemplar(nom);
        mostrarLista(ejDao.buscar_por_titulo(nom));    
    }
            
            
            
    public void mostrarLista(Collection<Ejemplar> lista){
        try{
            
            DefaultTableModel model = new DefaultTableModel();

            model.addColumn("ID");
            model.addColumn("Título");
            model.addColumn("Autor");
            model.addColumn("Editorial");
            model.addColumn("Año de Publicacion");
            model.addColumn("Pais");
            model.addColumn("Disponible");

            lista = ejDao.mostrar_todo();
            for(Ejemplar ejem : lista){
                if(ejem.getAnno_publicacion() != null){
                    model.addRow(new Object[]{ejem.getId(),  ejem.getTitulo(), 
                        ejem.getNombreAutor(), ejem.getNombreEditorial(),  
                        ejem.getAnno_publicacion(), ejem.getAutor().getNombrePais(),
                        ejem.getDisponibilidad()});
                }else{
                    model.addRow(new Object[]{ejem.getId(),  ejem.getTitulo(), 
                        ejem.getNombreAutor(), ejem.getNombreEditorial(),  
                        "--", ejem.getAutor().getNombrePais(),
                        ejem.getDisponibilidad()});
                }
            }
            this.vista.tabPrincipal.setModel(model);

            this.vista.tabPrincipal.getColumnModel().getColumn(0).setPreferredWidth(40);
            this.vista.tabPrincipal.getColumnModel().getColumn(0).setMinWidth(40);
            this.vista.tabPrincipal.getColumnModel().getColumn(0).setMaxWidth(40);
            this.vista.tabPrincipal.getColumnModel().getColumn(0).setWidth(40);

        }catch(GlobalException | NoDataException ex){
            JOptionPane.showMessageDialog(vista, "Error al cargar los autores: " + ex.getMessage());
        }
    }
            
    //**************************************************************************
    
    public void solicitarLibro() throws GlobalException, NoDataException{
        try {
            String str = (JOptionPane.showInputDialog("Codigo del libro a solicitar."));
            if(str.isBlank() || str.isEmpty()){
                JOptionPane.showMessageDialog(null, "Código no válido, por favor, inténtelo de nuevo");
            }else{
                int id = Integer.parseInt(str);
                Ejemplar ejm = new Ejemplar(id);
                ejDao.solicitar(ejm);                
            }
            
            mostrarTodo();
            
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "ID Invaálido.");  
        }
    }
    
    public void regresarLibro() throws GlobalException, NoDataException{
        try {
            String str = (JOptionPane.showInputDialog("Codigo del libro a solicitar."));
            if(str.isBlank() || str.isEmpty()){
                JOptionPane.showMessageDialog(null, "Código no válido, por favor, inténtelo de nuevo");
            }else{
                int id = Integer.parseInt(str);
                Ejemplar ejm = new Ejemplar(id);
                ejDao.devolver(ejm);                
            }
            
            mostrarTodo();
            
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "ID Invaálido.");  
        }
    }
    
    
    
    
    
    
    //************************************************************************
    
    public void cargarEditorialesACombobox(){
        try{
            Collection<Editorial> editoriales = eDao.mostrar_todo();
            for(Editorial p : editoriales){
                this.vCrud.cboxEditorial.addItem(p);        
            }

        }catch(GlobalException | NoDataException ex){
            JOptionPane.showMessageDialog(vista, "Error al cargar los países: " + ex.getMessage());
        }    
    }
    
    public void cargarAtuoresACombobox(){
        try{
            Collection<Autor> autores = aDao.mostrar_todo();
            for(Autor p : autores){
                this.vCrud.cboxAutor.addItem(p);        
            }

        }catch(GlobalException | NoDataException ex){
            JOptionPane.showMessageDialog(vista, "Error al cargar los países: " + ex.getMessage());
        }    
    }
    
    
    //*************************************************************************
    public String obtenerTextoEntrada(){
        return vista.txtBusqueda.getText();
    }
    
    public void mostrarVistaCRUD(int a){
        this.vCrud.setLocationRelativeTo(null);
        this.vCrud.setVisible(true);
        
        if(a==0){
            limpiarComponentesCRUD();
        
        }else{
            cargarEjemplar();
        }
    }
    
    public void limpiarComponentesCRUD(){
        this.vCrud.txtID.setText("Autoincremental");
        this.vCrud.txtTitulo.setText("");
        this.vCrud.txtAnno.setText("");
        
        if(this.vCrud.cboxAutor.getItemCount()>0){
            this.vCrud.cboxAutor.setSelectedIndex(0);
        }else{
            this.vCrud.cboxAutor.setSelectedIndex(-1);
        }
        
        if(this.vCrud.cboxEditorial.getItemCount()>0){
            this.vCrud.cboxEditorial.setSelectedIndex(0);
        }else{
            this.vCrud.cboxEditorial.setSelectedIndex(-1);
        }
        
        this.vCrud.chkDisponible.setSelected(true);
        this.vCrud.chkDisponible.setEnabled(false);
    }
        
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        mostrarTodo();
        cargarAtuoresACombobox();
        cargarEditorialesACombobox();
    }
    
    
    
}
