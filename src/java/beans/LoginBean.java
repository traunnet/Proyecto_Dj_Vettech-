package beans;

import dao.ConnBD;
import dao.UsuarioDAO;
import modelo.Usuario;

import java.io.IOException;
import java.io.Serializable;
import java.sql.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private Usuario usuario = new Usuario();       // para login
    private Usuario nuevoUsuario = new Usuario();  // para registro
    private String nombreUsuario = "";
    private byte[] fotoPerfil;

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Usuario getNuevoUsuario() { return nuevoUsuario; }
    public void setNuevoUsuario(Usuario nuevoUsuario) { this.nuevoUsuario = nuevoUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }


public String autenticar() {

    try (Connection con = ConnBD.conectar()) {

        String pwd = Utils.encriptar(usuario.getContrasena());

        String sql = "SELECT u.*, r.nombre_rol "
                + "FROM usuario u "
                + "LEFT JOIN rol r ON u.id_rol = r.id_rol "
                + "WHERE u.correo = ? AND u.contrasena = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, usuario.getCorreo());
        ps.setString(2, pwd);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
            String rol = rs.getString("nombre_rol");
            int idUsuario = rs.getInt("id_usuario");

            fotoPerfil = rs.getBytes("foto_perfil");

            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.getExternalContext().getSessionMap().put("user", nombreCompleto);
            ctx.getExternalContext().getSessionMap().put("tipo", rol.toUpperCase());
            ctx.getExternalContext().getSessionMap().put("id_usuario", idUsuario);

            switch (rol.toUpperCase()) {
                case "ADMINISTRADOR": return "/admin/index?faces-redirect=true";
                case "VETERINARIO":   return "/veterinario/dashboard?faces-redirect=true";
                case "CLIENTE":       return "/cliente/dashboard?faces-redirect=true";
                default:              return "/sinacceso?faces-redirect=true";
            }

        } else {
 
            try {
                FacesContext ctx = FacesContext.getCurrentInstance();
                ctx.getExternalContext().redirect("error.xhtml");
                ctx.responseComplete();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL,
                        "Error interno en el servidor", null));
        return null;
    }
}



    public String registrar() {
        UsuarioDAO udao = new UsuarioDAO();

        try {
           
            Usuario existente = udao.buscarPorCorreo(nuevoUsuario.getCorreo());
            if (existente != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "El correo ya está registrado.", null));
                return null;
            }

   
            if (nuevoUsuario.getContrasena() == null || nuevoUsuario.getContrasena().trim().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Debe ingresar una contraseña.", null));
                return null;
            }

 
            nuevoUsuario.setContrasena(Utils.encriptar(nuevoUsuario.getContrasena()));


            nuevoUsuario.setIdRol(3);


            udao.guardar(nuevoUsuario);


            Usuario u = udao.buscarPorCorreo(nuevoUsuario.getCorreo());


            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.getExternalContext().getSessionMap().put("user", u.getNombre() + " " + u.getApellido());
            ctx.getExternalContext().getSessionMap().put("tipo", "CLIENTE");
            ctx.getExternalContext().getSessionMap().put("id_usuario", u.getIdUsuario());

     
            return "/faces/cliente/dashboard.xhtml?faces-redirect=true";


        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "Error al registrar usuario.", null));
            return null;
        }
    }



    public String cerrar_sesion() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getExternalContext().invalidateSession();
        return "/login?faces-redirect=true";
    }

    public void verif_sesion(String tipoRequerido) {
        FacesContext ctx = FacesContext.getCurrentInstance();

        String user = (String) ctx.getExternalContext().getSessionMap().get("user");
        String tipo = (String) ctx.getExternalContext().getSessionMap().get("tipo");

        if (user == null || tipo == null || !tipo.equalsIgnoreCase(tipoRequerido)) {
            redirect("/sinacceso.xhtml");
            return;
        }

        nombreUsuario = user;
    }

    private void redirect(String ruta) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            String root = ctx.getExternalContext().getRequestContextPath();
            ctx.getExternalContext().redirect(root + "/faces" + ruta);
            ctx.responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
