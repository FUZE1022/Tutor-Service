package Model;

import Util.BackUp;

import java.io.Serializable;

public class DataCenter implements Serializable {
    private static DataCenter instance = null;
    private StudentLinkedList studentLinkedList;
    private StudentLoggedInQueue studentLoggedInQueue;
    private StudentHistory studentHistory;
    private Student currentStudent;

    private DataCenter() {
        studentLinkedList = new StudentLinkedList();
        studentLoggedInQueue = new StudentLoggedInQueue();
        studentHistory = new StudentHistory();
    }

    public static DataCenter getInstance() {
        if (instance == null) {
            instance = new DataCenter();
        }
        return instance;
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }
    public void setCurrentStudent(Student student) {
        currentStudent = student;
        BackUp.saveData();
    }

    public StudentLinkedList getStudentLinkedList() {
        return studentLinkedList;
    }

    public void setStudentLinkedList(StudentLinkedList studentLinkedList) {
        this.studentLinkedList.setStudentLinkedList(studentLinkedList.getStudentLinkedList());
        BackUp.saveData();
    }

    public StudentLoggedInQueue getStudentLoggedInQueue() {
        return studentLoggedInQueue;
    }

    public void setStudentLoggedInQueue(StudentLoggedInQueue studentLoggedInQueue) {
        this.studentLoggedInQueue = studentLoggedInQueue;
        BackUp.saveData();
    }

    public StudentHistory getStudentLoggedInHistoryLinkedList() {
        return studentHistory;
    }

    public void setStudentHistory(StudentHistory studentHistory) {
        this.studentHistory.setStudentHistory(studentHistory.getStudentHistory());
        BackUp.saveData();
    }
}
