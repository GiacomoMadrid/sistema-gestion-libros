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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Autor;
import modelo.Ejemplar;
import vista.frmEditorial;
import vista.frmPrincipal;
import vista.frmAutor;
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
    
    private ControladorPais contPais;
    private ControladorEditorial contEditorial;
    private ControladorAutor contAutor;
    
    private EjemplarDAO ejDao;
    private AutorDAO aDao;
    private EditorialDAO eDao;
    
    public ControladorPrincipal(frmPrincipal v) throws SQLException{
        this.vista = v;
        this.vistaP = new frmPais();
        this.vistaE = new frmEditorial();
        this.vistaA = new frmAutor();
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
    public void mostrarTodo(){
        try{
            
            DefaultTableModel model = new DefaultTableModel();

            model.addColumn("ID");
            model.addColumn("Título");
            model.addColumn("Autor");
            model.addColumn("Editorial");
            model.addColumn("Año de Publicacion");
            model.addColumn("Disponible");

            Collection<Ejemplar> ejemplares = ejDao.mostrar_todo();
            for(Ejemplar ejem : ejemplares){
                model.addRow(new Object[]{ejem.getId(),  ejem.getTitulo(), 
                     ejem.getNombreAutor(), ejem.getNombreEditorial(),  
                     ejem.getAnno_publicacion(), ejem.getDisponibilidad()});        
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
    
    
    
    
    
    //*************************************************************************
    public String obtenerTextoEntrada(){
        return vista.txtBusqueda.getText();
    }
    
    public void iniciar(){
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
        mostrarTodo();
    }
    
    
    
}
