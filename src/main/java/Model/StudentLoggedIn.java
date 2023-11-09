package Model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class StudentLoggedIn implements Serializable {
    private Student student;
    private String topic, instructor, courses;
    private LocalDate date;
    private LocalTime timeIn, timeOut;
    private boolean isLoggedIn;
    private int duration;

    public StudentLoggedIn(Student student, String courses, String topic, String instructor, Boolean isLoggedIn) {
        this.student = student;
        this.courses = courses;
        this.topic = topic;
        this.instructor = instructor;
        this.isLoggedIn = isLoggedIn;
    }

    public StudentLoggedIn(Student student, String courses, String topic, String instructor,
                           Boolean isLoggedIn, LocalDate date, LocalTime timeIn, LocalTime timeOut) {
        this(student, courses, topic, instructor, isLoggedIn);
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public StudentLoggedIn(StudentLoggedIn studentLoggedIn, Boolean isLoggedIn) {
        this(studentLoggedIn.getStudent(), studentLoggedIn.getCourses(), studentLoggedIn.getTopic(),
                studentLoggedIn.getInstructor(), isLoggedIn);
    }

    public String getId() {
    	return student.getId();
    }

    public String getName() {
    	return student.getName();
    }

    public void startTime() {
        	timeIn = LocalTime.now();
        	date = LocalDate.now();
    }
    public void endTime() {
    	timeOut = LocalTime.now();
    }
    public LocalDate getDate() {
    	return date;
    }

    public LocalTime getTimeIn() {
    	return timeIn;
    }

    public LocalTime getTimeOut() {
    	return timeOut;
    }
    public void setTimeOut() {
    	timeOut = LocalTime.now();
    }

    public Student getStudent() {
        return student;
    }

    public String getCourses() {
        return courses;
    }

    public String getTopic() {
        return topic;
    }

    public String getInstructor() {
        return instructor;
    }

    public boolean didTimeStart() {
    	return timeIn != null;
    }

    public boolean didTimeEnd() {
    	return timeOut != null;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean getLoggedIn() {
        return isLoggedIn;
    }

    public int getDuration() {
        if (timeIn != null && timeOut != null) {
            return Duration.between(timeIn, timeOut).toMinutesPart();
        } else {
            return 0;
        }
    }

    public void addDuration(int sessionMinutes) {
        if (timeIn != null && timeOut != null) {
            duration += sessionMinutes;
        }
    }

    @Override
    public String toString() {
        return "Student is " + student +
                ", topic is " + topic +
                ", instructor is " + instructor +
                ", courses is " + courses +
                ", date is " + date +
                ", time is " + timeIn +
                ", time out is " + timeOut +
                ", logged in is " + isLoggedIn +
                ", duration is " + duration;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentLoggedIn that = (StudentLoggedIn) o;
        return isLoggedIn == that.isLoggedIn &&
                duration == that.duration &&
                Objects.equals(student, that.student) &&
                Objects.equals(topic, that.topic) &&
                Objects.equals(instructor, that.instructor) &&
                Objects.equals(courses, that.courses) &&
                Objects.equals(date, that.date) &&
                Objects.equals(timeIn, that.timeIn) &&
                Objects.equals(timeOut, that.timeOut);
    }


    @Override

    public int hashCode() {
        return Objects.hash(student, topic, instructor, courses, date, timeIn, timeOut, isLoggedIn, duration);
    }

}
