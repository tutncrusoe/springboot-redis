package com.codingworld.springbootredis.controller;

import com.codingworld.springbootredis.entity.Student;
import com.codingworld.springbootredis.repo.StudentRepo;
import com.codingworld.springbootredis.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
public class StudentController {

    @Autowired
    StudentRepo studentRepo;

    @Autowired
    StudentService studentService;

    @GetMapping("/getAllStudent")
    public List<Student> getAllStudent() {
        List<Student> actualList = new ArrayList<>();
        studentRepo.findAll().iterator().forEachRemaining(actualList::add);
        return actualList;
    }

    @PostMapping("/saveStudent")
    public Student saveStudentData(@RequestBody Student student) {
        studentRepo.save(student);
        return student;
    }

    @DeleteMapping("/deleteStudent")
    @CacheEvict(key = "#id", value = "studentCache")
    public void deleteStudent(@PathParam("id") Integer id) {
        studentRepo.deleteById(id);
    }

    @GetMapping("/getStudentById/{id}")
    @Cacheable(key = "#id", value = "studentCache", unless = "#result.point < 1000") // call to repo with result.point < 1000
    public Student getStudent(@PathVariable Integer id) {
        return studentService.getStudentById(id);
    }

    @PostConstruct
    public void saveStudent() {
        Student student = new Student();
        student.setId(1);
        student.setName("Nilesh");
        student.setPoint(900);
        studentRepo.save(student);
    }
}
