package com.andersonmarques.debts_api.repositories;

import java.util.Optional;

import com.andersonmarques.debts_api.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByEmail(String email);

}