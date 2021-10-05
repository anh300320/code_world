package com.hust.grp1.repository;

import com.hust.grp1.entity.Bookmark;
import com.hust.grp1.entity.id.BookmarkId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {

    Optional<Bookmark> findByPostedItem_IdAndUser_Username(int itemId, String username);

    Page<Bookmark> findAllByUser_Username(String username, Pageable pageable);
    long countDistinctByUser_Id(int useId);
}
