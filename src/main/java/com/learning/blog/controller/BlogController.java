package com.learning.blog.controller;

import com.learning.blog.entity.*;
import com.learning.blog.service.BlogService;
import com.learning.blog.service.CategoryService;
import com.learning.blog.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class BlogController {

    Logger logger = LoggerFactory.getLogger(BlogController.class);

    private final BlogService blogService;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Autowired
    public BlogController(BlogService blogService, CategoryService categoryService, TagService tagService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String index(Model model) {
        logger.info("opening home page");
        model.addAttribute("blogs", blogService.findAll());
        return "index";
    }

    @GetMapping("/post/update/{id}")
    public String update(@PathVariable Long id, Model model) {

        PostDto editpostDto = new PostDto();

        BlogPost editpost = blogService.findById(id);
        Iterable<Category> allCategories = categoryService.getAllCategories();
        Iterable<Tag> allTags = tagService.getAllTags();

        LinkedList<CategorySelected> categorySelected = new LinkedList<>();
        Map<Category, Boolean> currentCategories = new HashMap<>();
        LinkedList<TagSelected> tagSelected = new LinkedList<>();
        Map<Tag, Boolean> currentTags = new HashMap<>();

        for (Category category : allCategories) {
            boolean selected = editpost.getCategory().contains(category);
            categorySelected.add(new CategorySelected(category.getId(), selected));
            currentCategories.put(category, selected);
        }

        for(Tag tag : allTags) {
            boolean selected = editpost.getTag().contains(tag);
            tagSelected.add(new TagSelected(tag.getId(), selected));
            currentTags.put(tag, selected);
        }

        editpostDto.setCurrentCategories(currentCategories);
        editpostDto.setSelectedCategories(categorySelected);

        editpostDto.setCurrentTags(currentTags);
        editpostDto.setSelectedTags(tagSelected);

        editpostDto.setPost(editpost);
        model.addAttribute("editpostdto", editpostDto);

        return  "update";
    }

    @PutMapping("/post/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("blogpost") PostDto postDto,
                         @RequestParam(required = false, defaultValue = "") Integer[] selectedIds,
                         @RequestParam(required = false, defaultValue = "") Integer[] selectedTagIds,
                         BindingResult bindingResult, Model model) {

        BlogPost editedPost = postDto.getPost();
        editedPost.setCategory(new HashSet<>());

        //get categories
        for(Integer catId : selectedIds){
            Category category = categoryService.getCategoryById(Long.valueOf(catId));
            editedPost.getCategory().add(category);
        }

        //get tags
        editedPost.setTag(new HashSet<>());
        for(Integer tagId : selectedTagIds){
            Tag tag = tagService.getTagById(Long.valueOf(tagId));
            editedPost.getTag().add(tag);
        }

        editedPost.setId(id);
        blogService.createOrUpdate(editedPost);

        return "redirect:/";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/tags")
    public String tags(Model model) {
        model.addAttribute("tags", tagService.findAll());
        return "tags";
    }

    @DeleteMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id){
        blogService.delete(id);
        return "redirect:/";
    }

    @DeleteMapping("/tags/delete/{id}")
    public String deleteTag(@PathVariable Long id){
        tagService.delete(id);
        return "redirect:/";
    }

    @DeleteMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable Long id){
        categoryService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/tags/new")
    public String createTag(Model model){
        Tag newTag = new Tag();
        model.addAttribute("newtag", newTag);
        return "newtag";
    }

    @GetMapping("/tags/update/{id}")
    public String updateTag(@PathVariable Long id, Model model){
        Tag tagById = tagService.getTagById(id);
        model.addAttribute("edittag", tagById);
        return "update-tag";
    }


    @PostMapping("/tags/update/{id}")
    public String updateTag(@PathVariable Long id, @ModelAttribute("edittag") Tag newTag){
        newTag.setId(id);
        tagService.createOrUpdate(newTag);

        return "redirect:/";
    }

    @GetMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable Long id, Model model){
        Category categoryById = categoryService.getCategoryById(id);
        model.addAttribute("editcategory", categoryById);
        return "update-category";
    }

    @PostMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute("editcategory") Category editcategory){
        editcategory.setId(id);
        categoryService.createCategory(editcategory);
        return "redirect:/";
    }

    @PostMapping("/tags/new")
    public String createTag(@ModelAttribute("newtag") Tag tag, Model model){
        tagService.createOrUpdate(tag);
        return  "redirect:/";
    }

    @GetMapping("/categories/new")
    public String createCategory(Model model){
        Category newCategory = new Category();
        model.addAttribute("newcategory", newCategory);
        return "newcategory";
    }

    @PostMapping("/categories/new")
    public String createCategory(@ModelAttribute("newcategory") Category category, Model model){
        categoryService.createCategory(category);
        return  "redirect:/";
    }

    @GetMapping("/post/new")
    public String createPost(Model model){

        PostDto newPostDto = new PostDto();
        Iterable<Category> allCategories = categoryService.getAllCategories();
        Iterable<Tag> allTags = tagService.getAllTags();
        LinkedList<CategorySelected> categorySelected = new LinkedList<>();
        Map<Category, Boolean> currentCategories = new HashMap<>();

        LinkedList<TagSelected> tagsSelected = new LinkedList<>();
        Map<Tag,Boolean> currentTags = new HashMap<>();

        for (Category category : allCategories) {
            categorySelected.add(new CategorySelected(category.getId(), false));
            currentCategories.put(category, false);
        }

        for(Tag tag : allTags) {
            tagsSelected.add(new TagSelected(tag.getId(), false));
            currentTags.put(tag, false);
        }

        newPostDto.setCurrentCategories(currentCategories);
        newPostDto.setSelectedCategories(categorySelected);
        newPostDto.setCurrentTags(currentTags);
        newPostDto.setSelectedTags(tagsSelected);

        model.addAttribute("newpostdto", newPostDto);

        return "newpost";
    }

    @PostMapping(value = "/post/new", consumes = MediaType.ALL_VALUE)
    public String createPost(@ModelAttribute("newpostdto") PostDto newpostdto,
                             @RequestParam(required = false, defaultValue = "") Integer[] selectedIds,
                            @RequestParam (required = false, defaultValue = "") Integer[] selectedTagIds,
        Model model)  {

        BlogPost newpost = newpostdto.getPost();
        newpost.setCategory(new HashSet<>());
        newpost.setTag(new HashSet<>());

        //get categories
        for(Integer id : selectedIds){
            Category category = categoryService.getCategoryById(Long.valueOf(id));
            newpost.getCategory().add(category);
        }

        //get tags
        for(Integer id : selectedTagIds){
            Tag tag = tagService.getTagById(id);
            newpost.getTag().add(tag);
        }

        blogService.createOrUpdate(newpost);

        return  "redirect:/";
    }
}
