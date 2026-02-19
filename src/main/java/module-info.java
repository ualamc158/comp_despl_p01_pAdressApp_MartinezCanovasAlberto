module es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    requires org.kordamp.bootstrapfx.core;
    requires java.prefs;
    requires java.desktop;

    opens es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.model;
    opens es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.persistence;
    opens es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.view;
    opens es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.util;

    opens es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto to javafx.fxml;
    exports es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto;
}