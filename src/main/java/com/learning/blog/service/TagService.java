package com.learning.blog.service;

import com.learning.blog.entity.BlogPost;
import com.learning.blog.entity.Tag;
import com.learning.blog.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private BlogService blogService;

    Logger logger = LoggerFactory.getLogger(TagService.class);

    public Iterable<Tag> findAll() {
        return tagRepository.findAll();
    }

    public void createOrUpdate(Tag tag) {
        tagRepository.save(tag);
    }

    public void delete(Long id) {
        //find if any associations before deleting
        tagRepository.findById(id).ifPresent(tag -> {
            for(BlogPost post: blogService.findAll()){
                if(post.getTag().contains(tag)){
                    post.getTag().remove(tag); //remove association
                    logger.info("Tag has been removed from post: " +post.getTitle());
                }
            }
            tagRepository.deleteById(id);
            logger.info("Tag " + tag.getTitle() + " has been deleted");
        });
    }

    public Tag getTagById(long tagid) {
        return tagRepository.findById(tagid).get();
    }

    public Iterable<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}
