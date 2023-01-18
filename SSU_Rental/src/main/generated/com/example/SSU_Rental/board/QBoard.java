package com.example.SSU_Rental.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -648836689L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final com.example.SSU_Rental.common.QBaseEntity _super = new com.example.SSU_Rental.common.QBaseEntity(this);

    public final BooleanPath blocked = createBoolean("blocked");

    public final ListPath<com.example.SSU_Rental.boardrp.Boardrp, com.example.SSU_Rental.boardrp.QBoardrp> boardrpList = this.<com.example.SSU_Rental.boardrp.Boardrp, com.example.SSU_Rental.boardrp.QBoardrp>createList("boardrpList", com.example.SSU_Rental.boardrp.Boardrp.class, com.example.SSU_Rental.boardrp.QBoardrp.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final NumberPath<Integer> dislikes = createNumber("dislikes", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final com.example.SSU_Rental.member.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath title = createString("title");

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public final NumberPath<Integer> warns = createNumber("warns", Integer.class);

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.SSU_Rental.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

