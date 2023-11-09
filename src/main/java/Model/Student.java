package Model;

import java.io.Serializable;
import java.util.Objects;

public class Student implements Serializable {
    private String id, name, course;

    public Student(String id, String name, String course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String[] getCourseArr() {
        return course.split(" ");
    }

    public String getFirstName() {
    	String[] nameArray = name.split(" ");
    	if (nameArray.length >= 2) {
    		return nameArray[1];
    	}
    	return "";
    }

    public String getLastName() {
    	String[] nameArray = name.split(" ");
    	if (nameArray.length >= 2) {
    		return nameArray[0];
    	}
    	return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(name, student.name) && Objects.equals(course, student.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, course);
    }

    @Override
    public String toString() {
        return "Student[" +
                "id is '" + id +
                ", name is " + name +
                ", course is " + course +
                ']';
    }
}
