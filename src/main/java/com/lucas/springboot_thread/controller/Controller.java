package com.lucas.springboot_thread.controller;

import com.lucas.springboot_thread.entity.Student;
import com.lucas.springboot_thread.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @Autowired
    StudentService studentService;
    @Autowired
    ThreadPoolTaskExecutor poolExecutor;
    @RequestMapping(value = "thread")
    public String testThread(){
        List<String> list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        long startTime=System.currentTimeMillis();
        List<Student> students = threadHandle(list);
        students.forEach(student ->{
            System.out.println(student.getId());
            System.out.println(student.getName());
        });
        System.out.println("花费的总时间："+(System.currentTimeMillis()-startTime));

        return "sad";
    }



    public List<Student> threadHandle(List<String> lists){
        LOGGER.info("线程开始啦");
        List<Future<List<Student>>> futures=new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(lists.size());
        lists.forEach(list -> {
            Future<List<Student>> future= poolExecutor.submit(() -> {
                long startTime = System.currentTimeMillis();
                try {
                    return studentService.getStudent(list);
                } finally {
                    System.out.println(Thread.currentThread().getName()+"单个线程执行耗时ms:"
                            + (System.currentTimeMillis() - startTime));
                    latch.countDown();
                }
            });
            futures.add(future);
        });
        List<Student> students=new ArrayList<>();
        try {
            latch.await();
            futures.forEach(f->{
                try {
                    students.addAll(f.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LOGGER.error("thread Exception get");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    LOGGER.error("thread Exception get");
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error("thread Exception");
        }
        LOGGER.info("thread start=====success");

        return students;
    }


}
