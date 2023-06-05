package com.example.backend2.repository;

import com.example.backend2.db.Monument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonumentRepository extends MongoRepository<Monument, String> {

    @Query("{'$and': [{ 'fields.commune':  ?0}, {'fields.siecle':  ?1}]}")
    public List<Monument> findByCommuneAndBySiecle(String commune, String siecle);
}
