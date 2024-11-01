package ru.hogwarts.school2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school2.model.Faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
   List<Faculty> findByColorIgnoreCaseOrNameIgnoreCase(String color, String name);
   Optional<Faculty> findById(Long id);
}

