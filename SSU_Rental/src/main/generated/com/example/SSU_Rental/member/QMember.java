package com.example.SSU_Rental.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1270025279L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath loginId = createString("loginId");

    public final EnumPath<com.example.SSU_Rental.common.Group> memberGroup = createEnum("memberGroup", com.example.SSU_Rental.common.Group.class);

    public final com.example.SSU_Rental.image.QMemberImage memberImage;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final ListPath<com.example.SSU_Rental.login.Session, com.example.SSU_Rental.login.QSession> sessionList = this.<com.example.SSU_Rental.login.Session, com.example.SSU_Rental.login.QSession>createList("sessionList", com.example.SSU_Rental.login.Session.class, com.example.SSU_Rental.login.QSession.class, PathInits.DIRECT2);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberImage = inits.isInitialized("memberImage") ? new com.example.SSU_Rental.image.QMemberImage(forProperty("memberImage")) : null;
    }

}

