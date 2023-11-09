package Model;

import Util.BackUp;

import java.io.Serializable;
import java.time.Duration;
import java.util.LinkedList;
import java.util.Objects;

public class StudentHistory implements Serializable {
    private LinkedList<StudentLoggedIn> studentHistory;

    public StudentHistory() {
        this(new LinkedList<>());
    }

    public StudentHistory(LinkedList<StudentLoggedIn> studentHistory) {
        this.studentHistory = studentHistory;
    }

    public void addStudentToHistory(StudentLoggedIn studentLoggedIn) {
        studentHistory.add(studentLoggedIn);
        BackUp.saveData();
    }

    public LinkedList<StudentLoggedIn> getStudentHistory() {
        return studentHistory;
    }

    public void setStudentHistory(LinkedList<StudentLoggedIn> studentHistory) {
        this.studentHistory = studentHistory;
    }

    public String calculateTotalDurationAsString() {
        Duration totalDuration = Duration.ZERO;

        for (StudentLoggedIn studentLoggedIn : studentHistory) {
            if (studentLoggedIn.getTimeIn() != null && studentLoggedIn.getTimeOut() != null) {
                totalDuration = totalDuration.plus(Duration.between(studentLoggedIn.getTimeIn(), studentLoggedIn.getTimeOut()));
            }
        }
        long hours = totalDuration.toHours();
        long minutes = totalDuration.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }

    public String calculateTotalDurationAsString(LinkedList<StudentLoggedIn> studentList) {
        Duration totalDuration = Duration.ZERO;

        for (StudentLoggedIn studentLoggedIn : studentList) {
            if (studentLoggedIn.getTimeIn() != null && studentLoggedIn.getTimeOut() != null) {
                totalDuration = totalDuration.plus(Duration.between(studentLoggedIn.getTimeIn(), studentLoggedIn.getTimeOut()));
            }
        }
        long hours = totalDuration.toHours();
        long minutes = totalDuration.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }

    public void display() {
        for (StudentLoggedIn studentLoggedIn : studentHistory) {
            System.out.println(studentLoggedIn);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentHistory that = (StudentHistory) o;
        return Objects.equals(studentHistory, that.studentHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentHistory);
    }
}
