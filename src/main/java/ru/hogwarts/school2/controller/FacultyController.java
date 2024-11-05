package ru.hogwarts.school2.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Faculty createFaculty(Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping("/{id}/edit")
    public void editFaculty(@PathVariable("id") Long id, Faculty faculty) {
        facultyService.editFaculty(id, faculty);
    }

    @DeleteMapping("/{id}/delete")
    public Faculty deliteFaculty(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping("/get/by-color")
    public List<Faculty> filterAllByColor(@RequestParam("color") String color) {
        return facultyService.filterAllByColor(color);
    }

    @GetMapping("/get/by-color-or-name")
    public List<Faculty> getFacultyByColorOrName(@RequestParam("color") String color,
                                                 @RequestParam("name") String name) {
        return facultyService.getFacultyByColorOrName(color, name);
    }

}