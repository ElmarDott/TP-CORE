package org.europa.together.application.internal;

import com.lowagie.text.pdf.PdfReader;
import java.io.IOException;

/**
 * This class hides implementation details of the OpenPdf PdfRendere class used
 * in the API.
 */
public class PdfDocument extends PdfReader {

    public PdfDocument(final String filename) throws IOException {
        super(filename);
    }

    public PdfDocument(final PdfReader reader) throws IOException {
        super(reader);
    }
}
