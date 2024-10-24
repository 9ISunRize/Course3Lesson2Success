package ru.hogwarts.school2.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school2.model.Student;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final HashMap<Long, Student> students = new HashMap<>();
    private long lastId = 0;

    @Override
    public Student createStudent(Student student) {
        student.setId(++lastId);
        students.put(lastId, student);
        return student;
    }

    @Override
    public Student findStudent(long id) {
        return students.get(id);
    }

    @Override
    public Student editStudent(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Student deleteStudent(long id) {
        return students.remove(id);

    }

    @Override
    public List<Student> filterAllByAge(int age) {
        List<Student> list = students.values().stream().filter(student -> student.getAge() == age).toList();
        return Collections.unmodifiableList(list);
    }
}
