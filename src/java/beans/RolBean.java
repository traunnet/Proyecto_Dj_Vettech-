package beans;

import dao.RolDAO;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import modelo.Rol;

@ManagedBean
@ApplicationScoped
public class RolBean {

    private List<Rol> listaRoles = new ArrayList<>();
    private RolDAO rDAO = new RolDAO();

    public List<Rol> getListaRoles() {
        return listaRoles;
    }
    public void setListaRoles(List<Rol> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public void cargar() {
        listaRoles = rDAO.listar();
    }
}
