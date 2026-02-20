package es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.view;

import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.model.Person;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.chart.ChartData;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class DonutCharStatisticsController {

    @FXML
    private Tile donutTile;

    private ObservableList<Person> personData;

    @FXML
    private void initialize() {
        if (donutTile != null) {
            donutTile.setSkinType(Tile.SkinType.DONUT_CHART);
            donutTile.setTitle("Generations");
            donutTile.setUnit("People");
            donutTile.setTextVisible(true);
            donutTile.setBackgroundColor(Color.TRANSPARENT);
            // Ajusta colores de texto para tu tema oscuro
            donutTile.setTitleColor(Color.WHITE);
            donutTile.setTextColor(Color.WHITE);
            donutTile.setUnitColor(Color.LIGHTGRAY);
            donutTile.setValueColor(Color.WHITE);
        }
    }

    public void setPersonData(ObservableList<Person> persons) {
        this.personData = persons;
        updateChart();

        // Listener de estructura
        personData.addListener((ListChangeListener<Person>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Person p : change.getAddedSubList()) {
                        p.birthdayProperty().addListener((obs, oldVal, newVal) -> updateChart());
                    }
                }
            }
            updateChart();
        });

        // Listener de fechas
        for (Person p : personData) {
            p.birthdayProperty().addListener((obs, oldVal, newVal) -> updateChart());
        }
    }

    private void updateChart() {
        int boomers = 0;
        int genX = 0;
        int millennials = 0;
        int genZ = 0;
        int alpha = 0;

        for (Person p : this.personData) {
            if (p.getBirthday() == null) continue;
            int year = p.getBirthday().getYear();

            if (year <= 1964) boomers++;
            else if (year <= 1980) genX++;
            else if (year <= 1996) millennials++;
            else if (year <= 2012) genZ++;
            else alpha++;
        }

        List<ChartData> tilesData = new ArrayList<>();
        if (boomers > 0)     tilesData.add(new ChartData("Baby Boomers", boomers, Color.web("#d5ac81")));
        if (genX > 0)        tilesData.add(new ChartData("Gen X", genX, Color.web("#5d98bc")));
        if (millennials > 0) tilesData.add(new ChartData("Millennials", millennials, Color.web("#bc5d5d")));
        if (genZ > 0)        tilesData.add(new ChartData("Gen Z", genZ, Color.web("#7cbc5d")));
        if (alpha > 0)       tilesData.add(new ChartData("Gen Alpha", alpha, Color.web("#9d5dbc")));

        donutTile.setChartData(tilesData);
    }
}