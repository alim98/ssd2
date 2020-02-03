package com.alim.ssn.test;

import com.alim.ssn.model.Student;

import java.util.ArrayList;
import java.util.List;

public class FakeStudentGenerator {

    public static List<Student> getStudents(){
        List<Student> students=new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Student student=new Student();
          //  student.setId("bXfY1FAhtReWuHpXrEnygbUt7Ll1");
            student.setName("student "+i+1);
            student.setPhotoUrl("https://dornsife.usc.edu/tools/mytools/PersonnelInfoSystem/DOC/Student/PHIL/photo_1078846.jpg");
            students.add(student);
        }
        return students;
    }
}
