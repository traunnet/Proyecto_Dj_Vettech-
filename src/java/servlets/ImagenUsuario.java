package servlets;

import dao.UsuarioDAO;
import modelo.Usuario;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ImagenUsuario")
public class ImagenUsuario extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            UsuarioDAO uDAO = new UsuarioDAO();
            Usuario u = uDAO.buscar(id);

            byte[] foto = u.getFotoPerfil();

            if (foto == null || foto.length == 0) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.setContentType("image/*");
            OutputStream out = response.getOutputStream();
            out.write(foto);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
