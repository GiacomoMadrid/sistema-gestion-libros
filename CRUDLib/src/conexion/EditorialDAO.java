package conexion;

import java.util.Collection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import modelo.Editorial;
import oracle.jdbc.OracleTypes;
/**
 *
 * @author Giacomo
 */
public class EditorialDAO extends Conexion implements I_Conexiones{
    private static final String MOSTRAR_EDITORIALES ="{? = call mostrar_editoriales()}" ;
    private static final String CREAR_EDITORIALES = "{call registrar_editorial(?)}";
    private static final String ELIMINAR_EDITORIALES = "{call eliminar_editorial(?)}";
    private static final String ACTUALIZAR_EDITORIALES = "{call actualizar_editorial(?,?)}";
    
    
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
        Editorial editorialObj = null;
        CallableStatement pstmt =null; 
        
        try{
            pstmt = conexion.prepareCall(MOSTRAR_EDITORIALES);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();
            
            rs = (ResultSet)pstmt.getObject(1);
            
            while(rs.next()){
               editorialObj = new Editorial(rs.getInt("id"), rs.getString("nombre"));
               lista.add(editorialObj);
            }
            
        }catch(SQLException ex){
            ex.printStackTrace();
            throw new GlobalException("Sentencia inválida");
        
        }finally{
            try{
                if(rs == null){
                    rs.close();
                }
                
                if(pstmt == null){
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
        Editorial ed = (Editorial) obj; 
        
        try{
            pstmt = conexion.prepareCall(CREAR_EDITORIALES);
            pstmt.setString(1, ed.getNombre());
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
        Editorial ed = (Editorial) obj; 
        
        try{
            pstmt = conexion.prepareCall(ELIMINAR_EDITORIALES);
            pstmt.setInt(1, ed.getId());
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
        Editorial ed = (Editorial) obj; 
        
        try{
            pstmt = conexion.prepareCall(ACTUALIZAR_EDITORIALES);
            pstmt.setInt(1, ed.getId());
            pstmt.setString(2, ed.getNombre());
            
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}
