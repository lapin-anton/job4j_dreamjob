package ru.job4j.dreamjob.servise;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostDBStore;
import ru.job4j.dreamjob.store.PostStore;

import java.util.Collection;
import java.util.List;

@Service
@ThreadSafe
public class PostService {

    private final PostDBStore store;

    private final CityService cityService;

    public PostService(PostDBStore store, CityService cityService) {
        this.store = store;
        this.cityService = cityService;
    }

    public Collection<Post> findAll() {
        List<Post> posts = store.findAll();
        posts.forEach(p -> p.setCity(
            cityService.findById(p.getCity().getId())
        ));
        return posts;
    }

    public void add(Post post) {
        store.add(post);
    }

    public Post findById(int id) {
        Post post = store.findById(id);
        if (post != null) {
            post.setCity(cityService.findById(post.getCity().getId()));
        }
        return post;
    }

    public void update(Post post) {
        store.update(post);
    }

}
