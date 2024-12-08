package conexion;

import conexion.GlobalException;
import conexion.NoDataException;
import java.util.Collection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
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
        Autor a = (Autor) obj; 
        
        try{
            pstmt = conexion.prepareCall(CREAR_EJEMPLARES);
            pstmt.setString(1, a.getNombres());
            pstmt.setString(2, a.getApellidos());  
            pstmt.setInt(3, a.getPais().getId());  
            pstmt.setInt(4, a.getAnno_nacimiento());
            boolean resultado  = pstmt.execute();
            
            if(resultado){
                throw new NoDataException("No se realizó la insersión");
            }
            
        }catch(SQLException ex){
            ex.printStackTrace();
            throw new GlobalException("Llave Duplicada");
        
        }finally{
            try{
                if(pstmt != null){
                    pstmt.close();
                }
                desconectar();
            }catch(SQLException ex){
                throw new GlobalException("Datos inválidos.");                
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
        Autor a = (Autor) obj; 
        
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
        Autor a = (Autor) obj; 
        
        try{
            pstmt = conexion.prepareCall(ACTUALIZAR_EJEMPLARES);
            pstmt.setInt(1, a.getId()); 
            pstmt.setString(2, a.getNombres());  
            pstmt.setString(3, a.getApellidos());    
            pstmt.setInt(4, a.getPais().getId()); 
            pstmt.setInt(5, a.getAnno_nacimiento());
            
            int resultado = pstmt.executeUpdate();
            
            if(resultado == 0){
                throw new NoDataException("No se realizó la actualización");
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
    public Object buscar(Object obj) throws GlobalException, NoDataException {
        try {
            conectar();
        } catch (ClassNotFoundException ex) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException exe) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Autor autorObj = (Autor) obj;
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(ENCONTRAR_UN_EJEMPLAR);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setInt(2, autorObj.getId());
            pstmt.execute();

            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                autorObj.setId(rs.getInt("id"));
                autorObj.setNombres(rs.getString("nombres"));
                autorObj.setApellidos(rs.getString("apellidos"));
                autorObj.setAnno_nacimiento(rs.getInt("anno_nacimiento"));
                
                //int idPais = rs.getInt("pais");
                //Pais pais = (Pais) pDao.buscar(new Pais(idPais));
                //autorObj.setPais(pais);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

        return autorObj;
    }                
        
    
    
}
