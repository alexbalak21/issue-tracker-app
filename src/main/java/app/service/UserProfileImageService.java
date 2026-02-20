package app.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.model.UserProfileImage;
import app.repository.UserProfileImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
public class UserProfileImageService {
        private static final Logger log = LoggerFactory.getLogger(UserProfileImageService.class);
    @Autowired
    private UserProfileImageRepository repo;

    public UserProfileImage getProfileImageById(Long id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * Save or update compressed profile image for a user.
     */
    public Long saveProfileImage(Long id, org.springframework.web.multipart.MultipartFile file) throws IOException {
            log.info("Saving profile image for user id: {}", id);
            log.info("File info: name={}, size={}, type={}", file.getOriginalFilename(), file.getSize(), file.getContentType());
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        log.info("Original image loaded: width={}, height={}", originalImage.getWidth(), originalImage.getHeight());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(120, 120)
                .outputQuality(0.8)
                .outputFormat("jpg")
                .toOutputStream(baos);
        log.info("Image compressed to 120x120, output size: {} bytes", baos.size());
        byte[] compressedData = baos.toByteArray();
        Optional<UserProfileImage> existing = repo.findById(id);
        UserProfileImage img;
        if (existing.isPresent()) {
            img = existing.get();
        } else {
            img = new UserProfileImage();
        }
        img.setFilename(file.getOriginalFilename());
        img.setMimeType("image/jpeg");
        img.setData(compressedData);
        if (existing.isPresent()) {
            img.setId(id); // Only set ID if updating
        }
        log.info("Saving UserProfileImage entity to repository");
        UserProfileImage saved = repo.save(img);
        log.info("Profile image saved with id: {}", saved.getId());
        return saved.getId();
    }

    /**
     * Get Base64-encoded profile image for a user.
     */
    public String getBase64Image(Long id) {
        UserProfileImage img = repo.findById(id).orElse(null);
        if (img == null || img.getData() == null) return null;
        return Base64.getEncoder().encodeToString(img.getData());
    }

    /**
     * Delete profile image for a user.
     */
    public void deleteProfileImage(Long id) {
        UserProfileImage img = repo.findById(id).orElse(null);
        if (img != null) repo.delete(img);
    }
}
