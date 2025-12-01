package beans;

import dao.ClienteDAO;
import modelos.Cliente;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "clienteBean")
@SessionScoped
public class ClienteBean {

    private Cliente cliente = new Cliente();
    private ClienteDAO clienteDAO = new ClienteDAO();

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // LISTA para index.xhtml
    public List<Cliente> getListaClientes() {
        return clienteDAO.listar();
    }

    // REGISTRAR
    public String registrar() {
        clienteDAO.registrar(cliente);
        cliente = new Cliente(); // limpiar
        return "index?faces-redirect=true";
    }

    // CARGAR POR ID EN editar.xhtml
    public void cargarPorId() {
        if (cliente.getIdCliente() != 0) {
            cliente = clienteDAO.buscarPorId(cliente.getIdCliente());
        }
    }

    // ACTUALIZAR
    public String actualizar() {
        clienteDAO.actualizar(cliente);
        return "index?faces-redirect=true";
    }

    // ELIMINAR
    public String eliminar(int id) {
        clienteDAO.eliminar(id);
        return "index?faces-redirect=true";
    }
}
