package com.example.SSU_Rental.item;


import com.example.SSU_Rental.common.Group;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {


    @EntityGraph(attributePaths = {"item_owner"},type = EntityGraphType.LOAD)
    @Query("select i,ti from Item i left outer join ItemImage ti on ti.item = i where i.item_status =:status and i.item_group =:group")
    Page<Object[]> findByItem_statusAndAndItem_group(@Param("status") ItemStatus status,@Param("group") Group group, Pageable pageable);


    @EntityGraph(attributePaths = {"item_owner"},type = EntityGraphType.LOAD)
    @Query("select i,ti from Item i left outer join ItemImage ti on ti.item = i where i.id =:item_id")
    List<Object[]> getItemWithImage(@Param("item_id")Long item_id);

}