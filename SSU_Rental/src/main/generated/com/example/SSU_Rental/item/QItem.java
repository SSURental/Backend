package com.example.SSU_Rental.item;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = 527424863L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final com.example.SSU_Rental.common.QBaseEntity _super = new com.example.SSU_Rental.common.QBaseEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final EnumPath<com.example.SSU_Rental.common.Group> itemGroup = createEnum("itemGroup", com.example.SSU_Rental.common.Group.class);

    public final ListPath<com.example.SSU_Rental.image.ItemImage, com.example.SSU_Rental.image.QItemImage> itemImages = this.<com.example.SSU_Rental.image.ItemImage, com.example.SSU_Rental.image.QItemImage>createList("itemImages", com.example.SSU_Rental.image.ItemImage.class, com.example.SSU_Rental.image.QItemImage.class, PathInits.DIRECT2);

    public final StringPath itemName = createString("itemName");

    public final com.example.SSU_Rental.member.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final EnumPath<ItemStatus> status = createEnum("status", ItemStatus.class);

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.SSU_Rental.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

