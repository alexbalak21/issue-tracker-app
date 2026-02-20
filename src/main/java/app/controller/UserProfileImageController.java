package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import app.model.UserProfileImage;
import app.service.UserProfileImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserProfileImageController {
        private static final Logger log = LoggerFactory.getLogger(UserProfileImageController.class);
    
    @Autowired
    private UserProfileImageService userProfileImageService;

    @Autowired
    private app.service.AuthService authService;

    @PostMapping("/users/profile-image")
    public ResponseEntity<Long> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        try {
            Long userId = authService.getCurrentUserId();
            log.info("Uploading profile image for user id: {}", userId);
            log.info("File received: name={}, size={}, type={}", file.getOriginalFilename(), file.getSize(), file.getContentType());
            Long savedId = userProfileImageService.saveProfileImage(userId, file);
            log.info("Profile image saved with id: {}", savedId);
            return ResponseEntity.ok(savedId);
        } catch (Exception e) {
            log.error("Failed to upload profile image", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users/profile-image/{id}.jpg")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id) {
        UserProfileImage img = userProfileImageService.getProfileImageById(id);
        if (img == null || img.getData() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + img.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(img.getMimeType()))
                .body(img.getData());
    }
}
