package ru.hogwarts.school2.service;

import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school2.exception.FacultyNotFoundException;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.repository.FacultyRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    private final Logger logger = (Logger) LoggerFactory.getLogger(AvatarServiceImpl.class);

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    @Override
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Отработал метод createFaculty");
        return facultyRepository.save(faculty);

    }

    @Override
    public Faculty findFaculty(long id) {
        logger.info("Отработал метод findFaculty");
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
    }

    @Override
    public Faculty editFaculty(long id, Faculty faculty) {
        logger.info("Отработал метод editFaculty");
        if (!facultyRepository.existsById(id)) {
            throw new FacultyNotFoundException(id);
        }
        faculty.setId(id);
        facultyRepository.save(faculty);

        return faculty;
    }

    @Override
    public Faculty deleteFaculty(long id) {
        logger.info("Отработал метод deleteFaculty");
        Faculty faculty = facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return faculty;
    }

    @Override
    public List<Faculty> filterAllByColor(String color) {
        logger.info("Отработал метод filterAllByColor");
        return facultyRepository.findAll().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .toList();

    }

    @Override
    public List<Faculty> getFacultyByColorOrName(String color, String name) {
        logger.info("Отработал метод getFacultyByColorOrName");
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }
    @Override
    public List<Faculty> findByColor(String color) {
        logger.info("Отработал метод findByColor");
        return facultyRepository.findByColorIgnoreCase(color);
    }
}
