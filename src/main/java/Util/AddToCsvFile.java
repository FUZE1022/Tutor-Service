package Util;

import Model.DataCenter;
import Model.Student;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public final class AddToCsvFile {
    private AddToCsvFile() {}

    public static void addStudentToCsvFile(String filePath, String id, String firstName, String lastName,
                                           String course) throws IOException, RuntimeException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = id + "," + lastName + " " + firstName + "," + course;
            writer.write(line);
            writer.newLine();
        }
        DataCenter.getInstance().getStudentLinkedList().addStudent(new Student(id, (lastName + " " + firstName),
                course));
        BackUp.saveData();
    }
}
