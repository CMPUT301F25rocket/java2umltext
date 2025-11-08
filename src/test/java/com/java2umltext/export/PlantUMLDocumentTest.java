package com.java2umltext.export;

import com.java2umltext.model.ClassWrapper;
import com.java2umltext.model.FieldWrapper;
import com.java2umltext.model.MethodWrapper;
import com.java2umltext.model.Relationship;
import com.java2umltext.model.Visibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantUMLDocumentTest {

    private PlantUMLDocument document;

    @BeforeEach
    void setUp() {
        document = new PlantUMLDocument();
    }

    @Test
    void testHeaderAndFooter() {
        String export = document.export();
        assertTrue(export.startsWith("@startuml"));
        assertTrue(export.endsWith("@enduml"));
    }

    @Test
    void testAddClass() {
        ClassWrapper cw = document.addClass("", "class", "TestClass");
        assertNotNull(cw);
        assertEquals("TestClass", cw.name());
        assertEquals("class", cw.type());
    }

    @Test
    void testExportSimpleClass() {
        ClassWrapper cw = document.addClass("", "class", "Person");
        cw.fields().add(new FieldWrapper(Visibility.PRIVATE, false, "String", "name"));
        cw.methods().add(new MethodWrapper(Visibility.PUBLIC, false, false, "String", "getName"));

        String export = document.export();
        assertTrue(export.contains("class Person {"));
        assertTrue(export.contains("- String name"));
        assertTrue(export.contains("+ String getName()"));
    }

    @Test
    void testExportInterface() {
        ClassWrapper cw = document.addClass("", "interface", "Drawable");
        cw.methods().add(new MethodWrapper(Visibility.PUBLIC, false, true, "void", "draw"));

        String export = document.export();
        assertTrue(export.contains("interface Drawable {"));
        assertTrue(export.contains("+ {abstract} void draw()"));
    }

    @Test
    void testExportWithPackage() {
        ClassWrapper cw = document.addClass("com.example", "class", "MyClass");

        String export = document.export();
        assertTrue(export.contains("class com.example.MyClass {"));
    }

    @Test
    void testAddRelationship() {
        document.addClass("", "class", "Parent");
        document.addClass("", "class", "Child");
        document.addRelationship(new Relationship("<|--", "Parent", "Child"));

        String export = document.export();
        assertTrue(export.contains("Parent <|-- Child"));
    }

    @Test
    void testRemoveForeignRelations() {
        document.addClass("", "class", "ExistingClass");
        document.addRelationship(new Relationship("--", "ExistingClass", "NonExistentClass"));
        document.addRelationship(new Relationship("--", "AnotherNonExistent", "ExistingClass"));

        document.removeForeignRelations();

        String export = document.export();
        assertFalse(export.contains("NonExistentClass"));
        assertFalse(export.contains("AnotherNonExistent"));
    }

    @Test
    void testStaticField() {
        ClassWrapper cw = document.addClass("", "class", "Counter");
        cw.fields().add(new FieldWrapper(Visibility.PUBLIC, true, "int", "count"));

        String export = document.export();
        assertTrue(export.contains("+ {static} int count"));
    }

    @Test
    void testStaticMethod() {
        ClassWrapper cw = document.addClass("", "class", "Utils");
        cw.methods().add(new MethodWrapper(Visibility.PUBLIC, true, false, "void", "helper"));

        String export = document.export();
        assertTrue(export.contains("+ {static} void helper()"));
    }
}
