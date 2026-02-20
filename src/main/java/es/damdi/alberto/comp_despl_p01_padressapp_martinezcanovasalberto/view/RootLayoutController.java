package es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.view;


import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.MainApp;
import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.model.Person;
import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.persistence.CsvPersonRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RootLayoutController {
    private CsvPersonRepository csvRepository = new CsvPersonRepository();

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // -------------------- MENU ACTIONS --------------------
    @FXML
    private void handleNew() {
        if (!confirmSaveIfDirty("New", "Create a new address book?")) {
            return; // cancelado
        }
        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null); // “no guardado previamente”
        mainApp.setDirty(false);
    }

    @FXML
    private void handleOpen() {
        if (!confirmSaveIfDirty("Open", "Open another address book?")) {
            return; // cancelado
        }
        FileChooser fc = createJsonFileChooser("Open Address Book (JSON)");
        setInitialDirectory(fc);
        File file = fc.showOpenDialog(mainApp.getPrimaryStage());
        if (file == null) return;
        try {
            mainApp.loadPersonDataFromJson(file);
            mainApp.setPersonFilePath(file);
            mainApp.setDirty(false);
        } catch (IOException e) {
            showError("Could not load data",
                    "Could not load data from file:\n" + file.getPath(), e);
        }
    }

    @FXML
    private void handleSave() {
        saveOrSaveAs(); // ✅ si no hay fichero -> Save As
    }

    @FXML
    private void handleSaveAs() {
        saveAs();
    }

    @FXML
    private void handleExit() {
        if (!confirmSaveIfDirty("Exit", "Exit application?")) {
            return; // cancelado
        }
        // Si confirmSaveIfDirty devolvió true, o se guardó, o eligió no guardar

        mainApp.getPrimaryStage().close();
    }

// -------------------- SAVE LOGIC --------------------
    /**
     * Guarda en el fichero actual. Si no existe (no se ha guardado nunca),
     * abre Save As... Devuelve true si se guardó o no hacía falta guardar.
     */
    private boolean saveOrSaveAs() {
        File file = mainApp.getPersonFilePath();
        if (file == null) {
            return saveAs();
        }
        try {
            mainApp.savePersonDataToJson(file);
            mainApp.setPersonFilePath(file);
            mainApp.setDirty(false);
            return true;
        } catch (IOException e) {
            showError("Could not save data",
                    "Could not save data to file:\n" + file.getPath(), e);
            return false;
        }
    }

    /**
     * Save As... Devuelve false si el usuario cancela.
     */
    private boolean saveAs() {
        FileChooser fc = createJsonFileChooser("Save Address Book (JSON)");
        setInitialDirectory(fc);
        File file = fc.showSaveDialog(mainApp.getPrimaryStage());
        if (file == null) return false;
        // Asegurar extensión .json
        if (!file.getPath().toLowerCase().endsWith(".json")) {
            file = new File(file.getPath() + ".json");
        }
        try {
            mainApp.savePersonDataToJson(file);
            mainApp.setPersonFilePath(file);
            mainApp.setDirty(false);
            return true;
        } catch (IOException e) {
            showError("Could not save data",
                    "Could not save data to file:\n" + file.getPath(), e);
            return false;
        }
    }

    // -------------------- DIRTY CONFIRMATION --------------------
    /**
     * Si hay cambios sin guardar (dirty), pregunta:
     * - Save (si no hay fichero -> Save As)
     * - Don't Save
     * - Cancel
     *
     * Devuelve true si se puede continuar con la acción solicitada.
     */
    private boolean confirmSaveIfDirty(String title, String header) {
        if (!mainApp.isDirty()) {
            return true;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText("You have unsaved changes. What do you want to do?");
        ButtonType save = new ButtonType("Save", ButtonBar.ButtonData.YES);
        ButtonType dontSave = new ButtonType("Don't Save", ButtonBar.ButtonData.NO);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(save, dontSave, cancel);
        ButtonType result = alert.showAndWait().orElse(cancel);
        if (result == save) {
            return saveOrSaveAs(); // ✅ aquí ocurre tu requisito “si no hay fichero -> Save As”
        }
        if (result == dontSave) {
            return true;
        }
        return false; // cancel
    }

    // -------------------- HELPERS --------------------
    private FileChooser createJsonFileChooser(String title) {
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*")
        );
        return fc;
    }
    private void setInitialDirectory(FileChooser fc) {
        File current = mainApp.getPersonFilePath();
        if (current != null && current.getParentFile() != null && current.getParentFile().exists()) {
            fc.setInitialDirectory(current.getParentFile());
        } else {
            File home = new File(System.getProperty("user.home"));
            if (home.exists()) fc.setInitialDirectory(home);
        }
    }
    private void showError(String header, String content, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content + "\n\n" + e.getClass().getSimpleName() + ": " + e.getMessage());
        alert.showAndWait();
    }
    //-------CSV-------


    // Método para crear/configurar el menú (llámalo en tu initialize o método main)
    public void setupMenuHandlers() {
        // Si usas FXML, vincula estos métodos a los MenuItem con onAction="#handleExport"
        // Si lo haces por código:
        // exportItem.setOnAction(event -> handleExport());
        // importItem.setOnAction(event -> handleImport());
    }
    @FXML
    public void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar personas a CSV");

        // Filtro para ver solo CSV
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Mostrar diálogo de guardar sobre la ventana principal
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage()); // Ajusta 'mainApp' a tu referencia

        if (file != null) {
            // Asegurar extensión .csv
            if (!file.getPath().endsWith(".csv")) {
                file = new File(file.getPath() + ".csv");
            }

            try {
                // Pasamos la lista de personas (ObservableList)
                csvRepository.exportData(file, mainApp.getPersonData());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText(null);
                alert.setContentText("Datos exportados correctamente a Excel/CSV.");
                alert.showAndWait();

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No se pudo guardar el archivo:\n" + e.getMessage());
                alert.showAndWait();
            }
        }
    }
    @FXML
    public void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar personas desde CSV");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            try {
                List<Person> imported = csvRepository.importData(file);

                // Lógica "New": Borrar todo y añadir lo nuevo
                mainApp.getPersonData().clear();
                mainApp.getPersonData().addAll(imported);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setContentText("Se han importado " + imported.size() + " personas.");
                alert.showAndWait();

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No se pudo leer el archivo:\n" + e.getMessage());
                alert.showAndWait();
            }
        }
    }
    /**
     * Opens the birthday statistics.
     */
    @FXML
    private void handleShowBirthdayStatistics() {
        mainApp.showBirthdayStatistics();
    }
    @FXML
    private void handleShowBirthdayStatisticsLine() {
        mainApp.showBirthdayStatisticsLine();
    }
    @FXML
    private void handleShowBirthdayStatisticsPie() {
        mainApp.showBirthdayStatisticsPie();
    }

    @FXML
    private void handleShowDonutStatistics() {
        mainApp.showDonutStatistics();
    }

}
