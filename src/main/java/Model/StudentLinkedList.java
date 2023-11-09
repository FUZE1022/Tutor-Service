package Model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

public class StudentLinkedList implements Serializable {
    LinkedList<Student> studentLinkedList;

    public StudentLinkedList() {
        this(new LinkedList<>());
    }

    public StudentLinkedList(LinkedList<Student> studentLinkedList) {
        this.studentLinkedList = studentLinkedList;
    }

    public void addStudent(Student student) {
        studentLinkedList.add(student);
    }

    public boolean search(Student student) {
        return studentLinkedList.contains(student);
    }

    public Optional<Student> searchStudentId(String id) {
        for (Student student : studentLinkedList) {
            if (student.getId().equals(id)) {
                return Optional.of(student);
            }
        }
        return Optional.empty();
    }

    public Optional<Student> searchFirstLastName(String firstName, String lastName) {
        for (Student student : studentLinkedList) {
            String name = student.getName();
            String[] nameArray = name.split(" ");
            if (nameArray.length >= 2) {
                String studentFirstName = nameArray[1], studentLastName = nameArray[0];
                if (studentLastName.equalsIgnoreCase(lastName) && studentFirstName.equalsIgnoreCase(firstName)) {
                    return Optional.of(student);
                }
            }
        }
        return Optional.empty();
    }

    public int size() {
        return studentLinkedList.size();
    }

    public Student get(int i) {
        return studentLinkedList.get(i);
    }

    public LinkedList<Student> getStudentLinkedList() {
        return studentLinkedList;
    }

    public void setStudentLinkedList(LinkedList<Student> studentLinkedList) {
        this.studentLinkedList = studentLinkedList;
    }

    public void display() {
        for (Student student : studentLinkedList) {
            System.out.println(student);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentLinkedList that = (StudentLinkedList) o;
        return Objects.equals(studentLinkedList, that.studentLinkedList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentLinkedList);
    }
}
