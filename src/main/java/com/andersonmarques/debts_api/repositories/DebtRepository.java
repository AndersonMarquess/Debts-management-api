package com.andersonmarques.debts_api.repositories;

import com.andersonmarques.debts_api.models.Debt;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends MongoRepository<Debt, String>, QueryByExampleExecutor<Debt> {

}