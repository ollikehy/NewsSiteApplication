package app.controller;

import static junit.framework.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void statusOk() throws Exception {
        mockMvc.perform(get("/news")).andExpect(status().isOk());
        mockMvc.perform(get("/news/trending")).andExpect(status().isOk());
        mockMvc.perform(get("/news/categories")).andExpect(status().isOk());
    }

    @Test
    public void frontpageTest() throws Exception {
        MvcResult res = mockMvc.perform(get("/news")).andReturn();
        String content = res.getResponse().getContentAsString();
        assertTrue(content.contains("Latest five:"));
        mockMvc.perform(get("/news")).andExpect(model().attributeExists("news"));
        mockMvc.perform(get("/news")).andExpect(model().attributeExists("imageIds"));
    }

    @Test
    public void trendingTest() throws Exception {
        MvcResult res = mockMvc.perform(get("/news/trending")).andReturn();
        String content = res.getResponse().getContentAsString();
        assertTrue(content.contains("Top five trending news:"));
        mockMvc.perform(get("/news/trending")).andExpect(model().attributeExists("news"));

    }
    @Test
    public void categoriesTest() throws Exception {
        MvcResult res = mockMvc.perform(get("/news/categories")).andReturn();
        String content = res.getResponse().getContentAsString();
        assertTrue(content.contains("List of categories:"));
        mockMvc.perform(get("/news/categories")).andExpect(model().attributeExists("categories"));
    }
}
