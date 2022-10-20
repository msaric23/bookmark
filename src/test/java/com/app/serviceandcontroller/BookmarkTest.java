package com.app.serviceandcontroller;

import com.app.repository.BookmarkRepo;
import com.app.service.AuthService;
import com.app.service.BookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookmarkTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookmarkRepo bookmarkRepo;

    @Mock
    private AuthService authService;
    private BookmarkService bookmarkService;

    @BeforeEach
    void setup(){
        bookmarkService = new BookmarkService(bookmarkRepo, authService);
    }

    @Test
    void unauthenticatedUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bookmark"))
                .andExpect(status().isForbidden())
                .andDo(
                        mvcResult -> mockMvc.perform(post("/api/bookmark/add")).andExpect(status().isForbidden())
                )
                .andDo(
                        mvcResult -> mockMvc.perform(delete("/api/note/bookmark/{id}", 1)).andExpect(status().isForbidden())
                );
    }

    @Test
    void getAllBookmarks() {
        bookmarkService.getAllBookmarks(true, new Principal() {
            @Override
            public String getName() {
                return null;
            }
        });
        verify(bookmarkRepo).findAll();
    }

    @Test
    void addBookmark() {
    }
}