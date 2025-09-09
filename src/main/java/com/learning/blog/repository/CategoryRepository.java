package com.learning.blog.repository;

import com.learning.blog.entity.BlogPost;
import com.learning.blog.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

}
