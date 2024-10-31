package ru.hogwarts.school2.service;

import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;

import java.util.List;


public interface FacultyService {
    Faculty createFaculty(Faculty faculty);

    Faculty findFaculty(long id);

    void editFaculty(long id, Faculty faculty);

    Faculty deleteFaculty(long id);

    List<Faculty> filterAllByColor(String color);

    List<Faculty> getFacultyByColorOrName(String color, String name);
}
