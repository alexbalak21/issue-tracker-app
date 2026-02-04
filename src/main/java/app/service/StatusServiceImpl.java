package app.service;

import app.model.Status;
import app.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public List<Status> findAll() {
        return statusRepository.findAll();
    }

    @Override
    public Optional<Status> findById(int id) {
        return statusRepository.findById(id);
    }

    @Override
    public Optional<Status> findByName(String name) {
        return statusRepository.findByName(name);
    }

    @Override
    public Optional<Status> findByDescription(String description) {
        return statusRepository.findByDescription(description);
    }

    @Override
    public Status save(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public void deleteById(int id) {
        statusRepository.deleteById(id);
    }
}
