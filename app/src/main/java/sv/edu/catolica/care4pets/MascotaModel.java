package sv.edu.catolica.care4pets;

import java.time.LocalDate;

public class MascotaModel {

    private String nombre;
    private String descripcion;
    private int foto;
    private boolean tieneNotificacines;
    private int id;
    private String raza;
    private String especie;
    private String sexo;
    private String color;
    private String fechaNacimiento;

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    private String fechaEsterilizacion;
    private String esterilizacion;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRaza() {
        return raza;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFechaEsterilizacion() {
        return fechaEsterilizacion;
    }

    public void setFechaEsterilizacion(String fechaEsterilizacion) {
        this.fechaEsterilizacion = fechaEsterilizacion;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEsterilizacion() {
        return esterilizacion;
    }

    public void setEsterilizacion(String esterilizacion) {
        this.esterilizacion = esterilizacion;
    }
}
