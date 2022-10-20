package com.app.controller;

import com.app.model.Bookmark;
import com.app.service.BookmarkService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/bookmark")
@AllArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/add")
    public ResponseEntity<Bookmark> addBookmark(@RequestBody Bookmark bookmark, Principal principal) {
        bookmarkService.saveNote(bookmark, principal);
        return new ResponseEntity<>(bookmark, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Bookmark>> getAllBookmarks(@RequestParam(required = false, defaultValue = "true") boolean showAll, Principal principal) {
        return status(HttpStatus.OK).body(bookmarkService.getAllBookmarks(showAll, principal));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBookmark(@PathVariable Long id, Principal principal) {
        bookmarkService.deleteBookmark(id, principal);
        return new ResponseEntity<>("Bookmark deleted", HttpStatus.GONE);
    }
}
