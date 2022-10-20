package com.app.service;

import com.app.exceptions.AuthException;
import com.app.exceptions.NotFoundException;
import com.app.model.Bookmark;
import com.app.model.User;
import com.app.repository.BookmarkRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;


@Service
@AllArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepo bookmarkRepo;
    private final AuthService authService;

    public void saveNote(Bookmark bookmarkItem, Principal principal) {
        User currentUser = authService.findLoggedInUser(principal);
        Bookmark bookmark = new Bookmark(bookmarkItem.getLink(), bookmarkItem.getName(), bookmarkItem.isShared(), currentUser);

        bookmarkRepo.save(bookmark);
    }

    @Transactional(readOnly = true)
    public List<Bookmark> getAllBookmarks(boolean showAll, Principal principal) {
        if (showAll) {
            return bookmarkRepo.findBySharedTrue();
        }
        return authService.findLoggedInUser(principal).getBookmarks();
    }


    public void deleteBookmark(Long id, Principal principal) {
        boolean bookmarkExists = bookmarkRepo.existsById(id);
        User currentUser = authService.findLoggedInUser(principal);
        if (!bookmarkExists) {
            throw new NotFoundException("Not found");
        }
        Bookmark bookmarkFromDb = bookmarkRepo.findById(id).orElseThrow();

        if (bookmarkFromDb.getUser() != currentUser) {
            throw new AuthException("Unable to do it");
        }

        bookmarkRepo.deleteById(id);
    }
}
