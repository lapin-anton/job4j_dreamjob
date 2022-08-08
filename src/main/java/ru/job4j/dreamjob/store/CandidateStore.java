package ru.job4j.dreamjob.store;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CandidateStore {

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private final AtomicInteger id = new AtomicInteger();

    private CandidateStore() {
        add(new Candidate(1, "Petrov", "Java Junior Developer", LocalDateTime.now()));
        add(new Candidate(2, "Ivanov", "Java Middle Developer", LocalDateTime.now()));
        add(new Candidate(3, "Sidorov", "Java Senior Developer", LocalDateTime.now()));
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public void add(Candidate candidate) {
        candidate.setId(id.incrementAndGet());
        candidate.setCreated(LocalDateTime.now());
        candidates.put(candidate.getId(), candidate);
    }

    public void update(Candidate candidate) {
        candidate.setCreated(LocalDateTime.now());
        candidates.replace(candidate.getId(), candidate);
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }
}
