package ru.job4j.dreamjob.controller;

import org.junit.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.servise.CityService;
import ru.job4j.dreamjob.servise.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostControlTest {
    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post", "Description", LocalDateTime.now(), true, new City()),
                new Post(2, "New post", "Description", LocalDateTime.now(), false, new City())
        );
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        PostControl postController = new PostControl(
                postService,
                cityService
        );
        String page = postController.posts(model, session);
        verify(model).addAttribute("posts", posts);
        assertThat(page, is("posts"));
    }

    @Test
    public void whenCreatePost() {
        Post input = new Post(1, "New post", "Description", LocalDateTime.now(), true, new City());
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostControl postController = new PostControl(
                postService,
                cityService
        );
        String page = postController.createPost(input);
        verify(postService).add(input);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenAddPost() {
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        List<City> cities = List.of(
                new City(1, "Moscow"),
                new City(2, "New York")
        );
        when(cityService.getAllCities()).thenReturn(cities);
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        PostControl postController = new PostControl(
                postService,
                cityService
        );
        String page = postController.addPost(model, session);
        verify(model).addAttribute("cities", cities);
        assertThat(page, is("addPost"));
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post(1, "Post", "Description", LocalDateTime.now(), false, new City(2, "Moscow"));
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostControl postController = new PostControl(
                postService,
                cityService
        );
        City updatedCity = new City(1, "City");
        when(cityService.findById(post.getCity().getId())).thenReturn(new City(1, "City"));
        String page = postController.updatePost(post);
        assertThat(post.getCity(), is(updatedCity));
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenFormUpdatePost() {
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostControl postController = new PostControl(
                postService,
                cityService
        );
        Model model = mock(Model.class);
        int postId = 1;
        HttpSession session = mock(HttpSession.class);
        Post post = new Post(1, "Name", "Descr", LocalDateTime.now(), true, new City(1, "City"));
        List<City> cities = List.of(
                new City(1, "Moscow"),
                new City(2, "New York"),
                new City(1, "Brno")
        );
        when(postService.findById(postId)).thenReturn(post);
        when(cityService.getAllCities()).thenReturn(cities);
        String page = postController.formUpdatePost(model, postId, session);
        verify(model).addAttribute("post", post);
        verify(model).addAttribute("cities", cities);
        assertThat(page, is("updatePost"));
    }


}