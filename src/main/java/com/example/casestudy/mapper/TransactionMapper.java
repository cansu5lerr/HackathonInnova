package com.example.casestudy.mapper;

import com.example.casestudy.dto.response.TransactionResponse;
import com.example.casestudy.entity.Transaction;
import com.example.casestudy.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    @Mapping(source = "transaction.id", target = "id")
    @Mapping(source = "transaction.amount", target = "amount")
    @Mapping(source = "transaction.categoryName", target = "categoryName")
    @Mapping(source = "transaction.createdAt", target = "date")
    @Mapping(source = "user.username", target = "username")
    TransactionResponse toTransactionResponse(Transaction transaction, User user);
}