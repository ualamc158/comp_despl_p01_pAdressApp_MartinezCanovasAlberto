package es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.persistence;



import es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.model.Person;

import java.io.File;
import java.io.IOException;
import java.util.List;
public interface PersonRepository {
    List<Person> load(File file) throws IOException;
    void save(File file, List<Person> persons) throws IOException;
}

