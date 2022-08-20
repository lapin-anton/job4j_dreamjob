package ru.job4j.dreamjob.store;

import org.junit.*;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CandidateDBStoreTest {

    private static Connection connection;

    @BeforeClass
    public static void initConnection() {
        try (InputStream in = CandidateDBStoreTest.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(
                    config.getProperty("jdbc.url"),
                    config.getProperty("jdbc.username"),
                    config.getProperty("jdbc.password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @After
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from candidate")) {
            statement.execute();
        }
    }
    
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