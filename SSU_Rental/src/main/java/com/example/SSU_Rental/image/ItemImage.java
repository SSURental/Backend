package com.example.SSU_Rental.image;
import com.example.SSU_Rental.item.Item;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @Column(name = "Itemimage_id")
    private Long id;

    private String imgName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    @Builder
    public ItemImage(Long id, String imgName, Item item) {
        this.id = id;
        this.imgName = imgName;
        this.item = item;
    }

    public void addItem(Item item) {
        this.item = item;
    }
}
