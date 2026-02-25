package es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.model.Person;
import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.persistence.JacksonPersonRepository;
import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.persistence.PersonRepository;
import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.settings.AppPreferences;
import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.view.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * El repositorio (JSON con Jackson)
     */
    private final PersonRepository repository = new JacksonPersonRepository();

    /**
     * El fichero actual asociado (si existe)
     */
    private File personFilePath;

    /**
     * El estado de cambios sin guardar (dirty)
     */
    private boolean dirty;
    public PersonRepository getRepository() {
        return repository;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public File getPersonFilePath() {
        return personFilePath;
    }

    public void setPersonData(ObservableList<Person> personData) {
        this.personData = personData;
    }

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
        personData.add(new Person("Alberto", "Martínez"));

    }

    /**
     * Returns the data as an observable list of Persons.
     * @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp - Alberto Martínez Cánovas");

        initRootLayout();

        showPersonOverview();
        // Dirty flag cambios en la lista
        personData.addListener((javafx.collections.ListChangeListener<Person>) c -> setDirty(true));
        loadOnStartup();

    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            this.primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param person the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog(Person person) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));
            Scene scene = new Scene(page);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Conecta el fichero actual con preferencias
     */
    public void setPersonFilePath(File file) {
        this.personFilePath = file;
        AppPreferences.setPersonFile(file == null ? null : file.getAbsolutePath());
        // opcional: reflejar en el título
        if (primaryStage != null) {
            String name = (file == null) ? "AdressApp AMC" : "AdressApp AMC - " + file.getName();
            primaryStage.setTitle(name);
        }
    }

    /**
     * Implementa loadPersonDataFromJson(File file)
     */
    public void loadPersonDataFromJson(File file) throws IOException {
        // 1) Cargar desde repositorio
        List<Person> loaded = repository.load(file);
        // 2) IMPORTANTE: NO reasignar personData. Usar setAll.
        // Así la TableView sigue enlazada a la misma lista.
        personData.setAll(loaded);
        // 3) Guardar el fichero actual (y en preferencias)
        setPersonFilePath(file);
        // 4) Acabamos de cargar: no hay cambios sin guardar
        setDirty(false);
    }

    /**
     * Implementa savePersonDataToJson(File file)
     */
    public void savePersonDataToJson(File file) throws IOException {
        // 1) Guardar con el repositorio
        repository.save(file, new ArrayList<>(personData));
        // 2) Marcar fichero actual (y en preferencias)
        setPersonFilePath(file);
        // 3) Tras guardar, ya no hay cambios pendientes
        setDirty(false);
    }

    /**
     * Cargar el último fichero al arrancar (con preferencias)
     */

    private void loadOnStartup() {
        // 1) si hay ruta en Preferences -> carga
        AppPreferences.getPersonFile().ifPresentOrElse(
                path -> {
                    File f = new File(path);
                    if (f.exists()) {
                        try {
                            loadPersonDataFromJson(f);
                            setPersonFilePath(f);
                        } catch (IOException e) {
                            // si falla, cae al default
                            loadDefaultIfExists();
                        }
                    } else {
                        loadDefaultIfExists();
                    }
                },
                this::loadDefaultIfExists
        );
    }
    private void loadDefaultIfExists() {
        File f = defaultJsonPath.toFile();
        if (f.exists()) {
            try {
                loadPersonDataFromJson(f);
                setPersonFilePath(f);
            } catch (IOException ignored) {
                // si falla, te quedas con los datos en memoria (ej. sample data)
            }
        } else {
            // No existe aún: te quedas con los sample data (o lista vacía, como prefieras)
            setPersonFilePath(f); // así autosave crea el fichero al salir
        }
    }
    private final Path defaultJsonPath = Paths.get(System.getProperty("user.home"), ".addressappv2", "persons.json");

    /**
     * Opens a dialog to show birthday statistics.
     */
    public void showBirthdayStatistics() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(personData);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showBirthdayStatisticsLine() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LineCharStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            LineCharStatisticsController controller = loader.getController();
            controller.setPersonData(personData);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showBirthdayStatisticsPie() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PieCharStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Generational Statistics");
//            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            PieCharStatisticsController controller = loader.getController();
            controller.setPersonData(personData);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDonutStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/DonutCharStatistics.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Donut Statistics (TilesFX)");
            dialogStage.initOwner(primaryStage);
            dialogStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/images/icono.png")));

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            DonutCharStatisticsController controller = loader.getController();
            controller.setPersonData(personData);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
