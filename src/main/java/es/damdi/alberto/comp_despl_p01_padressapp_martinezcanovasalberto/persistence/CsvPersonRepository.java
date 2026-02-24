package es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.persistence;

import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.model.Person;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvPersonRepository {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    private static final String DELIMITER = ";";

    public void exportData(File file, List<Person> personList) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {


            writer.write("FirstName;LastName;Street;PostalCode;City;Birthday");
            writer.newLine();

            for (Person person : personList) {

                String dateStr = (person.getBirthday() != null)
                        ? DATE_FORMATTER.format(person.getBirthday())
                        : "";


                String line = String.join(DELIMITER,
                        person.getFirstName(),
                        person.getLastName(),
                        person.getStreet(),
                        String.valueOf(person.getPostalCode()), // Convertir int a String
                        person.getCity(),
                        dateStr
                );

                writer.write(line);
                writer.newLine();
            }
        }
    }

    public List<Person> importData(File file) throws IOException {
        List<Person> importedPersons = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(DELIMITER, -1);


                if (data.length >= 6) {
                    Person person = new Person();


                    person.setFirstName(data[0]);
                    person.setLastName(data[1]);
                    person.setStreet(data[2]);


                    try {
                        person.setPostalCode(Integer.parseInt(data[3]));
                    } catch (NumberFormatException e) {
                        person.setPostalCode(0);
                    }

                    person.setCity(data[4]);


                    if (!data[5].isEmpty()) {
                        try {
                            LocalDate date = LocalDate.parse(data[5], DATE_FORMATTER);
                            person.setBirthday(date);
                        } catch (Exception e) {
                            person.setBirthday(null);
                        }
                    }

                    importedPersons.add(person);
                }
            }
        }
        return importedPersons;
    }
}