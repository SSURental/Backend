package com.example.SSU_Rental.image;

import com.example.SSU_Rental.item.Item;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemImage {

    @Id
    @GeneratedValue
    @Column(name = "itemimage_id")
    private Long id;

    @Column(name = "itemimage_uuid")
    private String uuid;

    @Column(name = "itemimage_name")
    private String imgName;

    @Column(name = "itemimage_path")
    private String path;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;




    @Builder
    public ItemImage(Long id, String uuid, String imgName, String path,Item item) {
        this.id = id;
        this.uuid = uuid;
        this.imgName = imgName;
        this.path = path;
        this.item = item;
    }


    public void addItem(Item item) {
        this.item = item;
    }
}
