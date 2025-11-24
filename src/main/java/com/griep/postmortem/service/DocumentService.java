package com.griep.postmortem.service;

import com.griep.postmortem.domain.enums.DocFormatEnum;
import com.griep.postmortem.infra.exception.BadGatewayException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DocumentService {
    public String filename(final Long incidentId, final Integer version, final DocFormatEnum format) {
        return "postmortem-%d-v%d%s".formatted(incidentId, version, format.getExtension());
    }

    public byte[] convert(final byte[] mdContent, final DocFormatEnum format) {
        try {
            byte[] content;
            switch (format) {
                case HTML -> content = convertToHtml(mdContent);
                case PDF -> content = convertToPdf(convertToHtml(mdContent));
                default -> content = mdContent;
            }

            return content;
        } catch (IOException exception) {
            throw new BadGatewayException("Unable to generate document.");
        }
    }

    private byte[] convertToHtml(final byte[] mdContent) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(new String(mdContent));
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlContent = renderer.render(document);

        String builder = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css2?family=Exo+2:wght@300;400;500;600;700&amp;display=swap\" />" +
                "<style>\n" +
                "body {\n" +
                "    font-family: 'Exo 2', sans-serif;\n" +
                "}\n" +
                "</style>\n" +
                "<meta charset=\"UTF-8\" />\n" +
                "</head>\n" +
                "<body>\n" +
                htmlContent +
                "</body>\n" +
                "</html>";

        return builder.getBytes();
    }

    private byte[] convertToPdf(final byte[] htmlContent) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (os) {
            // Parse HTML with Jsoup to ensure well-formed XHTML
            // Document doc = Jsoup.parse(htmlContent, "UTF-8");
            // doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode(); // Optional: for faster rendering, may reduce precision
            builder.withHtmlContent(new String(htmlContent), null); // Pass the parsed HTML content
            builder.toStream(os);
            builder.run();
        }
        return os.toByteArray();
    }
}
