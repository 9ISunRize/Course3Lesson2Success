package ru.hogwarts.school2.service;

import ru.hogwarts.school2.model.Faculty;

import java.util.List;


public interface FacultyService {
    Faculty createFaculty(Faculty faculty);

    Faculty findFaculty(long id);

    Faculty editFaculty(long id, Faculty faculty);

    Faculty deleteFaculty(long id);

    List<Faculty> filterAllByColor(String color);

    List<Faculty> getFacultyByColorOrName(String color, String name);
    List<Faculty> findByColor(String color);

}
