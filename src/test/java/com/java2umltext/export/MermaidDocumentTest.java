package com.java2umltext.export;

import com.java2umltext.model.ClassWrapper;
import com.java2umltext.model.FieldWrapper;
import com.java2umltext.model.MethodWrapper;
import com.java2umltext.model.Relationship;
import com.java2umltext.model.Visibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MermaidDocumentTest {

    private MermaidDocument document;

    @BeforeEach
    void setUp() {
        document = new MermaidDocument();
    }

    @Test
    void testHeader() {
        String export = document.export();
        assertTrue(export.startsWith("classDiagram"));
    }

    @Test
    void testAddClass() {
        ClassWrapper cw = document.addClass("", "class", "TestClass");
        assertNotNull(cw);
        assertEquals("TestClass", cw.name());
    }

    @Test
    void testExportSimpleClass() {
        ClassWrapper cw = document.addClass("", "class", "Person");
        cw.fields().add(new FieldWrapper(Visibility.PRIVATE, false, "String", "name"));
        cw.methods().add(new MethodWrapper(Visibility.PUBLIC, false, false, "String", "getName"));

        String export = document.export();
        assertTrue(export.contains("class Person {"));
        assertTrue(export.contains("-name: String"));
        assertTrue(export.contains("+getName() String"));
    }

    @Test
    void testExportInterface() {
        ClassWrapper cw = document.addClass("", "interface", "Drawable");

        String export = document.export();
        assertTrue(export.contains("class Drawable {"));
        assertTrue(export.contains("<<interface>>"));
    }

    @Test
    void testExportAbstractClass() {
        ClassWrapper cw = document.addClass("", "abstract class", "BaseClass");

        String export = document.export();
        assertTrue(export.contains("class BaseClass {"));
        assertTrue(export.contains("<<abstract>>"));
    }

    @Test
    void testExportEnum() {
        ClassWrapper cw = document.addClass("", "enum", "Color");

        String export = document.export();
        assertTrue(export.contains("class Color {"));
        assertTrue(export.contains("<<enum>>"));
    }

    @Test
    void testExportRecord() {
        ClassWrapper cw = document.addClass("", "record", "Point");

        String export = document.export();
        assertTrue(export.contains("class Point {"));
        assertTrue(export.contains("<<record>>"));
    }

    @Test
    void testPackageNameReplacedWithUnderscore() {
        ClassWrapper cw = document.addClass("com.example.test", "class", "MyClass");

        String export = document.export();
        assertTrue(export.contains("class com_example_test_MyClass {"));
    }

    @Test
    void testStaticFieldMarker() {
        ClassWrapper cw = document.addClass("", "class", "Counter");
        cw.fields().add(new FieldWrapper(Visibility.PUBLIC, true, "int", "count"));

        String export = document.export();
        assertTrue(export.contains("+count: int$"));
    }

    @Test
    void testStaticMethodMarker() {
        ClassWrapper cw = document.addClass("", "class", "Utils");
        cw.methods().add(new MethodWrapper(Visibility.PUBLIC, true, false, "void", "helper"));

        String export = document.export();
        assertTrue(export.contains("+helper() void$"));
    }

    @Test
    void testAbstractMethodMarker() {
        ClassWrapper cw = document.addClass("", "abstract class", "BaseClass");
        cw.methods().add(new MethodWrapper(Visibility.PUBLIC, false, true, "void", "abstractMethod"));

        String export = document.export();
        assertTrue(export.contains("+abstractMethod() void*"));
    }

    @Test
    void testGenericTypeEscaping() {
        ClassWrapper cw = document.addClass("", "class", "Container");
        cw.fields().add(new FieldWrapper(Visibility.PRIVATE, false, "List<String>", "items"));

        String export = document.export();
        assertTrue(export.contains("List~String~"));
    }
}
