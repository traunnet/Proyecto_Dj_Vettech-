package beans;

import dao.UsuarioDAO;
import modelo.Usuario;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

@ManagedBean
@RequestScoped
public class ReporteUsuariosBeanExcel {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void generarExcel() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarU();

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("Usuarios");

            // ====== ESTILO ENCABEZADO ======
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // ====== ENCABEZADOS ======
            Row headerRow = sheet.createRow(0);

            String[] titulos = {"ID", "Tipo Doc", "Nombre", "Apellido", "Correo",
                    "Teléfono", "Dirección", "Rol"};

            for (int i = 0; i < titulos.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(titulos[i]);
                cell.setCellStyle(headerStyle);
            }

            // ====== CONTENIDO ======
            int rowIndex = 1;

            for (Usuario u : usuarios) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(u.getIdUsuario());
                row.createCell(1).setCellValue(u.getTipoDoc());
                row.createCell(2).setCellValue(u.getNombre());
                row.createCell(3).setCellValue(u.getApellido());
                row.createCell(4).setCellValue(u.getCorreo());
                row.createCell(5).setCellValue(u.getTelefono());
                row.createCell(6).setCellValue(u.getDireccion());
                row.createCell(7).setCellValue(u.getRolNombre());
            }

            for (int i = 0; i < titulos.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ====== DESCARGA ======
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletResponse response = 
                (HttpServletResponse) fc.getExternalContext().getResponse();

            response.reset();
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"usuarios.xlsx\"");
            response.setContentType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            OutputStream out = response.getOutputStream();
            wb.write(out);
            wb.close();
            out.flush();
            out.close();

            fc.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
