module Client {
    requires Shared;
    requires java.sql;
    requires spring.core;
    requires spring.web;
    requires spring.webflux;
    requires javafx.controls;
    requires reactor.core;
    requires transitive javafx.graphics;
    // Required for easier objects transfer
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens de.tum.in.ase.eist.view to javafx.graphics;
    opens de.tum.in.ase.eist to javafx.graphics;
}
