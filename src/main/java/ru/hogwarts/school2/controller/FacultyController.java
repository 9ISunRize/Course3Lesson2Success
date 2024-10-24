package ru.hogwarts.school2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.service.FacultyService;

import java.util.List;

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
    public Faculty editFaculty(Faculty faculty) {
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("/{id}/delete")
    public Faculty deliteFaculty(@PathVariable Long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping("/get/by-color")
    public List<Faculty> filterAllByColor(@RequestParam("color") String color) {
        return facultyService.filterAllByColor(color);
    }
}