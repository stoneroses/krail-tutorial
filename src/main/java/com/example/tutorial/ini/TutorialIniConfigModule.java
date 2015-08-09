package com.example.tutorial.ini;

import uk.q3c.krail.core.config.ApplicationConfigurationModule;

public class TutorialIniConfigModule extends ApplicationConfigurationModule {
    @Override
    protected void bindConfigs() {
        super.bindConfigs();
        addConfig("moreConfig.ini", 1, false);
        addConfig("essential.ini", 2, true);
    }
}