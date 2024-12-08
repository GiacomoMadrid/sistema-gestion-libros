package conexion;

import java.util.Collection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import modelo.Pais;
import oracle.jdbc.OracleTypes;
/**
 *
 * @author Giacomo
 */
public class PaisDAO extends Conexion implements I_Conexiones{
    private static final String MOSTRAR_PAISES ="{? = call mostrar_paises()}" ;
    private static final String CREAR_PAIS = "{call registrar_pais(?)}";
    private static final String ELIMINAR_PAIS = "{call eliminar_pais(?)}";
    
    
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
        Pais paisObj = null;
        CallableStatement pstmt =null; 
        
        try{
            pstmt = conexion.prepareCall(MOSTRAR_PAISES);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();
            
            rs = (ResultSet)pstmt.getObject(1);
            
            while(rs.next()){
               paisObj = new Pais(rs.getInt("id"), rs.getString("nombre"));
               lista.add(paisObj);
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
        Pais p = (Pais) obj; 
        
        try{
            pstmt = conexion.prepareCall(CREAR_PAIS);
            pstmt.setString(1, p.getNombre());
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
        Pais p = (Pais) obj; 
        
        try{
            pstmt = conexion.prepareCall(ELIMINAR_PAIS);
            pstmt.setInt(1, p.getId());
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}
