package es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.view;

import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

/**
 * The type Pie char statistics controller.
 */
public class PieCharStatisticsController {

    @FXML
    private PieChart pieChart;

    // Guardamos la lista como ObservableList para poder escuchar sus cambios
    private ObservableList<Person> personData;

    /**
     * Establece la lista de personas y configura los listeners para que el gráfico
     * se actualice automáticamente cuando los datos cambien.
     *
     * @param persons La lista observable de personas.
     */
    public void setPersonData(ObservableList<Person> persons) {
        this.personData = persons;

        // 1. Carga inicial de datos
        updatePieChartData();

        // 2. Listener para detectar cambios EN LA ESTRUCTURA de la lista (Add/Remove)
        personData.addListener((ListChangeListener<Person>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    // Si se añaden personas nuevas, hay que escuchar también sus cambios de fecha
                    for (Person p : change.getAddedSubList()) {
                        p.birthdayProperty().addListener((obs, oldVal, newVal) -> updatePieChartData());
                    }
                }
                // Si se borran, no es estrictamente necesario quitar el listener individual
                // porque el objeto se irá al Garbage Collector, pero el gráfico debe repintarse.
            }
            // Actualizar el gráfico tras cualquier cambio de estructura (añadir/borrar)
            updatePieChartData();
        });

        // 3. Listener para detectar cambios EN LAS FECHAS de las personas que ya existen
        for (Person p : personData) {
            p.birthdayProperty().addListener((obs, oldVal, newVal) -> updatePieChartData());
        }
    }

    /**
     * Recalcula las generaciones y actualiza el gráfico.
     */
    private void updatePieChartData() {
        // Limpiamos los datos anteriores del gráfico
        pieChart.getData().clear();

        int boomers = 0;
        int genX = 0;
        int millennials = 0;
        int genZ = 0;
        int alpha = 0;

        // Usamos la lista guardada en la clase (this.personData)
        for (Person p : this.personData) {
            if (p.getBirthday() == null) continue; // Evitar NullPointerException si no hay fecha

            int year = p.getBirthday().getYear();

            if (year <= 1964) {
                boomers++;
            } else if (year <= 1980) {
                genX++;
            } else if (year <= 1996) {
                millennials++;
            } else if (year <= 2012) {
                genZ++;
            } else {
                alpha++;
            }
        }

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        if (boomers > 0)     pieData.add(new PieChart.Data("Baby Boomers", boomers));
        if (genX > 0)        pieData.add(new PieChart.Data("Gen X", genX));
        if (millennials > 0) pieData.add(new PieChart.Data("Millennials", millennials));
        if (genZ > 0)        pieData.add(new PieChart.Data("Gen Z", genZ));
        if (alpha > 0)       pieData.add(new PieChart.Data("Gen Alpha", alpha));

        pieChart.setData(pieData);
        pieChart.setTitle("Distribución por Generaciones");
    }
}