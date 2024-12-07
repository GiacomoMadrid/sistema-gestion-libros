package modelo;

import java.util.Date;

/**
 *
 * @author Giacomo
 */
public class Autor {
    int id;
    String nombres;
    String apellidos;
    Date fecha_nacimiento;
    Pais pais;
    String nombre_completo;
    
    public Autor(int id, String nombres, String apellidos, Date fecha_nacimiento, Pais pais){
        this.id= id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fecha_nacimiento = fecha_nacimiento;
        this.pais = pais;
        this.nombre_completo = nombres + " " + apellidos;
        
        
    }

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

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

}
