package com.ex01.basic.repository;

import com.ex01.basic.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemRepository extends JpaRepository<MemberEntity, Integer> {
    boolean existsByUsername(String username);
    Optional<MemberEntity> findByUsername(String username);
}
