package com.example.AEsportsmerchandise.repository;

import com.example.AEsportsmerchandise.entity.CartEntity;
import com.example.AEsportsmerchandise.entity.CartItemEntity;
import com.example.AEsportsmerchandise.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

     Optional<CartEntity> findByUser(UserEntity user);


}

