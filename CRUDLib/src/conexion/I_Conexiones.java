package conexion;

import java.util.Collection;

/**
 *
 * @author Giacomo
 */
public interface I_Conexiones {
    public Collection mostrar_todo() throws GlobalException, NoDataException;
    
    public void crear(Object obj)throws GlobalException, NoDataException;
        
    public void delete(Object obj)throws GlobalException, NoDataException;
        
    public void update(Object obj)throws GlobalException, NoDataException;
    
   
}
