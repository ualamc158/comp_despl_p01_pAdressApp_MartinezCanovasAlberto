package es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.view;


import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * The type Markdown help.
 */
public class MarkdownHelp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Cargar el contenido del archivo Markdown
        String markdownContent = loadMarkdown();

        // Convertir Markdown a HTML utilizando Flexmark
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        Node document = parser.parse(markdownContent);
        String htmlContent = renderer.render(document);

        // Crear un WebView para mostrar el contenido HTML generado
        WebView webView = new WebView();
        webView.getEngine().loadContent(htmlContent);

        // Configurar la escena y mostrar la ventana
        primaryStage.setScene(new Scene(webView, 800, 600));
        primaryStage.setTitle("Ayuda en Markdown");
        primaryStage.show();
    }

    /**
     * Método para cargar el contenido del archivo Markdown desde los recursos del classpath.
     *
     * @return Contenido del archivo Markdown como String, o un mensaje de error si ocurre un problema.
     */
    private String loadMarkdown() {
        try (InputStream inputStream = getClass().getResourceAsStream("/help/markdown/README.md")) {
            if (inputStream == null) {
                System.err.println("⚠️ El archivo Markdown no se encontró dentro del JAR.");
                return "Error: No se pudo encontrar el archivo Markdown.";
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("❌ Error al cargar el archivo Markdown: " + e.getMessage());
            return "Error al cargar el archivo Markdown.";
        }
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
