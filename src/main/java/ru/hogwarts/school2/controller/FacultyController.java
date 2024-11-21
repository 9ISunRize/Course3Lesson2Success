package ru.hogwarts.school2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;
import ru.hogwarts.school2.service.FacultyService;
import ru.hogwarts.school2.service.StudentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    @Autowired
    private FacultyService facultyService;


    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}/get")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.findFaculty(id);
    }

    @PostMapping("/add")
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<Faculty> editFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        Faculty updatedStudent = facultyService.editFaculty(id, faculty);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}/delete")
    public Faculty deliteFaculty(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping("/get/by-color")
    public ResponseEntity<List<Faculty>> getFacultiesByColor(@RequestParam String color) {
        List<Faculty> faculties = facultyService.findByColor(color);
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/get/by-color-or-name")
    public List<Faculty> getFacultyByColorOrName(@RequestParam("color") String color,
                                                 @RequestParam("name") String name) {
        return facultyService.getFacultyByColorOrName(color, name);
    }
    @GetMapping("/longestnameoffaculty")
    public ResponseEntity<String> getLongestNameOfFaculty(){
        return facultyService.getLongestNameOfFaculty();
    }
}