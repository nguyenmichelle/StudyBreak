package com.example.michellenguy3n.studybreak.model;

/**
 * This class models the Course object. A Course contains the details of a university/college course.
 * 
 * Created by michellenguy3n on 12/11/16.
 */
public class Course {
    public String subject;
    public String courseNumber;
    public String className;
    public String instructor;

    public Course(String _subject, String _courseNumber, String _className, String _instructor) {
        subject = _subject;
        courseNumber = _courseNumber;
        className = _className;
        instructor = _instructor;
    }

    public String getSubject() {
        return subject;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getClassName() {
        return className;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public boolean checkEquals(Course course) {
        if ((this.getCourseNumber().equals(course.getCourseNumber())) && (this.getSubject().equals(course.getSubject()))) {
            return true;
        } else {
            return false;
        }
    }
}
