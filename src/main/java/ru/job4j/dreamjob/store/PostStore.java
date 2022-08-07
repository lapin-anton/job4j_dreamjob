package ru.job4j.dreamjob.store;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PostStore {

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private final AtomicInteger id = new AtomicInteger();

    private PostStore() {
        add(new Post(1, "Junior Java Job", "Easy to work", LocalDateTime.now()));
        add(new Post(2, "Middle Java Job", "Very easy to work", LocalDateTime.now()));
        add(new Post(3, "Senior Java Job", "Best job on the Earth", LocalDateTime.now()));
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void add(Post post) {
        post.setId(id.incrementAndGet());
        post.setCreated(LocalDateTime.now());
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public void update(Post post) {
        post.setCreated(LocalDateTime.now());
        posts.replace(post.getId(), post);
    }
}
