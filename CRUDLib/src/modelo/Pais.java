package modelo;

import conexion.GlobalException;
import conexion.NoDataException;
import conexion.PaisDAO;
import java.sql.SQLException;
import crudlib.CRUDLib;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giacomo
 */
public class Pais {
    int id;
    String nombre;
    
    public Pais(int id, String name){
        this.id = id;
        this.nombre = name;
    }
    
    public Pais(String name){
        this.nombre = name;
    }
    
    public Pais(int id){
        this.id = id;
    }
     
    
    //--------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    
}
