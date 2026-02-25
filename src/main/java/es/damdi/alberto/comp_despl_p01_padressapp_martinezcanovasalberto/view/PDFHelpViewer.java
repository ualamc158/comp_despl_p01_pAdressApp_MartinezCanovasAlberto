package es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.view;


import com.dansoftware.pdfdisplayer.PDFDisplayer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The type Pdf help viewer.
 */
public class PDFHelpViewer extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Crear el visor PDF
        PDFDisplayer displayer = new PDFDisplayer();
        Scene scene = new Scene(displayer.toNode());

        // Obtener la URL del PDF
        URL pdfUrl = getClass().getResource("/help/pdf/ayuda.pdf");

        if (pdfUrl == null) {
            System.err.println("⚠️ El archivo PDF no se encontró en el classpath.");
            return;
        }

        try {
            // Convertir la URL en un archivo y cargar el PDF
            File pdfFile = new File(pdfUrl.toURI());
            displayer.loadPDF(pdfFile);
        } catch (URISyntaxException | IOException e) {
            System.err.println("❌ Error al cargar el PDF: " + e.getMessage());
            return;
        }

        // Configurar y mostrar la ventana
        primaryStage.setTitle("Ayuda - Manual de Usuario");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
