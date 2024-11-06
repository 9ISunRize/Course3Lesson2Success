package ru.hogwarts.school2.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school2.model.Avatar;

import java.io.IOException;

public interface AvatarService {
    void uploadImage(long studentId, MultipartFile multipartFile) throws IOException;

    Avatar getAvatarFromDB(long studentId);

    byte[] getAvatarFromLocal(long studentId);
}
