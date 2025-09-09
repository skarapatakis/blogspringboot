package com.learning.blog;

import com.learning.blog.controller.BlogController;
import com.learning.blog.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogApplicationTests {

    @Autowired
    private BlogController blogController;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(blogController).isNotNull();
    }

    @Test
    void loads_posts_home_page(@Autowired BlogService blogService) {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).isNotNull();
        assertThat(blogService.findById(1L)).isNotNull();
        assertThat(blogService.findById(1L).getTitle()).isEqualTo("Running your first 5K");
        assertThat(blogService.findById(2L).getTitle()).isEqualTo("Dynamic Post");
        assertThat(blogService.findById(3L).getTitle()).isEqualTo("Running your first marathon");
    }

}
