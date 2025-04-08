package com.shalom.shipping_status.repository;

import com.shalom.shipping_status.document.ShipStatusDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ShipStatusRepository extends ReactiveMongoRepository<ShipStatusDocument, String> {
    @Query("{ 'complete': false, 'email': { $ne: null } }")
    Flux<ShipStatusDocument> findIncompleteWithEmail();
}
