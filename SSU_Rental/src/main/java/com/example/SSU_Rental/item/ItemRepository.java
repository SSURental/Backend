package com.example.SSU_Rental.item;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>,ItemRepositoryCustom{

}