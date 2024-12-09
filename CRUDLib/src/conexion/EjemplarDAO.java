package conexion;

import conexion.GlobalException;
import conexion.NoDataException;
import java.util.Collection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import modelo.Autor;
import modelo.Editorial;
import modelo.Ejemplar;
import modelo.Pais;
import oracle.jdbc.OracleTypes;
/**
 *
 * @author Giacomo
 */
public class EjemplarDAO extends Conexion implements I_Conexiones{    
    private static final String MOSTRAR_EJEMPLARES ="{? = call mostrar_todo()}" ;
    private static final String CREAR_EJEMPLARES = "{call registrar_ejemplar(?,?,?,?)}";
    private static final String ELIMINAR_EJEMPLARES = "{call eliminar_ejemplar(?)}";
    private static final String ACTUALIZAR_EJEMPLARES = "{call actualizar_ejemplar(?,?,?,?,?,?)}";
    private static final String ENCONTRAR_UN_EJEMPLAR ="{? = call buscar_ejemplar(?)}" ;
    private static final String SOLICITAR_EJEMPLAR ="{call pedir_libro(?)}" ;
    private static final String DEVOLVER_EJEMPLAR ="{call devolver_libro(?)}" ;
    private static final String BUSCAR_POR_TITULO ="{? = call buscar_por_titulo(?)}";
    private static final String BUSCAR_POR_EDITORIAL ="{? = call buscar_por_editorial(?)}";
    private static final String BUSCAR_POR_AUTOR ="{? = call buscar_por_autor(?)}";
    private static final String BUSCAR_POR_PAIS ="{? = call buscar_por_pais(?)}";
    private static final String BUSCAR_DISPONIBLES ="{? = call buscar_por_disponibilidad()}";
    private static final String MOSTRAR_DATA = "{call mostrar_data(?,?,?,?,?,?,?,?,?,?,?)}";
    private static final String OBTENER_LINEA = "BEGIN DBMS_OUTPUT.GET_LINE(?, ?); END;";
    
    private PaisDAO pDao = new PaisDAO();
    private EditorialDAO eDao = new EditorialDAO();
    private AutorDAO aDao = new AutorDAO();
    
    @Override
    public Collection mostrar_todo() throws GlobalException, NoDataException {
        try{
            conectar();
        
        }catch(ClassNotFoundException ex){
            throw new GlobalException("No se ha localizado el driver"); 
            
        }catch(SQLException exe){
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        Ejemplar ejm = null;
        CallableStatement pstmt =null; 
        
        try{
            pstmt = conexion.prepareCall(MOSTRAR_EJEMPLARES);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();
            
            rs = (ResultSet)pstmt.getObject(1);
            
            while(rs.next()){
                int autorId = rs.getInt("autor");                
                int edId = rs.getInt("editorial");
                Autor autor = (Autor) aDao.buscar(new Autor(autorId)); 
                Editorial editorial = (Editorial) eDao.buscar(new Editorial(edId)); 
                
                ejm = new Ejemplar(
                        rs.getInt("id"), 
                        autor,
                        editorial,
                        rs.getString("titulo"), 
                        rs.getInt("disponible"), 
                        rs.getInt("anno_publicacion")
                );
                lista.add(ejm);                
            }
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            throw new GlobalException("Sentencia inválida");
        
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(pstmt != null){
                    pstmt.close();
                }
                
                desconectar();
                
            }catch(SQLException se){
                throw new GlobalException("Estatutos no válidos");
            
            }        
        }
                
        if(lista == null || lista.size() == 0){
            throw new NoDataException("No hay datos");
        }        
        return lista;        
    }

    @Override
    public void crear(Object obj) throws GlobalException, NoDataException {
        try{
            conectar();
        
        }catch(ClassNotFoundException ex){
            throw new GlobalException("No se ha localizado el driver"); 
            
        }catch(SQLException exe){
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        CallableStatement pstmt =null;
        Ejemplar ej = (Ejemplar) obj;
        
        try{
            pstmt = conexion.prepareCall(CREAR_EJEMPLARES);
            pstmt.setInt(1, ej.getAutor().getId());
            pstmt.setInt(2, ej.getEditorial().getId());  
            pstmt.setString(3, ej.getTitulo());  
            pstmt.setInt(4, ej.getAnno_publicacion());
            boolean resultado = pstmt.execute();
            
            if(resultado){
                throw new NoDataException("No se realizó la insersión");
            }
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            throw new GlobalException();
        
        }finally{
            try{
                if(pstmt != null){
                    pstmt.close();
                }
                desconectar();
            }catch(SQLException ex){
                throw new GlobalException();                
            }
        }
        
        
    }

    @Override
    public void eliminar(Object obj) throws GlobalException, NoDataException {
        try{
            conectar();
        
        }catch(ClassNotFoundException ex){
            throw new GlobalException("No se ha localizado el driver"); 
            
        }catch(SQLException exe){
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        PreparedStatement pstmt = null;
        Ejemplar a = (Ejemplar) obj; 
        
        try{
            pstmt = conexion.prepareCall(ELIMINAR_EJEMPLARES);
            pstmt.setInt(1, a.getId());
            int resultado = pstmt.executeUpdate();
            
            if(resultado == 0){
                throw new NoDataException("No se realizó la eliminación");
            }
                    
        }catch(SQLException ex){
            throw new GlobalException("Sentecia no válida");
        
        }finally{
            try{
                if(pstmt != null){
                    pstmt.close();
                }
                desconectar();
            }catch(SQLException ex){
                throw new GlobalException("Estatutos inválidos");                
            }
        }
            
    }

    @Override
    public void actualizar(Object obj) throws GlobalException, NoDataException {
        try{
            conectar();
        
        }catch(ClassNotFoundException ex){
            throw new GlobalException("No se ha localizado el driver"); 
            
        }catch(SQLException exe){
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        PreparedStatement pstmt = null;
        Ejemplar a = (Ejemplar) obj; 
        
        try{
            pstmt = conexion.prepareCall(ACTUALIZAR_EJEMPLARES);
            pstmt.setInt(1, a.getId()); 
            pstmt.setInt(2, a.getAutor().getId());  
            pstmt.setInt(3, a.getEditorial().getId());    
            pstmt.setString(4, a.getTitulo()); 
            pstmt.setInt(5, a.isDisponible());
            pstmt.setInt(6, a.getAnno_publicacion());
            
            int resultado = pstmt.executeUpdate();
            
            if(resultado == 0){
                throw new NoDataException("No se realizó la actualización");
            }
                    
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        
        }finally{
            try{
                if(pstmt != null){
                    pstmt.close();
                }
                desconectar();
            }catch(SQLException ex){
                throw new GlobalException("Estatutos inválidos");                
            }
        }
        
    
    }
    
    @Override
    public Object buscar(Object obj) throws GlobalException, NoDataException {
        try {
            conectar();
        } catch (ClassNotFoundException ex) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException exe) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Ejemplar eObj = (Ejemplar) obj;
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(ENCONTRAR_UN_EJEMPLAR);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setInt(2, eObj.getId());
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                eObj.setId(rs.getInt("id"));
                eObj.setTitulo(rs.getString("titulo"));
                eObj.setAnno_publicacion(rs.getInt("anno_publicacion"));
                eObj.setDisponible(rs.getInt("disponible"));
                
                int idEditorial = rs.getInt("editorial");
                Editorial ed = (Editorial) eDao.buscar(new Editorial(idEditorial));
                eObj.setEditorial(ed);
                
                int idAutor = rs.getInt("autor");
                Autor aut = (Autor) aDao.buscar(new Autor(idAutor));
                eObj.setAutor(aut);
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            throw new GlobalException("Sentencia inválida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                desconectar();
            } catch (SQLException se) {
                throw new GlobalException("Estatutos no válidos");
            }
        }

        return eObj;
    }                
        
    public void solicitar(Object obj) throws GlobalException, NoDataException{
        try {
            conectar();
        } catch (ClassNotFoundException ex) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException exe) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        ResultSet rs = null;
        Ejemplar eObj = (Ejemplar) obj;
        CallableStatement pstmt = null;
        
        try {
            pstmt = conexion.prepareCall(SOLICITAR_EJEMPLAR);
            pstmt.setInt(1, eObj.getId());
            pstmt.execute();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            throw new GlobalException("Sentencia inválida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                desconectar();
            } catch (SQLException se) {
                throw new GlobalException("Estatutos no válidos");
            }
        }
    
    }
    
    public void devolver(Object obj) throws GlobalException, NoDataException{
        try {
            conectar();
        } catch (ClassNotFoundException ex) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException exe) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        ResultSet rs = null;
        Ejemplar eObj = (Ejemplar) obj;
        CallableStatement pstmt = null;
        
        try {
            pstmt = conexion.prepareCall(DEVOLVER_EJEMPLAR);
            pstmt.setInt(1, eObj.getId());
            pstmt.execute();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            throw new GlobalException("Sentencia inválida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                desconectar();
            } catch (SQLException se) {
                throw new GlobalException("Estatutos no válidos");
            }
        }
    
    }
    
    public Collection buscar_por_autor(Object obj)throws GlobalException, NoDataException{
        try{
            conectar();
        
        }catch(ClassNotFoundException ex){
            throw new GlobalException("No se ha localizado el driver"); 
            
        }catch(SQLException exe){
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        Autor aut = (Autor) obj;
        Ejemplar ejm = null;
        CallableStatement pstmt =null; 
        
        try{
            pstmt = conexion.prepareCall(BUSCAR_POR_AUTOR);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR); 
            pstmt.setInt(2, aut.getId()); 
            pstmt.execute();
            
            rs = (ResultSet)pstmt.getObject(1);
            
            while(rs.next()){
                int autorId = rs.getInt("autor");                
                int edId = rs.getInt("editorial");
                Autor autor = (Autor) aDao.buscar(new Autor(autorId)); 
                Editorial editorial = (Editorial) eDao.buscar(new Editorial(edId)); 
                
                ejm = new Ejemplar(
                        rs.getInt("id"), 
                        autor,
                        editorial,
                        rs.getString("titulo"), 
                        rs.getInt("disponible"), 
                        rs.getInt("anno_publicacion")
                );
                lista.add(ejm);                
            }
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ex.printStackTrace();
            throw new GlobalException("Sentencia inválida");
        
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(pstmt != null){
                    pstmt.close();
                }
                
                desconectar();
                
            }catch(SQLException se){
                throw new GlobalException("Estatutos no válidos");
            
            }        
        }
                
        if(lista == null || lista.size() == 0){
            JOptionPane.showMessageDialog(null, "No hay datos para esta selección.");
            //throw new NoDataException("No hay datos");
        }        
        return lista;  
    
    }
    
    public Collection buscar_por_editorial(Object obj)throws GlobalException, NoDataException{
        try{
            conectar();
        
        }catch(ClassNotFoundException ex){
            throw new GlobalException("No se ha localizado el driver"); 
            
        }catch(SQLException exe){
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        Editorial ed = (Editorial) obj;
        Ejemplar ejm = null;
        CallableStatement pstmt =null; 
        
        try{
            pstmt = conexion.prepareCall(BUSCAR_POR_EDITORIAL);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR); 
            pstmt.setInt(2, ed.getId()); 
            pstmt.execute();
            
            rs = (ResultSet)pstmt.getObject(1);
            
            while(rs.next()){
                int autorId = rs.getInt("autor");                
                int edId = rs.getInt("editorial");
                Autor autor = (Autor) aDao.buscar(new Autor(autorId)); 
                Editorial editorial = (Editorial) eDao.buscar(new Editorial(edId)); 
                
                ejm = new Ejemplar(
                        rs.getInt("id"), 
                        autor,
                        editorial,
                        rs.getString("titulo"), 
                        rs.getInt("disponible"), 
                        rs.getInt("anno_publicacion")
                );
                lista.add(ejm);                
            }
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            throw new GlobalException("Sentencia inválida");
        
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(pstmt != null){
                    pstmt.close();
                }
                
                desconectar();
                
            }catch(SQLException se){
                JOptionPane.showMessageDialog(null, "No hay datos para esta selección.");
            }        
        }
                
        if(lista == null || lista.size() == 0){
            throw new NoDataException("No hay datos");
        }        
        return lista;  
    
    }
    
    public Collection buscar_por_disponibilidad()throws GlobalException, NoDataException{
        try{
            conectar();
        
        }catch(ClassNotFoundException ex){
            throw new GlobalException("No se ha localizado el driver"); 
            
        }catch(SQLException exe){
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        Ejemplar ejm = null;
        CallableStatement pstmt =null; 
        
        try{
            pstmt = conexion.prepareCall(BUSCAR_DISPONIBLES);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();
            
            rs = (ResultSet)pstmt.getObject(1);
            
            while(rs.next()){
                int autorId = rs.getInt("autor");                
                int edId = rs.getInt("editorial");
                Autor autor = (Autor) aDao.buscar(new Autor(autorId)); 
                Editorial editorial = (Editorial) eDao.buscar(new Editorial(edId)); 
                
                ejm = new Ejemplar(
                        rs.getInt("id"), 
                        autor,
                        editorial,
                        rs.getString("titulo"), 
                        rs.getInt("disponible"), 
                        rs.getInt("anno_publicacion")
                );
                lista.add(ejm);                
            }
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            throw new GlobalException("Sentencia inválida");
        
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(pstmt != null){
                    pstmt.close();
                }
                
                desconectar();
                
            }catch(SQLException se){
                throw new GlobalException("Estatutos no válidos");
            
            }        
        }
                
        if(lista == null || lista.size() == 0){
            JOptionPane.showMessageDialog(null, "No hay datos para esta selección.");
        }         
        return lista;  
    
    }
    
    public Collection buscar_por_titulo(Object obj)throws GlobalException, NoDataException{
        try{
            conectar();
        
        }catch(ClassNotFoundException ex){
            throw new GlobalException("No se ha localizado el driver"); 
            
        }catch(SQLException exe){
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        Ejemplar ejm = (Ejemplar) obj;
        CallableStatement pstmt =null; 
        
        try{
            pstmt = conexion.prepareCall(BUSCAR_POR_TITULO);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, ejm.getTitulo());
            pstmt.execute();
            
            rs = (ResultSet)pstmt.getObject(1);
            
            while(rs.next()){
                int autorId = rs.getInt("autor");                
                int edId = rs.getInt("editorial");
                Autor autor = (Autor) aDao.buscar(new Autor(autorId)); 
                Editorial editorial = (Editorial) eDao.buscar(new Editorial(edId)); 
                
                ejm = new Ejemplar(
                        rs.getInt("id"), 
                        autor,
                        editorial,
                        rs.getString("titulo"), 
                        rs.getInt("disponible"), 
                        rs.getInt("anno_publicacion")
                );
                lista.add(ejm);                
            }
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            throw new GlobalException("Sentencia inválida");
        
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(pstmt != null){
                    pstmt.close();
                }
                
                desconectar();
                
            }catch(SQLException se){
                throw new GlobalException("Estatutos no válidos");
            
            }        
        }
                
        if(lista == null || lista.size() == 0){
            JOptionPane.showMessageDialog(null, "No hay datos para esta selección.");
        }          
        return lista;  
    
    }
    
    public Collection buscar_por_pais(Object obj)throws GlobalException, NoDataException{
        try{
            conectar();
        
        }catch(ClassNotFoundException ex){
            throw new GlobalException("No se ha localizado el driver"); 
            
        }catch(SQLException exe){
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        
        ResultSet rs = null;
        ArrayList lista = new ArrayList();
        Pais p = (Pais) obj;
        Ejemplar ejm = null;
        CallableStatement pstmt =null; 
        
        try{
            pstmt = conexion.prepareCall(BUSCAR_POR_PAIS);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR); 
            pstmt.setInt(2, p.getId()); 
            pstmt.execute();
            
            rs = (ResultSet)pstmt.getObject(1);
            
            while(rs.next()){
                int autorId = rs.getInt("autor");                
                int edId = rs.getInt("editorial");
                Autor autor = (Autor) aDao.buscar(new Autor(autorId)); 
                Editorial editorial = (Editorial) eDao.buscar(new Editorial(edId)); 
                
                ejm = new Ejemplar(
                        rs.getInt("id"), 
                        autor,
                        editorial,
                        rs.getString("titulo"), 
                        rs.getInt("disponible"), 
                        rs.getInt("anno_publicacion")
                );
                lista.add(ejm);                
            }
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            throw new GlobalException("Sentencia inválida");
        
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                
                if(pstmt != null){
                    pstmt.close();
                }
                
                desconectar();
                
            }catch(SQLException se){
                throw new GlobalException("Estatutos no válidos");
            
            }        
        }
                
        if(lista == null || lista.size() == 0){
            JOptionPane.showMessageDialog(null, "No hay datos para esta selección.");
        }          
        return lista;  
    
    }
    
    
    public void mostrar_data(int ejemplarId, JTextArea textArea) throws GlobalException, NoDataException {
    try {
        conectar();
    } catch (ClassNotFoundException ex) {
        throw new GlobalException("No se ha localizado el driver");
    } catch (SQLException exe) {
        throw new NoDataException("La base de datos no se encuentra disponible");
    }
    
    CallableStatement procedimientoStmt = null;
    try {
        procedimientoStmt = conexion.prepareCall(MOSTRAR_DATA);
        procedimientoStmt.setInt(1, ejemplarId);  
        
        procedimientoStmt.registerOutParameter(2, OracleTypes.INTEGER);   // lib_autor
        procedimientoStmt.registerOutParameter(3, OracleTypes.INTEGER);   // lib_editorial
        procedimientoStmt.registerOutParameter(4, OracleTypes.VARCHAR);   // lib_titulo
        procedimientoStmt.registerOutParameter(5, OracleTypes.INTEGER);   // lib_dis
        procedimientoStmt.registerOutParameter(6, OracleTypes.INTEGER);   // lib_anno
        procedimientoStmt.registerOutParameter(7, OracleTypes.INTEGER);   // pais_id
        procedimientoStmt.registerOutParameter(8, OracleTypes.VARCHAR);   // autor_nombres
        procedimientoStmt.registerOutParameter(9, OracleTypes.VARCHAR);   // autor_apellidos
        procedimientoStmt.registerOutParameter(10, OracleTypes.VARCHAR);  // ed_nombre
        procedimientoStmt.registerOutParameter(11, OracleTypes.VARCHAR);  // pais_nombre

        procedimientoStmt.execute();

        String titulo = procedimientoStmt.getString(4);
        int disponible = procedimientoStmt.getInt(5);
        int annoPublicacion = procedimientoStmt.getInt(6);
        String autorNombres = procedimientoStmt.getString(8);
        String autorApellidos = procedimientoStmt.getString(9);
        String editorialNombre = procedimientoStmt.getString(10);
        String paisNombre = procedimientoStmt.getString(11);

        StringBuilder reporteTexto = new StringBuilder();
        reporteTexto.append("*************************************************************************\n");
        reporteTexto.append("Detalles del Ejemplar\n");
        reporteTexto.append("ID: ").append(ejemplarId).append("\n");
        reporteTexto.append("Título: ").append(titulo).append("\n");
        reporteTexto.append("Autor: ").append(autorNombres).append(" ").append(autorApellidos).append("\n");
        reporteTexto.append("Editorial: ").append(editorialNombre).append("\n");
        reporteTexto.append("Año de Publicación: ").append(annoPublicacion).append("\n");
        reporteTexto.append("Disponible: ").append(disponible == 1 ? "Sí" : "No").append("\n");
        reporteTexto.append("País: ").append(paisNombre).append("\n");
        reporteTexto.append("\n");
                
        textArea.append(reporteTexto.toString());

    } catch (SQLException e) {
        e.printStackTrace();
        textArea.setText("Error al ejecutar el procedimiento: " + e.getMessage());
    } finally {
        try {
            if (procedimientoStmt != null) procedimientoStmt.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
    
}
