package reportes;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class HeaderFooterPDF extends PdfPageEventHelper {

    private Image logo;

    public HeaderFooterPDF(Image logo) {
        this.logo = logo;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        PdfContentByte cb = writer.getDirectContent();

        // ========== ENCABEZADO ==========
        if (logo != null) {
            logo.scaleToFit(60, 60);
            logo.setAbsolutePosition(document.left(), document.top() + 10);
            try {
                cb.addImage(logo);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                new Phrase("Reporte de Usuarios - Luppy & Sus Mascotas",
                        new Font(Font.HELVETICA, 12, Font.BOLD)),
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.top() + 20, 0);

        // ========== PIE DE PÁGINA ==========
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                new Phrase("Página " + writer.getPageNumber(),
                        new Font(Font.HELVETICA, 10)),
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 10, 0);
    }
}
