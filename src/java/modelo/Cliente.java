package modelos;

public class Cliente {

    private int idCliente;
    private String fechaRegistro;
    private int mascotasRegistradas;

    public Cliente() { }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getMascotasRegistradas() {
        return mascotasRegistradas;
    }

    public void setMascotasRegistradas(int mascotasRegistradas) {
        this.mascotasRegistradas = mascotasRegistradas;
    }
}
