package ru.hogwarts.school2.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school2.model.Avatar;
import ru.hogwarts.school2.service.AvatarService;
import ru.hogwarts.school2.service.AvatarServiceImpl;

import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadImage(@RequestParam("studentId") long studentId,
                            @RequestBody MultipartFile multipartFile) throws IOException {
        avatarService.uploadImage(studentId, multipartFile);
    }

    @GetMapping("/get/from-db")
    public ResponseEntity<byte[]> getAvatarFromDB(@RequestParam("studentId") long studentId) {
        Avatar avatar = avatarService.getAvatarFromDB(studentId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .body(avatar.getData());
    }

    @GetMapping(path = "/get/from-local", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getAvatarFromLocal(@RequestParam("studentId") long studentId) {
        return avatarService.getAvatarFromLocal(studentId);
    }
    @GetMapping(value = "/all")
    public Page<Avatar> getAllAvatars(@RequestParam("page") int page, @RequestParam("size") int size) {
        return avatarService.getAllAvatars(page,size);
    }
}
