package app.service;

import app.model.Status;
import java.util.List;
import java.util.Optional;

public interface StatusService {

    List<Status> findAll();

    Optional<Status> findById(int id);

    Optional<Status> findByName(String name);

    Optional<Status> findByType(String type);

    Status save(Status status);

    void deleteById(int id);
}
