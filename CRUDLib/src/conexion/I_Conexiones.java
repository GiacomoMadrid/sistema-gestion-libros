package conexion;

import java.util.Collection;

/**
 *
 * @author Giacomo
 */
public interface I_Conexiones {
    public Collection mostrar_todo() throws GlobalException, NoDataException;
    
    public void crear(Object obj)throws GlobalException, NoDataException;
        
    public void eliminar(Object obj)throws GlobalException, NoDataException;
        
    public void actualizar(Object obj)throws GlobalException, NoDataException;
    
    public Object buscar(Object obj) throws GlobalException, NoDataException;
    
   
}
