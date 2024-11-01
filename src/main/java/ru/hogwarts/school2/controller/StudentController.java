package ru.hogwarts.school2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;
import ru.hogwarts.school2.service.StudentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}/get")
    public Student getStudent(@PathVariable("id") Long id) {
        return studentService.findStudent(id);
    }

    @PostMapping("/add")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}/edit")
    public void editStudent(@PathVariable("id") Long id, @RequestBody Student student) {
        studentService.editStudent(id, student);
    }

    @DeleteMapping("/{id}/delete")
    public Student deleteStudent(@PathVariable("id") Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/get/by-age")
    public List<Student> filterAllByAge(@RequestParam("age") int age) {
        return studentService.filterAllByAge(age);
    }

    @GetMapping("/get/by-age-between")
    List<Student> findByAgeBetween(int ageMin, int ageMax) {
        return studentService.findByAgeBetween(ageMin, ageMax);
    }

    @GetMapping("/{id}/get-by-faculty")
    public List <Student> findStudentByFacultyId(@PathVariable("id") Long id) {
        return studentService. findStudentsByFacultyId(id);
    }

}