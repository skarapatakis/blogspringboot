package com.learning.blog;

import com.learning.blog.entity.BlogPost;
import com.learning.blog.entity.Category;
import com.learning.blog.entity.Tag;
import com.learning.blog.service.BlogService;
import com.learning.blog.service.CategoryService;
import com.learning.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;

import java.util.*;

@SpringBootApplication
public class BlogApplication implements CommandLineRunner {

    private BlogService blogService;
    private CategoryService categoryService;
    private TagService tagService;

    @Autowired
    public BlogApplication(BlogService blogService, CategoryService categoryService, TagService tagService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Adding some data dynamically");

        BlogPost newPost = new BlogPost();
        newPost.setTitle("Dynamic Post");
        newPost.setContent("Dynamic Post Content");

        BlogPost postRun = new BlogPost();
        postRun.setTitle("Running your first 5K");
        postRun.setContent("All the planning and details for your preparation of a 5K race");

        BlogPost postMarathon = new BlogPost();
        postMarathon.setTitle("Running your first marathon");
        postMarathon.setContent("All the planning and details for your preparation of a marathon race");

        Category category = new Category();
        Category catExercise = new Category();
        Set<Category> categories = new HashSet<>();

        category.setTitle("Dynamic category");
        category.setDescription("Dynamic category Description");

        catExercise.setTitle("Exercise");
        catExercise.setDescription("All posts about working out and exercise");

        categories.add(category);
       // categories.add(catExercise);

        newPost.setCategory(categories);

        Set<Category> categorySet = new HashSet<>();
        Set<Category> cs = new HashSet<>();
        categorySet.add(catExercise);
        postRun.setCategory(categorySet);


        Category healthExercise = new Category();
        healthExercise.setTitle("Health Exercise");
        healthExercise.setDescription("All posts about health and nutrition");

        cs.add(healthExercise);
        postMarathon.setCategory(cs);

        Tag tag1 = new Tag();
        tag1.setTitle("misc");
        tag1.setCreationDate(new Date());
        Tag tag2 = new Tag();
        tag2.setTitle("VO-2");

        Set<Tag> tags = new HashSet<>();
        tags.add(tag1);
        tags.add(tag2);
        postRun.setTag(tags);

        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag1);


        blogService.createOrUpdate(postRun);
        blogService.createOrUpdate(newPost);
        blogService.createOrUpdate(postMarathon);

        cs.add(catExercise);
        postRun.setCategory(cs);
        blogService.createOrUpdate(postRun);

        postMarathon.setTag(tagSet);
        blogService.createOrUpdate(postMarathon);


        Tag t3 = new Tag();
        t3.setTitle("ui");
        t3.setCreationDate(new Date());

        tagService.createOrUpdate(t3);

    }

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

}
