package modelo;

import java.util.Date;

/**
 *
 * @author Giacomo
 */
public class Ejemplar {
    int id;
    String titulo;
    Autor autor;
    Editorial editorial;
    boolean disponible;
   Integer anno_publicacion;

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

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Integer getAnno_publicacion() {
        return anno_publicacion;
    }

    public void setAnno_publicacion(int anno_publicacion) {
        this.anno_publicacion = anno_publicacion;
    }
           
    
    
}
