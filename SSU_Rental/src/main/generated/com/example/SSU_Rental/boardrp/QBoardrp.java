package com.example.SSU_Rental.boardrp;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardrp is a Querydsl query type for Boardrp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardrp extends EntityPathBase<Boardrp> {

    private static final long serialVersionUID = 1186336427L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardrp boardrp = new QBoardrp("boardrp");

    public final com.example.SSU_Rental.common.QBaseEntity _super = new com.example.SSU_Rental.common.QBaseEntity(this);

    public final com.example.SSU_Rental.board.QBoard board;

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final com.example.SSU_Rental.member.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public QBoardrp(String variable) {
        this(Boardrp.class, forVariable(variable), INITS);
    }

    public QBoardrp(Path<? extends Boardrp> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardrp(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardrp(PathMetadata metadata, PathInits inits) {
        this(Boardrp.class, metadata, inits);
    }

    public QBoardrp(Class<? extends Boardrp> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.example.SSU_Rental.board.QBoard(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new com.example.SSU_Rental.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

