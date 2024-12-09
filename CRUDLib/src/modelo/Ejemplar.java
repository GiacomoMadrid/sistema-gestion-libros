package modelo;

import java.util.Date;

/**
 *
 * @author Giacomo
 */
public class Ejemplar {
    private int id;
    private String titulo;
    private Autor autor;
    private Editorial editorial;
    private int disponible;
    private Integer anno_publicacion;
    
    public Ejemplar(int id, Autor a, Editorial e, String t, int d, Integer an){
        this.id = id;
        this.titulo = t;
        this.autor = a;
        this.editorial = e;
        this.disponible = d;
        this.anno_publicacion = an;
    }
    
    public Ejemplar(){
    }
    
    public Ejemplar(int id){
        this.id = id;
    }
    
    public Ejemplar(String t){
        this.titulo = t;
    }
    
    
    public Ejemplar(Autor a, Editorial e, String t,  int d, Integer an){
        this.titulo = t;
        this.autor = a;
        this.editorial = e;
        this.disponible = d;
        this.anno_publicacion = an;
    }
    
    //--------------------------------------------------------------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public int isDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    public Integer getAnno_publicacion() {
        return anno_publicacion;
    }

    public void setAnno_publicacion(int anno_publicacion) {
        this.anno_publicacion = anno_publicacion;
    }
    
    public String getNombreAutor(){
        return this.autor.getNombre_completo();
    }
    
    public String getNombreEditorial(){
        return this.editorial.getNombre();
    }
    
    public String getDisponibilidad(){
        if(this.disponible == 1){
            return "Sí";
        }else{
            return "No";
        }
    }
    
    @Override
    public String toString(){
        return id + " - " + titulo;
    }
    
}
