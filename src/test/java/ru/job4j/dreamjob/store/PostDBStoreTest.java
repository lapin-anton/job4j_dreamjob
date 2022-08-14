package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.is;

public class PostDBStoreTest {

    @Test
    public void whenCreatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0,
                "Java Job",
                "Nice job",
                LocalDateTime.now(),
                true,
                new City(1, "Moscow"));
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenUpdatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0,
                "Java Job",
                "Nice job",
                LocalDateTime.now(),
                true,
                new City(1, "Moscow"));
        store.add(post);
        post = store.findById(post.getId());
        post.setName("Python Job");
        store.update(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenFindAllPosts() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        List<Post> posts = List.of(
            new Post(0, "Java Job", "Nice job", LocalDateTime.now(), true, new City(1, "Moscow")),
            new Post(0, "Python Job", "Not bad job", LocalDateTime.now(), false, new City(2, "St. Petersburg")),
            new Post(0, "Java Script Job", "So so job", LocalDateTime.now(), true, new City(11, "Sacramento"))
        );
        posts.forEach(store::add);
        List<Post> postsInDb = store.findAll();
        assertThat(postsInDb.size(), is(posts.size()));
        List<String> names = posts.stream().map(Post::getName).collect(Collectors.toList());
        List<String> postNamesInDb = postsInDb.stream().map(Post::getName).collect(Collectors.toList());
        assertThat(postNamesInDb, is(names));
    }

}