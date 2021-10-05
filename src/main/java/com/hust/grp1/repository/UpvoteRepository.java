package com.hust.grp1.repository;

import com.hust.grp1.entity.Upvote;
import com.hust.grp1.entity.id.UpvoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UpvoteRepository extends JpaRepository<Upvote, UpvoteId> {

    @Query("SELECT SUM(u.vote) FROM Upvote u WHERE u.item.id = :itemId GROUP BY u.item")
    Integer countItemUpvote(int itemId);

    Upvote findByItem_IdAndUpvoteUser_Username(int itemId, String username);
}
