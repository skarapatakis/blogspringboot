package com.learning.blog.service;

import com.learning.blog.entity.BlogPost;
import com.learning.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogService {

    private BlogRepository blogRepository;

    @Autowired
    public void setBlogRepository(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public Iterable<BlogPost> findAll() {
        return blogRepository.findAll();
    }

    public BlogPost findById(Long id) {
        return blogRepository.findById(id).get();
    }

    public void createOrUpdate(BlogPost todoList) {
        blogRepository.save(todoList);
    }

    public void delete(Long id) {
        blogRepository.deleteById(id);
    }
}
