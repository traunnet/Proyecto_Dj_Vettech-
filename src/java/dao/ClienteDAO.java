package dao;

import modelos.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public ClienteDAO() {
        con = ConnBD.conectar();
    }

    // LISTAR
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();

        try {
            String sql = "SELECT id_cliente, fecha_registro, mascotas_registradas FROM cliente";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setFechaRegistro(rs.getString("fecha_registro"));
                c.setMascotasRegistradas(rs.getInt("mascotas_registradas"));
                lista.add(c);
            }

        } catch (Exception e) {
            System.out.println("Error listar cliente: " + e.getMessage());
        }

        return lista;
    }

    // BUSCAR POR ID
    public Cliente buscarPorId(int id) {
        Cliente c = new Cliente();

        try {
            String sql = "SELECT id_cliente, fecha_registro, mascotas_registradas FROM cliente WHERE id_cliente=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setFechaRegistro(rs.getString("fecha_registro"));
                c.setMascotasRegistradas(rs.getInt("mascotas_registradas"));
            }

        } catch (Exception e) {
            System.out.println("Error buscar cliente: " + e.getMessage());
        }

        return c;
    }

    // REGISTRAR
    public boolean registrar(Cliente c) {
        try {
            String sql = "INSERT INTO cliente(id_cliente, fecha_registro, mascotas_registradas) VALUES(?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, c.getIdCliente());
            ps.setString(2, c.getFechaRegistro());
            ps.setInt(3, c.getMascotasRegistradas());

            ps.execute();
            return true;

        } catch (Exception e) {
            System.out.println("Error registrar cliente: " + e.getMessage());
            return false;
        }
    }

    // ACTUALIZAR
    public boolean actualizar(Cliente c) {
        try {
            String sql = "UPDATE cliente SET fecha_registro=?, mascotas_registradas=? WHERE id_cliente=?";
            ps = con.prepareStatement(sql);

            ps.setString(1, c.getFechaRegistro());
            ps.setInt(2, c.getMascotasRegistradas());
            ps.setInt(3, c.getIdCliente());

            ps.execute();
            return true;

        } catch (Exception e) {
            System.out.println("Error actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR
    public boolean eliminar(int id) {
        try {
            String sql = "DELETE FROM cliente WHERE id_cliente=?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ps.execute();
            return true;

        } catch (Exception e) {
            System.out.println("Error eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}
