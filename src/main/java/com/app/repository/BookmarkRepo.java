package com.app.repository;

import com.app.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepo extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findBySharedTrue();
}
