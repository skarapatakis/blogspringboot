package com.learning.blog.service;

import com.learning.blog.entity.BlogPost;
import com.learning.blog.entity.Category;
import com.learning.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BlogService blogRepository;

    @Autowired
    private CategoryService(CategoryRepository categoryRepository, BlogService blogRepository) {
        this.categoryRepository = categoryRepository;
        this.blogRepository = blogRepository;
    }

    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {

        categoryRepository.findById(id).ifPresent(category -> {
            for(BlogPost post: blogRepository.findAll()){
                post.getCategory().remove(category);

            };
            categoryRepository.deleteById(id);
        });
    }
}
