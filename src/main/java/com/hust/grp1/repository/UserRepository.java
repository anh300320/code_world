package com.hust.grp1.repository;

import com.hust.grp1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findOneByUsername(String username);

    boolean existsByUsername(String username);
}
