package com.lucas.springboot_thread.service;

import com.lucas.springboot_thread.entity.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class StudentService {
    public List<Student> getStudent(String id){
        List<Student> list=new ArrayList<>();
        Student student=new Student();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        student.setId(id);
        student.setName("姓名："+id);
        list.add(student);
        return list;
    }
}
