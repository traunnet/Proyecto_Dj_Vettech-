package beans;

import dao.VeterinarioDAO;
import dao.UsuarioDAO;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import modelo.Usuario;
import modelo.Veterinario;

@ManagedBean
@ViewScoped
public class VeterinarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Veterinario veterinario;
    private List<Veterinario> listaV;
    private List<Usuario> listaUsuarios;

    private int idSeleccionado;

    private VeterinarioDAO vDAO = new VeterinarioDAO();
    private UsuarioDAO uDAO = new UsuarioDAO();

    @PostConstruct
    public void init() {
        veterinario = new Veterinario();
        listaV = vDAO.listarV();
        listaUsuarios = uDAO.listarClientes(); // SOLO clientes para "Nuevo"
    }

    // ------------------------
    // GETTERS / SETTERS
    // ------------------------

    public Veterinario getVeterinario() {
        return veterinario;
    }
    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public List<Veterinario> getListaV() {
        return listaV;
    }
    public void setListaV(List<Veterinario> listaV) {
        this.listaV = listaV;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public int getIdSeleccionado() {
        return idSeleccionado;
    }
    public void setIdSeleccionado(int idSeleccionado) {
        this.idSeleccionado = idSeleccionado;
    }

    // ------------------------
    // ACCIONES
    // ------------------------

    public void cargarVeterinario() {

        if (idSeleccionado == 0) {
            // MODO NUEVO
            veterinario = new Veterinario();
            listaUsuarios = uDAO.listarClientes();
            return;
        }

        // MODO EDITAR: cargar veterinario completo
        veterinario = vDAO.buscarCompleto(idSeleccionado);

        // En editar no debe mostrarse selección
        listaUsuarios = null;
    }


    public String guardar() {
        FacesContext fc = FacesContext.getCurrentInstance();

        if (veterinario == null || veterinario.getId_usuario() == 0) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Debe seleccionar un usuario cliente.", null));
            return null;
        }

        Usuario u = uDAO.buscar(veterinario.getId_usuario());

        if (u == null) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Usuario no existe.", null));
            return null;
        }

        if (u.getIdRol() != 3) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Debe seleccionar un cliente (rol 3).", null));
            return null;
        }

        if (vDAO.existePorUsuario(veterinario.getId_usuario())) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Este usuario ya es veterinario.", null));
            return null;
        }

        // PROMOVER CLIENTE → VETERINARIO (rol 3 → 2)
        uDAO.cambiarRolA2(veterinario.getId_usuario());

        // GUARDAR
        vDAO.guardar(veterinario);

        listaV = vDAO.listarV();
        veterinario = new Veterinario();
        listaUsuarios = uDAO.listarClientes();

        return "index?faces-redirect=true";
    }

    public String actualizar() {

        if (veterinario == null || veterinario.getId_veterinario() == 0) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se encontró el veterinario.", null));
            return null;
        }

        vDAO.actualizar(veterinario);
        listaV = vDAO.listarV();

        return "index?faces-redirect=true";
    }

    public void eliminar(int id) {
        vDAO.eliminar(id);
        listaV = vDAO.listarV();
    }
}
