package com.project.user.management.repository;

import com.project.user.management.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(" FROM User u where username = ?1")
    User getByUsername(String username);

    @Query("SELECT ud " +
    "FROM User ud " +
    "WHERE ud.isDelete = 0 " +
    "AND ud.isActive = :isActive ")
    Page<User> findUserByStatus(@Param("isActive") Integer isActive, Pageable pageable);

    @Query("SELECT ud " +
    "FROM User ud ")
    Page<User> findAllUser(Pageable pageable);
    
}