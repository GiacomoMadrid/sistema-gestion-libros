package modelo;

import java.util.Date;

/**
 *
 * @author Giacomo
 */
public class Autor {
    private int id;
    private String nombres;
    private String apellidos;
    private Integer anno_nacimiento;
    private Pais pais;
    private String nombre_completo;
    
    public Autor(int id, String nombres, String apellidos, int anno_nacimiento, Pais pais){
        this.id= id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.anno_nacimiento = anno_nacimiento;
        this.pais = pais;
        this.nombre_completo = nombres + " " + apellidos;               
    }
    
    public Autor(int id, String nombres, String apellidos, Pais pais){
        this.id= id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.anno_nacimiento = null;
        this.pais = pais;
        this.nombre_completo = nombres + " " + apellidos;               
    }
    
    public Autor(String nombres, String apellidos, Pais pais){
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.anno_nacimiento = null;
        this.pais = pais;
        this.nombre_completo = nombres + " " + apellidos;               
    }
    
    public Autor(String nombres, String apellidos, int anno_nacimiento, Pais pais){       
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.anno_nacimiento = anno_nacimiento;
        this.pais = pais;
        this.nombre_completo = nombres + " " + apellidos;               
    }
    
    public Autor(int id){
        this.id= id;             
    }
    
    //----------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getAnno_nacimiento() {
        return this.anno_nacimiento;
    }

    public void setAnno_nacimiento(int anno_nacimiento) {
        this.anno_nacimiento = anno_nacimiento;
    }

    public String getNombrePais() {
        return pais.getNombre();
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Pais getPais(){
        return pais;
    }
    
    public String getNombre_completo() {
        return nombres + " " + apellidos;
    }

}
