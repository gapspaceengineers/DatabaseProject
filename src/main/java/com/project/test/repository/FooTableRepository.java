package com.project.test.repository;

import com.project.test.domain.FooTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FooTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FooTableRepository extends JpaRepository<FooTable, Long> {}
