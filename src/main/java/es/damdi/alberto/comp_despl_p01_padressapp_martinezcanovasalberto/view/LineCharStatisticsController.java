package es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.view;


import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class LineCharStatisticsController {


    // --- Componentes del Gráfico de LÍNEAS ---
    @FXML
    private LineChart<String, Integer> lineChart;
    @FXML
    private CategoryAxis xAxisLine;

    private ObservableList<String> monthNames = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Obtener nombres de meses en inglés
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        monthNames.addAll(Arrays.asList(months));

        // Asignar categorías a AMBOS ejes X
        xAxisLine.setCategories(monthNames);
    }

    public void setPersonData(List<Person> persons) {
        // 1. Contar nacimientos por mes
        int[] monthCounter = new int[12];
        for (Person p : persons) {
            int month = p.getBirthday().getMonthValue() - 1;
            monthCounter[month]++;
        }

        // 2. Crear la serie para el gráfico de BARRAS
        XYChart.Series<String, Integer> seriesBar = new XYChart.Series<>();
        seriesBar.setName("Barras");

        // 3. Crear la serie para el gráfico de LÍNEAS
        XYChart.Series<String, Integer> seriesLine = new XYChart.Series<>();
        seriesLine.setName("Líneas");

        // 4. Rellenar ambas series con los datos
        for (int i = 0; i < monthCounter.length; i++) {
            String monthName = monthNames.get(i);
            int count = monthCounter[i];

            // Añadir dato a la serie de barras
            seriesBar.getData().add(new XYChart.Data<>(monthName, count));

            // Añadir dato a la serie de líneas
            seriesLine.getData().add(new XYChart.Data<>(monthName, count));
        }

        lineChart.getData().clear();
        lineChart.getData().add(seriesLine);
    }
}