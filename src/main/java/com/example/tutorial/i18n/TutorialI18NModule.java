package com.example.tutorial.i18n;

import uk.q3c.krail.core.user.opt.InMemory;
import uk.q3c.krail.i18n.ClassPatternSource;
import uk.q3c.krail.i18n.I18NModule;

import java.util.Locale;

public class TutorialI18NModule extends I18NModule {

    @Override
    protected void define() {
        defaultLocale(Locale.UK);
        supportedLocales(Locale.GERMANY);
        source(InMemory.class);
        source(ClassPatternSource.class);
    }

}