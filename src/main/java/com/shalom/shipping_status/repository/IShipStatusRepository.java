package com.shalom.shipping_status.repository;

import com.shalom.shipping_status.document.ShipStatusDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShipStatusRepository extends ReactiveMongoRepository<ShipStatusDocument, String> {
}
