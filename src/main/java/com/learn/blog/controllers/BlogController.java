package com.learn.blog.controllers;

import com.learn.blog.model.Post;
import com.learn.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model) {
        model.addAttribute("title", "Blog page");
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/article_add")
    public String blogAdd(Model model) {
        model.addAttribute("title", "Article add page");
        return "article_add";
    }

    @PostMapping("/article_add")
    public String postAdd(@RequestParam String title,
                             @RequestParam String anons,
                             @RequestParam String full_text, Model model) {
        Post post = new Post(title, anons, full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog-details/{id}")
    public String postView(@PathVariable(value = "id") Long postId, Model model) {
        if(!postRepository.existsById(postId)){
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post", result);
        return "blog-details";
    }
    @GetMapping("/blog-details/{id}/edit")
    public String postEdit(@PathVariable(value = "id") Long postId, Model model) {
        if(!postRepository.existsById(postId)){
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post", result);
        return "blog-edit";
    }

    @PostMapping("/blog-details/{id}/edit")
    public String postRemove(@PathVariable(value = "id") Long postId,
                             @RequestParam String title,
                             @RequestParam String anons,
                             @RequestParam String full_text, Model model) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }
    @PostMapping("/blog-details/{id}/remove")
    public String postRemove(@PathVariable(value = "id") Long postId, Model model) {
        Post post = postRepository.findById(postId).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }

}

