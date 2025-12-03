package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;

public class UsuarioDAO {
    private Connection con;

    public UsuarioDAO() {
        con = ConnBD.conectar();
    }

    public List<Usuario> listarU() {
        List<Usuario> listUsr = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.tipo_doc, u.nombre, u.apellido, u.correo, u.telefono, " +
                     "u.direccion, u.foto_perfil, u.id_rol, r.nombre_rol " +
                     "FROM usuario u LEFT JOIN rol r ON u.id_rol = r.id_rol";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setTipoDoc(rs.getString("tipo_doc"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setCorreo(rs.getString("correo"));
                u.setTelefono(rs.getString("telefono"));
                u.setDireccion(rs.getString("direccion"));
                u.setFotoPerfil(rs.getBytes("foto_perfil"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setRolNombre(rs.getString("nombre_rol"));
                listUsr.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listUsr;
    }
    public List<Usuario> listarClientes() {
    List<Usuario> lista = new ArrayList<>();
    String sql = "SELECT * FROM usuario WHERE id_rol = 3";

    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<Usuario> listarVeterinarios() {
    List<Usuario> lista = new ArrayList<>();
    String sql = "SELECT * FROM usuario WHERE id_rol = 2";

    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void cambiarRolA2(int idUsuario) {
    String sql = "UPDATE usuario SET id_rol = 2 WHERE id_usuario = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void guardar(Usuario usr) {
        String sql = "INSERT INTO usuario (tipo_doc, nombre, apellido, correo, telefono, direccion, contrasena, foto_perfil, id_rol) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usr.getTipoDoc());
            ps.setString(2, usr.getNombre());
            ps.setString(3, usr.getApellido());
            ps.setString(4, usr.getCorreo());
            ps.setString(5, usr.getTelefono());
            ps.setString(6, usr.getDireccion());
            ps.setString(7, usr.getContrasena());
            ps.setBytes(8, usr.getFotoPerfil());
            ps.setInt(9, usr.getIdRol());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario buscar(int id) {
        Usuario usr = null;
        String sql = "SELECT u.*, r.nombre_rol FROM usuario u " +
                     "LEFT JOIN rol r ON u.id_rol = r.id_rol WHERE u.id_usuario = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usr = new Usuario();
                    usr.setIdUsuario(rs.getInt("id_usuario"));
                    usr.setTipoDoc(rs.getString("tipo_doc"));
                    usr.setNombre(rs.getString("nombre"));
                    usr.setApellido(rs.getString("apellido"));
                    usr.setCorreo(rs.getString("correo"));
                    usr.setTelefono(rs.getString("telefono"));
                    usr.setDireccion(rs.getString("direccion"));
                    usr.setContrasena(rs.getString("contrasena"));
                    usr.setContrasenaAnterior(rs.getString("contrasena"));
                    usr.setFotoPerfil(rs.getBytes("foto_perfil"));
                    usr.setIdRol(rs.getInt("id_rol"));
                    usr.setRolNombre(rs.getString("nombre_rol"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usr;
    }

    
    public void actualizar(Usuario usr) {
        String sql = "UPDATE usuario SET tipo_doc=?, nombre=?, apellido=?, correo=?, telefono=?, " +
                     "direccion=?, contrasena=?, foto_perfil=?, id_rol=? WHERE id_usuario=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usr.getTipoDoc());
            ps.setString(2, usr.getNombre());
            ps.setString(3, usr.getApellido());
            ps.setString(4, usr.getCorreo());
            ps.setString(5, usr.getTelefono());
            ps.setString(6, usr.getDireccion());

            String pwd = usr.getContrasena();
            if (pwd == null || pwd.trim().isEmpty()) {
                pwd = usr.getContrasenaAnterior();
            }

            ps.setString(7, pwd);
            ps.setBytes(8, usr.getFotoPerfil());
            ps.setInt(9, usr.getIdRol());
            ps.setInt(10, usr.getIdUsuario());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario buscarPorCorreo(String correo) {
        Usuario usr = null;
        String sql = "SELECT u.*, r.nombre_rol FROM usuario u " +
                     "LEFT JOIN rol r ON u.id_rol = r.id_rol WHERE u.correo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usr = new Usuario();
                    usr.setIdUsuario(rs.getInt("id_usuario"));
                    usr.setTipoDoc(rs.getString("tipo_doc"));
                    usr.setNombre(rs.getString("nombre"));
                    usr.setApellido(rs.getString("apellido"));
                    usr.setCorreo(rs.getString("correo"));
                    usr.setTelefono(rs.getString("telefono"));
                    usr.setDireccion(rs.getString("direccion"));
                    usr.setFotoPerfil(rs.getBytes("foto_perfil"));
                    usr.setContrasena(rs.getString("contrasena"));
                    usr.setIdRol(rs.getInt("id_rol"));
                    usr.setRolNombre(rs.getString("nombre_rol"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usr;
    }
    //
    public List<Usuario> filtrarUsuarios(String nombre, String correo, String rol) {
    List<Usuario> lista = new ArrayList<>();

    String sql = "SELECT u.id_usuario, u.tipo_doc, u.nombre, u.apellido, u.correo, u.telefono, " +
                 "u.direccion, u.foto_perfil, u.id_rol, r.nombre_rol " +
                 "FROM usuario u LEFT JOIN rol r ON u.id_rol = r.id_rol " +
                 "WHERE 1=1 ";

    if (nombre != null && !nombre.isEmpty()) {
        sql += " AND (u.nombre LIKE ? OR u.apellido LIKE ?) ";
    }
    if (correo != null && !correo.isEmpty()) {
        sql += " AND u.correo LIKE ? ";
    }
    if (rol != null && !rol.isEmpty()) {
        sql += " AND r.nombre_rol LIKE ? ";
    }

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        int i = 1;

        if (nombre != null && !nombre.isEmpty()) {
            ps.setString(i++, "%" + nombre + "%");
            ps.setString(i++, "%" + nombre + "%");
        }
        if (correo != null && !correo.isEmpty()) {
            ps.setString(i++, "%" + correo + "%");
        }
        if (rol != null && !rol.isEmpty()) {
            ps.setString(i++, "%" + rol + "%");
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setTipoDoc(rs.getString("tipo_doc"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setCorreo(rs.getString("correo"));
                u.setTelefono(rs.getString("telefono"));
                u.setDireccion(rs.getString("direccion"));
                u.setFotoPerfil(rs.getBytes("foto_perfil"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setRolNombre(rs.getString("nombre_rol"));
                lista.add(u);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return lista;
}

}
