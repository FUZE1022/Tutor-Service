package Util;

import Model.DataCenter;
import Model.StudentHistory;
import Model.StudentLinkedList;

import java.nio.file.Files;
import java.nio.file.Paths;

public final class BackUp {
    private static final String BACKUP_STUDENT_FILE_PATH = "BackUp/students.dat";
    private static final String BACKUP_STUDENTHISTORY_FILE_PATH = "BackUp/studentHistory.dat";
    private BackUp() {}
    private static boolean seralizedFileExists() {
        return Files.exists(Paths.get(BACKUP_STUDENT_FILE_PATH)) && Files.exists(Paths.get(BACKUP_STUDENTHISTORY_FILE_PATH));
    }

    public static void loadData(String filePath) {
        if(!seralizedFileExists()) {
            StudentLinkedList studentLinkedList = CsvParser.readCsvFile(filePath);
            DataCenter.getInstance().setStudentLinkedList(studentLinkedList);
            saveData();
            return;
        }
        try  {
            StudentLinkedList loadStudentLinkedList = (StudentLinkedList) Serializer.deserializeFromFile(BACKUP_STUDENT_FILE_PATH);
            StudentHistory loadStudentHistory = (StudentHistory) Serializer.deserializeFromFile(BACKUP_STUDENTHISTORY_FILE_PATH);
            DataCenter.getInstance().setStudentLinkedList(loadStudentLinkedList);
            DataCenter.getInstance().setStudentHistory(loadStudentHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveData() {
        try{
            Serializer.serializeToFile(DataCenter.getInstance().getStudentLinkedList(), BACKUP_STUDENT_FILE_PATH);
            Serializer.serializeToFile(DataCenter.getInstance().getStudentLoggedInHistoryLinkedList(), BACKUP_STUDENTHISTORY_FILE_PATH);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
