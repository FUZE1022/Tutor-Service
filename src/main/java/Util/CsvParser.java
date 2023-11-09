package Util;

import Model.Student;
import Model.StudentLinkedList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class CsvParser {
    private CsvParser() {}
    public static StudentLinkedList readCsvFile(String csvFilePath) {
        StudentLinkedList students = new StudentLinkedList();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0], name = data[1], course = data[2];
                Student student = new Student(id, name, course);
                students.addStudent(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return students;
    }
}
