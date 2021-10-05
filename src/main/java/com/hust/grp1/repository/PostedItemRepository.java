package com.hust.grp1.repository;

import com.hust.grp1.entity.PostedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostedItemRepository extends JpaRepository<PostedItem, Integer> {
    @Query("SELECT DISTINCT p FROM PostedItem p WHERE (LOWER(p.title) LIKE LOWER(:searchKey) OR LOWER(p.content) LIKE LOWER(:s)) AND p.parent = null")
    Page<PostedItem> findByContentLikeIgnoreCaseOrTitleLikeIgnoreCaseAndParentIsNull(String searchKey,String s, Pageable pageable);

    @Query("SELECT DISTINCT p " +
            "FROM PostedItem p " +
            "JOIN ItemTag t ON t.item.id = p.id " +
            "WHERE t.tag IN (:tags) " +
            "AND p.id <> :exceptId " +
            "AND p.parent IS NULL " +
            "ORDER BY p.updatedDate DESC ")
    List<PostedItem> findRelatedPost(int exceptId, List<String> tags, Pageable pageable);
    Page<PostedItem> findByParentIsNull(Pageable pageable);
    Page<PostedItem> findByParentIsNullAndOwner_Username(String username, Pageable pageable);
    Page<PostedItem> findByParentNotNullAndOwner_Username(String username, Pageable pageable);
    void deleteById(int id);
}
