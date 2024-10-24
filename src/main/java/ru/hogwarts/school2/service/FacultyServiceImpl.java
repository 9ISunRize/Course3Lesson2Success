package ru.hogwarts.school2.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school2.model.Faculty;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private long listId = 0;

    @Override
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++listId);
        faculties.put(listId, faculty);
        return faculty;
    }

    @Override
    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }

    @Override
    public List<Faculty> filterAllByColor(String color) {
        List<Faculty> list = faculties.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .toList();
        return Collections.unmodifiableList(list);
    }
}
