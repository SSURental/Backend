package com.example.SSU_Rental.item;


import com.example.SSU_Rental.common.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {


    Page<Item> findByItem_statusAndAndItem_group(ItemStatus status, Group group, Pageable pageable);

}