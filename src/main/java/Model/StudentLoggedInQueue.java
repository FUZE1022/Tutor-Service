package Model;

import javafx.scene.control.Alert;

import java.io.Serializable;
import java.util.*;

public class StudentLoggedInQueue implements Serializable {
    private LinkedList<StudentLoggedIn> studentQueue;

    public StudentLoggedInQueue() {
        this(new LinkedList<>());
    }

    public StudentLoggedInQueue(LinkedList<StudentLoggedIn> studentQueue) {
        this.studentQueue = studentQueue;
    }

    public void addStudentToQueue(StudentLoggedIn studentLoggedIn, Student student) {
        if (!containsStudent(student)) {
            studentQueue.add(studentLoggedIn);
        }
    }

    public boolean containsStudent(Student student) {
    	for(StudentLoggedIn studentLoggedIn : studentQueue) {
    		if(studentLoggedIn.getStudent().equals(student)) {
    			return true;
    		}
    	}
        return false;
    }

    public void logOutStudent(StudentLoggedIn studentLoggedIn) {
    	studentQueue.remove(studentLoggedIn);
    }

    public void logOutAllStudents(StudentHistory studentHistory) {
        List<StudentLoggedIn> studentsToLogOut = new ArrayList<>(studentQueue);
        for (StudentLoggedIn studentLoggedIn : studentsToLogOut) {
            studentLoggedIn.endTime();
            studentQueue.remove(studentLoggedIn);
            studentHistory.addStudentToHistory(new StudentLoggedIn(studentLoggedIn.getStudent(),
                    studentLoggedIn.getCourses(), studentLoggedIn.getTopic(), studentLoggedIn.getInstructor(),
                    false, studentLoggedIn.getDate(), studentLoggedIn.getTimeIn(), studentLoggedIn.getTimeOut()));
        }
    }

    public Optional<StudentLoggedIn> getStudentLoggedIn(Student student) {
    	for(StudentLoggedIn studentLoggedIn : studentQueue) {
    		if(studentLoggedIn.getStudent().equals(student)) {
    			return Optional.of(studentLoggedIn);
    		}
    	}
    	return Optional.empty();
    }

    public Optional<StudentLoggedIn> getStudentLoggedIn(String name) {
        for(StudentLoggedIn studentLoggedIn : studentQueue) {
            if(studentLoggedIn.getStudent().getName().equals(name)) {
                return Optional.of(studentLoggedIn);
            }
        }
        return Optional.empty();
    }

    public LinkedList<StudentLoggedIn> getStudentQueue() {
        return studentQueue;
    }

    public boolean isEmpty() {
        return studentQueue.isEmpty();
    }

    public int size() {
        return studentQueue.size();
    }

    public void display() {
    	for (StudentLoggedIn studentLoggedIn : studentQueue) {
    		System.out.println(studentLoggedIn);
    	}
    }

    @Override
    public String toString() {
        return "StudentLoggedInQueue{" +
                "studentQueue=" + studentQueue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentLoggedInQueue that = (StudentLoggedInQueue) o;
        return Objects.equals(studentQueue, that.studentQueue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentQueue);
    }

    public StudentLoggedIn get(int i) {
        return studentQueue.get(i);
    }
}
