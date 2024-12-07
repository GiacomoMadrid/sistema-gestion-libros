package modelo;

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
