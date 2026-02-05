module es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.model;
//    opens es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.persistence;
    opens es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.view;
    opens es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto.util;

    opens es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto to javafx.fxml;
    exports es.damdi.alberto.comp_despl_p01_padressapp_martinezcanovasalberto;
}