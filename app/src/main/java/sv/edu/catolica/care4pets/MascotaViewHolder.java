package sv.edu.catolica.care4pets;

public class MascotaViewHolder {

    private String nombre;
    private String descripcion;
    private int foto;
    private boolean tieneNotificacines;

    public MascotaViewHolder(String nombre, String descripcion, int foto, boolean tieneNotificacines) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.foto = foto;
        this.tieneNotificacines = tieneNotificacines;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isTieneNotificacines() {
        return tieneNotificacines;
    }

    public void setTieneNotificacines(boolean tieneNotificacines) {
        this.tieneNotificacines = tieneNotificacines;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
