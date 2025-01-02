package com.tui.proof.infrastructure.database;

import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tui.proof.domain.model.PilotesOrder;

public interface PilotesOrderRepository extends JpaRepository<PilotesOrder, UUID> {
    //this query can have big duration, because of no index. In normal scenarios with pagination won't have a problem
    @Query("""
        SELECT o FROM PilotesOrder o
        WHERE LOWER(o.client.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(o.client.lastName)  LIKE LOWER(CONCAT('%', :query, '%'))
        """)
    Page<PilotesOrder> searchByName(@Param("query") String query, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PilotesOrder p WHERE p.id = :id")
    Optional<PilotesOrder> findByIdForUpdate(@Param("id") UUID id);
}
