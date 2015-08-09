package com.example.tutorial.jpa;

import org.apache.commons.io.FileUtils;
import uk.q3c.krail.jpa.persist.DefaultJpaInstanceConfiguration;
import uk.q3c.krail.jpa.persist.JpaDb;
import uk.q3c.krail.jpa.persist.JpaModule;

import java.io.File;
import java.io.IOException;

public class TutorialJpaModule extends JpaModule {

    File userHome = new File(System.getProperty("user.home"));
    File tempDir = new File(userHome, "temp/krail-tutorial");

    public TutorialJpaModule() {

        try {
            FileUtils.forceMkdir(tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void define() {
        addPersistenceUnit("derbyDb", DerbyJpa.class, derbyConfig());
        addPersistenceUnit("hsqlDb", HsqlJpa.class, hsqlConfig());
    }

    private DefaultJpaInstanceConfiguration hsqlConfig() {
        DefaultJpaInstanceConfiguration config = new DefaultJpaInstanceConfiguration();
        config.db(JpaDb.HSQLDB)
              .autoCreate(true)
              .url("mem:test")
              .user("sa")
              .useLongIntDao()
              .password("")
              .ddlGeneration(DefaultJpaInstanceConfiguration.Ddl.DROP_AND_CREATE);
        return config;

    }

    private DefaultJpaInstanceConfiguration derbyConfig() {
        DefaultJpaInstanceConfiguration config = new DefaultJpaInstanceConfiguration();
        File dbFolder = new File(tempDir, "derbyDb");

        config.transactionType(DefaultJpaInstanceConfiguration.TransactionType.RESOURCE_LOCAL)
              .db(JpaDb.DERBY_EMBEDDED)
              .autoCreate(true)
              .url(dbFolder.getAbsolutePath())
              .user("test")
              .useLongIntDao()
              .provideOptionDao()
              .providePatternDao()
              .password("test")
              .ddlGeneration(DefaultJpaInstanceConfiguration.Ddl.DROP_AND_CREATE);
        return config;
    }
}