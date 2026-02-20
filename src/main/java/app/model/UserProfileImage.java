package app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profile_image")
public class UserProfileImage {
    @Version
    private Long version;

    @Id
    private Long id; // This will be set to the user's ID

    @Column(nullable = true)
    private String filename;

    @Column(name = "mime_type", nullable = true)
    private String mimeType;

    @Lob
    @Column(nullable = true)
    private byte[] data;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
