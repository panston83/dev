package com.example.dev.endtity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoxOfficeRepository extends
        JpaRepository<Boxoffice,Long>, QuerydslPredicateExecutor {
}
