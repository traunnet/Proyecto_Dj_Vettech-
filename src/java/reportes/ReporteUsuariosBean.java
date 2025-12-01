package reportes;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import dao.UsuarioDAO;
import modelo.Usuario;
import reportes.HeaderFooterPDF;

import java.io.Serializable;
import java.awt.Color;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@ViewScoped
public class ReporteUsuariosBean implements Serializable {

    private UsuarioDAO dao = new UsuarioDAO();

    public void generarPDF() {

        try {
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response =
                    (HttpServletResponse) faces.getExternalContext().getResponse();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=usuariopsf.pdf"); // ← NUEVO NOMBRE

            Document doc = new Document(PageSize.A4, 36, 36, 70, 50);
            PdfWriter writer = PdfWriter.getInstance(doc, response.getOutputStream());

            // ========== LOGO ==========
            Image logo = null;
            try {
                String ruta = faces.getExternalContext().getRealPath("/resources/img/logo_luppy.png");
                logo = Image.getInstance(ruta);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            writer.setPageEvent(new HeaderFooterPDF(logo));

            doc.open();

            // ========== TÍTULO ==========
            Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD, Color.BLACK);
            Paragraph titulo = new Paragraph("Lista de Usuarios Registrados", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);
            doc.add(new Paragraph(" "));

            // ========== TABLA ==========
            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);

            Color headerColor = new Color(225, 225, 240);
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            String[] headers = {"ID", "Tipo Doc", "Nombre", "Apellido", "Correo", "Rol"};

            for (String h : headers) {
                PdfPCell celda = new PdfPCell(new Phrase(h, headerFont));
                celda.setBackgroundColor(headerColor);
                celda.setBorderColor(Color.GRAY);
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(celda);
            }

            List<Usuario> usuarios = dao.listarU();
            Font contenidoFont = new Font(Font.HELVETICA, 11);

            for (Usuario u : usuarios) {
                tabla.addCell(new Phrase(String.valueOf(u.getIdUsuario()), contenidoFont));
                tabla.addCell(new Phrase(u.getTipoDoc(), contenidoFont));
                tabla.addCell(new Phrase(u.getNombre(), contenidoFont));
                tabla.addCell(new Phrase(u.getApellido(), contenidoFont));
                tabla.addCell(new Phrase(u.getCorreo(), contenidoFont));
                tabla.addCell(new Phrase(u.getRolNombre(), contenidoFont));
            }

            doc.add(tabla);
            doc.close();
            faces.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
