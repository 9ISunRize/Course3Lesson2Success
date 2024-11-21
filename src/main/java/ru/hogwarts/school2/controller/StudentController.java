package ru.hogwarts.school2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;
import ru.hogwarts.school2.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    public ResponseEntity<Student> editStudent(@PathVariable Long id, @RequestBody Student student) {
        Student updatedStudent = studentService.editStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
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

    @GetMapping("/{id}/get-faculty-by-student")
    public Faculty findFacultyByStudentId(@PathVariable("id") Long id) {
        Faculty faculty = studentService.findStudent(id).getFaculty();
        return faculty;
    }

    @GetMapping("/{id}/get-student-by-faculty")
    public List<Student> findStudentByFacultyId(@PathVariable("id") Long id) {
        return studentService.findStudentsByFacultyId(id);
    }
    @GetMapping("/students/{id}")
    public Collection<Student> readAllByFacultyId(@PathVariable long id) {
        return studentService.readByFacultyId(id);
    }

    @GetMapping("/filteredbyname")
    public ResponseEntity<Collection<String>> getAllStudentsWithName(){
        Collection<String> students = studentService.getFilteredByName();
        if(students.isEmpty()){return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
        return ResponseEntity.ok(students);
    }

    @GetMapping("/getallstudentsbyavgage")
    public Double getAllStudentsByAvgAge(){
        return studentService.getAllStudentsByAvgAge();
    }

    @GetMapping("/sum41")
    public int getSum(){
        long time =System.currentTimeMillis();
        Stream.iterate(1, a->a+1)
                .limit(1_000_000)
                //.parallel()
                .reduce(0,(a,b)->a+b);
        time=System.currentTimeMillis()-time;
        System.out.println("Время работы ="+time);
        return (int)time;
    }

}