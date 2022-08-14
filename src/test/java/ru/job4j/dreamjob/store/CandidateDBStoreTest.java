package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CandidateDBStoreTest {
    
    @Test
    public void whenCreateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Petrov", "Java Developer", LocalDateTime.now());
        store.add(candidate);
        Candidate postInDb = store.findById(candidate.getId());
        assertThat(postInDb.getName(), is(candidate.getName()));
    }

    @Test
    public void whenUpdateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Petrov", "Java Developer", LocalDateTime.now());
        store.add(candidate);
        candidate = store.findById(candidate.getId());
        candidate.setName("Python Job");
        store.update(candidate);
        Candidate postInDb = store.findById(candidate.getId());
        assertThat(postInDb.getName(), is(candidate.getName()));
    }

    @Test
    public void whenFindAllCandidates() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        List<Candidate> posts = List.of(
                new Candidate(0, "Petrov", "Java Developer", LocalDateTime.now()),
                new Candidate(0, "Ivanov", "Python Developer", LocalDateTime.now()),
                new Candidate(0, "Sidorov", "Java Script Developer", LocalDateTime.now())
        );
        posts.forEach(store::add);
        List<Candidate> postsInDb = store.findAll();
        assertThat(postsInDb.size(), is(posts.size()));
        List<String> names = posts.stream().map(Candidate::getName).collect(Collectors.toList());
        List<String> postNamesInDb = postsInDb.stream().map(Candidate::getName).collect(Collectors.toList());
        assertThat(postNamesInDb, is(names));
    }
    
}