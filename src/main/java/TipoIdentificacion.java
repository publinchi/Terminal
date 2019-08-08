public class TipoIdentificacion {
    private Integer id;
    private String nombre;

    public TipoIdentificacion(Integer id) {
        this.id = id;
    }

    public TipoIdentificacion(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String toString() {
        return this.nombre;
    }
}

