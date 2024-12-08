package modelo;


/**
 *
 * @author Giacomo
 */
public class Pais {
    private int id;
    private String nombre;
    
    public Pais(){
    }
    
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
    
    @Override
    public String toString(){
        return id + " - " + nombre;
    }
    
    
}
