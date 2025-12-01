package beans;

import dao.UsuarioDAO;
import dao.RolDAO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import modelo.Rol;
import modelo.Usuario;

@ManagedBean
@ApplicationScoped
public class UsuarioBean {

    private Usuario usuario = new Usuario();
    private List<Usuario> listaU = new ArrayList<>();
    private UsuarioDAO uDAO = new UsuarioDAO();

    private List<Rol> listaRoles = new ArrayList<>();
    private RolDAO rDAO = new RolDAO();

    private Part archivoFoto;

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getListaU() {
        return listaU;
    }
    public void setListaU(List<Usuario> listaU) {
        this.listaU = listaU;
    }

    public Part getArchivoFoto() {
        return archivoFoto;
    }
    public void setArchivoFoto(Part archivoFoto) {
        this.archivoFoto = archivoFoto;
    }

    public List<Rol> getListaRoles() {
        return listaRoles;
    }
    public void setListaRoles(List<Rol> listaRoles) {
        this.listaRoles = listaRoles;
    }

    private byte[] procesarFoto(Part foto) throws Exception {

        if (foto == null || foto.getSize() == 0) {
            return null;
        }

        String tipo = foto.getContentType().toLowerCase();

        if (!tipo.equals("image/jpeg") && 
            !tipo.equals("image/jpg")  && 
            !tipo.equals("image/png")) {
            throw new Exception("Formato no permitido. Solo JPG, JPEG o PNG.");
        }

        InputStream is = foto.getInputStream();
        BufferedImage original = ImageIO.read(is);

        if (original == null) {
            throw new Exception("No se pudo leer la imagen.");
        }

        int newW = 300;
        int newH = 300;

        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = resized.createGraphics();
        g.drawImage(original, 0, 0, newW, newH, null);
        g.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resized, "jpg", os);

        return os.toByteArray();
    }

    public void listar() {
        usuario = new Usuario();
        listaU = uDAO.listarU();
        listaRoles = rDAO.listar();
    }

    public void guardar() {

        try {
            if (archivoFoto != null && archivoFoto.getSize() > 0) {
                usuario.setFotoPerfil(procesarFoto(archivoFoto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (usuario.getContrasena() != null && !usuario.getContrasena().trim().isEmpty()) {
            usuario.setContrasena(Utils.encriptar(usuario.getContrasena()));
        }

        uDAO.guardar(usuario);
        listar();
    }

    public void buscar(int id) {
        usuario = uDAO.buscar(id);
        listaRoles = rDAO.listar();
    }

    public void actualizar() {

        try {
            if (archivoFoto != null && archivoFoto.getSize() > 0) {
                usuario.setFotoPerfil(procesarFoto(archivoFoto));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (usuario.getContrasena() != null && usuario.getContrasena().trim().length() > 0) {
            usuario.setContrasena(Utils.encriptar(usuario.getContrasena()));
        } else {
            usuario.setContrasena("");
        }

        uDAO.actualizar(usuario);
        listar();
    }

    public void eliminar(int id) {
        uDAO.eliminar(id);
        listar();
    }
    
    public String getPreviewFoto() {
    try {
        if (archivoFoto == null || archivoFoto.getSize() == 0) {
            return null;
        }

        byte[] bytes = archivoFoto.getInputStream().readAllBytes();
        String base64 = java.util.Base64.getEncoder().encodeToString(bytes);

        return "data:" + archivoFoto.getContentType() + ";base64," + base64;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }   

    
}
