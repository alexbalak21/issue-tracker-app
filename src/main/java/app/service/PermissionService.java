package app.service;

import java.util.List;
import org.springframework.stereotype.Service;
import app.model.Permission;
import app.repository.PermissionRepository;

@Service
public class PermissionService {

    private final PermissionRepository repo;

    public PermissionService(PermissionRepository repo) {
        this.repo = repo;
    }

    public Permission create(String name, String description) {
        Permission p = new Permission();
        p.setName(name);
        p.setDescription(description);
        return repo.save(p);
    }

    public List<Permission> getAll() {
        return repo.findAll();
    }
}
