package ru.hogwarts.school2.service;

import org.junit.platform.commons.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school2.exception.AvatarNotFoundException;
import ru.hogwarts.school2.exception.StudentNotFoundException;
import ru.hogwarts.school2.model.Avatar;
import ru.hogwarts.school2.model.Student;
import ru.hogwarts.school2.repository.AvatarRepository;
import ru.hogwarts.school2.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@Service
public class AvatarServiceImpl implements AvatarService {
    private final Logger logger = (Logger) LoggerFactory.getLogger(AvatarServiceImpl.class);
    @Value("${path.dir}")
    private String pathDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    @Override
    public void uploadImage(long studentId, MultipartFile multipartFile) throws IOException {
        logger.info("Отработал метод uploadImage");

        createDirectory();

        Path filePath = Path.of(pathDir, UUID.randomUUID() + "." + getExtension(multipartFile.getOriginalFilename()));

        createAvatar(studentId, multipartFile, filePath.toString());

        multipartFile.transferTo(filePath);
    }

    @Override
    public Avatar getAvatarFromDB(long studentId){
        logger.info("Отработал метод getAvatarFromDB");
        boolean studentExist = studentRepository.existsById(studentId);
        if (!studentExist){
            throw new StudentNotFoundException(studentId);
        }
        return avatarRepository.getByStudentId(studentId)
                .orElseThrow(AvatarNotFoundException::new);
    }

    @Override
    public byte[] getAvatarFromLocal(long studentId) {
        logger.info("Отработал метод getAvatarFromLocal");
        boolean studentExist = studentRepository.existsById(studentId);
        if (!studentExist){
            throw new StudentNotFoundException(studentId);
        }

        Avatar avatar = avatarRepository.getByStudentId(studentId)
                .orElseThrow(AvatarNotFoundException::new);
        String filePath = avatar.getFilePath();
        try(BufferedInputStream bufferedOutputStream = new BufferedInputStream(new FileInputStream(filePath))) {
            return bufferedOutputStream.readAllBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("Чтение картинки не удалось: " + e.getMessage());
        }
    }

    private void createAvatar(long studentId, MultipartFile multipartFile, String filePath) throws IOException {
        logger.info("Отработал метод createAvatar");
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        avatarRepository.save(new Avatar(
                filePath,
                multipartFile.getSize(),
                multipartFile.getContentType(),
                multipartFile.getBytes(),
                student
        ));
    }

    private String getExtension(String originalPath) {
        logger.info("Отработал метод getExtension");
        return originalPath.substring(originalPath.lastIndexOf(".") + 1);
    }

    private void createDirectory() throws IOException {
        logger.info("Отработал метод createDirectory");
        Path path = Path.of(pathDir);
        if (Files.notExists(path)) {
            Files.createDirectory(path);
        }
    }
    @Override
    public Page<Avatar> getAllAvatars(Integer pageNo, Integer pageSize) {
        logger.info("Отработал метод getAllAvatars");
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return avatarRepository.findAll(paging);
    }
}
