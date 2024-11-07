package ru.hogwarts.school2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school2.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> getByStudentId(long studentId);
}
