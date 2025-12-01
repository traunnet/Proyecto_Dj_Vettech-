package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Veterinario;

public class VeterinarioDAO {

    private Connection con;

    public VeterinarioDAO() {
        con = ConnBD.conectar();
    }

    // ===========================================================
    // LISTAR
    // ===========================================================
    public List<Veterinario> listarV() {
        List<Veterinario> lista = new ArrayList<>();

        String sql = "SELECT v.id_veterinario, v.numero_licencia, v.especialidad, "
                   + "v.anios_experiencia, v.id_usuario, "
                   + "u.nombre AS usuario_nombre, u.apellido AS usuario_apellido "
                   + "FROM veterinario v "
                   + "LEFT JOIN usuario u ON v.id_usuario = u.id_usuario "
                   + "ORDER BY v.id_veterinario DESC";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Veterinario v = new Veterinario();
                v.setId_veterinario(rs.getInt("id_veterinario"));
                v.setNumero_licencia(rs.getString("numero_licencia"));
                v.setEspecialidad(rs.getString("especialidad"));
                v.setAnios_experiencia(rs.getInt("anios_experiencia"));
                v.setId_usuario(rs.getInt("id_usuario"));

                v.setNombreUsuario(rs.getString("usuario_nombre"));
                v.setApellidoUsuario(rs.getString("usuario_apellido"));

                lista.add(v);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar veterinarios: " + e.getMessage());
        }

        return lista;
    }

    // ===========================================================
    // INSERTAR
    // ===========================================================
    public void guardar(Veterinario vr) {
        String sql = "INSERT INTO veterinario "
                   + "(numero_licencia, especialidad, anios_experiencia, id_usuario) "
                   + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vr.getNumero_licencia());
            ps.setString(2, vr.getEspecialidad());
            ps.setInt(3, vr.getAnios_experiencia());
            ps.setInt(4, vr.getId_usuario());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error guardando veterinario: " + e.getMessage());
        }
    }

    // ===========================================================
    // BUSCAR SIMPLE
    // ===========================================================
    public Veterinario buscar(int id) {

        String sql = "SELECT v.id_veterinario, v.numero_licencia, v.especialidad, "
                   + "v.anios_experiencia, v.id_usuario, "
                   + "u.nombre AS usuario_nombre, u.apellido AS usuario_apellido "
                   + "FROM veterinario v "
                   + "LEFT JOIN usuario u ON v.id_usuario = u.id_usuario "
                   + "WHERE v.id_veterinario = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Veterinario v = new Veterinario();

                    v.setId_veterinario(rs.getInt("id_veterinario"));
                    v.setNumero_licencia(rs.getString("numero_licencia"));
                    v.setEspecialidad(rs.getString("especialidad"));
                    v.setAnios_experiencia(rs.getInt("anios_experiencia"));
                    v.setId_usuario(rs.getInt("id_usuario"));

                    v.setNombreUsuario(rs.getString("usuario_nombre"));
                    v.setApellidoUsuario(rs.getString("usuario_apellido"));

                    return v;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar veterinario: " + e.getMessage());
        }

        return null;
    }

    // ===========================================================
    // BUSCAR COMPLETO (PARA EDITAR)
    // ===========================================================
    public Veterinario buscarCompleto(int id) {

        String sql = "SELECT v.*, "
                   + "u.nombre AS usuario_nombre, u.apellido AS usuario_apellido "
                   + "FROM veterinario v "
                   + "INNER JOIN usuario u ON v.id_usuario = u.id_usuario "
                   + "WHERE v.id_veterinario = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Veterinario vet = new Veterinario();

                vet.setId_veterinario(rs.getInt("id_veterinario"));
                vet.setId_usuario(rs.getInt("id_usuario"));
                vet.setNumero_licencia(rs.getString("numero_licencia"));
                vet.setEspecialidad(rs.getString("especialidad"));
                vet.setAnios_experiencia(rs.getInt("anios_experiencia"));

                vet.setNombreUsuario(rs.getString("usuario_nombre"));
                vet.setApellidoUsuario(rs.getString("usuario_apellido"));

                return vet;
            }

        } catch (SQLException e) {
            System.err.println("Error buscarCompleto: " + e.getMessage());
        }

        return null;
    }

    // ===========================================================
    // VERIFICAR SI UN USUARIO YA ES VETERINARIO
    // ===========================================================
    public boolean existePorUsuario(int id_usuario) {

        String sql = "SELECT COUNT(*) FROM veterinario WHERE id_usuario = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_usuario);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error existePorUsuario: " + e.getMessage());
        }

        return false;
    }

    // ===========================================================
    // ACTUALIZAR
    // ===========================================================
    public void actualizar(Veterinario vr) {

        String sql = "UPDATE veterinario SET "
                   + "numero_licencia=?, especialidad=?, anios_experiencia=?, id_usuario=? "
                   + "WHERE id_veterinario=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vr.getNumero_licencia());
            ps.setString(2, vr.getEspecialidad());
            ps.setInt(3, vr.getAnios_experiencia());
            ps.setInt(4, vr.getId_usuario());
            ps.setInt(5, vr.getId_veterinario());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error actualizando veterinario: " + e.getMessage());
        }
    }

    // ===========================================================
    // ELIMINAR
    // ===========================================================
    public void eliminar(int id) {

        String sql = "DELETE FROM veterinario WHERE id_veterinario = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error eliminando veterinario: " + e.getMessage());
        }
    }
}
