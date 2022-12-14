package com.example.SSU_Rental.item;


import com.example.SSU_Rental.common.Group;
import com.example.SSU_Rental.member.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {


    @EntityGraph(attributePaths = {"member"},type = EntityGraphType.LOAD)
    @Query("select i,ti from Item i left outer join ItemImage ti on ti.item = i where i.status =:status and i.itemGroup =:itemGroup group by i")
    Page<Object[]> getListPage(@Param("status") ItemStatus status,@Param("itemGroup") Group itemGroup, Pageable pageable);


    @EntityGraph(attributePaths = {"member"},type = EntityGraphType.LOAD)
    @Query("select i,ti from Item i left outer join ItemImage ti on ti.item = i where i.member =:member group by i")
    Page<Object[]> findByMember(@Param("member")Member member, Pageable pageable);

    @EntityGraph(attributePaths = {"member"},type = EntityGraphType.LOAD)
    @Query("select i,ti from Item i left outer join ItemImage ti on ti.item = i where i.id =:item_id")
    List<Object[]> getItemWithImage(@Param("item_id")Long item_id);

}