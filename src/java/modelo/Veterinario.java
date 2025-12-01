package modelo;

public class Veterinario {
    private int id_veterinario;
    private int anios_experiencia;
    private String numero_licencia;
    private String especialidad;
    private int id_usuario;

    // Campos adicionales para mostrar nombre del usuario asociado
    private String nombreUsuario;
    private String apellidoUsuario;

    public int getId_veterinario() {
        return id_veterinario;
    }
    public void setId_veterinario(int id_veterinario) {
        this.id_veterinario = id_veterinario;
    }

    public int getAnios_experiencia() {
        return anios_experiencia;
    }
    public void setAnios_experiencia(int anios_experiencia) {
        this.anios_experiencia = anios_experiencia;
    }

    public String getNumero_licencia() {
        return numero_licencia;
    }
    public void setNumero_licencia(String numero_licencia) {
        this.numero_licencia = numero_licencia;
    }

    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    // nombreUsuario / apellidoUsuario
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }
    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }
}
