package ru.hogwarts.school2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school2.model.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
}