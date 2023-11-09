package Util;

import Model.DataCenter;
import Model.Student;
import Model.StudentLoggedIn;
import Model.StudentLoggedInQueue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public final class Export {

    private Export() {}
    public static void exportCSV(String filePath) throws IOException {
        try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(filePath))) {
            csvWriter.write("ID,Name,Courses,Instructor,Topic,Time In,Time Out,Date,Status");
            csvWriter.newLine();

            for (StudentLoggedIn student : DataCenter.getInstance().getStudentLoggedInHistoryLinkedList().getStudentHistory()) {
                csvWriter.write(student.getId() + "," + student.getName() + "," + student.getCourses() + ","
                        + student.getInstructor() + "," + student.getTopic() + "," + student.getTimeIn() + ","
                        + (student.getTimeOut() != null ? student.getTimeOut() : "N/A") + ","
                        + student.getDate() + "," + (student.getLoggedIn() ? "In" : "Out"));
                csvWriter.newLine();
            }
        }
    }
}
