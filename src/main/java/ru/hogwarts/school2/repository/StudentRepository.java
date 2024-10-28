package ru.hogwarts.school2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
