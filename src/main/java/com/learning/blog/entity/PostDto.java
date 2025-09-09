package com.learning.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto implements Serializable {
    private BlogPost post;
    private List<CategorySelected> selectedCategories;
    private Map<Category, Boolean> currentCategories;
    //add map for tags
    private List<TagSelected> selectedTags;
    private Map<Tag, Boolean> currentTags;

}
