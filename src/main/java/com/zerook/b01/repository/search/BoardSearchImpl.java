package com.zerook.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.zerook.b01.domain.Board;
import com.zerook.b01.domain.QBoard;
import com.zerook.b01.domain.QReply;
import com.zerook.b01.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl(){
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {

        QBoard board = QBoard.board; //Q도메인 객체

        JPQLQuery<Board> query = from(board); //select ... from board

        //query.where(board.title.contains("1")); //where title like ...

        BooleanBuilder booleanBuilder = new BooleanBuilder(); //()

        booleanBuilder.or(board.title.contains("11")); // title like ...

        booleanBuilder.or(board.content.contains("11")); //content like ...

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L)); // bno > 0 ...

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch(); //쿼리 실행

        long count = query.fetchCount(); //count 쿼리

        return null;

//        select
//        b1_0.bno,
//                b1_0.content,
//                b1_0.moddate,
//                b1_0.regdate,
//                b1_0.title,
//                b1_0.writer
//        from
//        board b1_0
//        where
//                (
//                        b1_0.title like ? escape '!'
//                     or b1_0.content like ? escape '!'
//                )
//        and b1_0.bno>?
//        order by
//        b1_0.bno desc
//        limit
//                ?, ?

    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if((types != null &&  types.length > 0) && keyword != null){ //검색 조건과 키워드가 있다면

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types){

                switch(type){

                    case "t" :
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c" :
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w" :
                        booleanBuilder.or((board.writer.contains(keyword)));
                        break;

                }

            }
            query.where(booleanBuilder);
        }

        query.where(board.bno.gt(0L)); // bno > 0

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);

//        select
//        b1_0.bno,
//                b1_0.content,
//                b1_0.moddate,
//                b1_0.regdate,
//                b1_0.title,
//                b1_0.writer
//        from
//        board b1_0
//        where
//                (
//                        b1_0.title like ? escape '!'
//                     or b1_0.content like ? escape '!'
//                     or b1_0.writer like ? escape '!'
//                )
//        and b1_0.bno>?
//        order by
//          b1_0.bno desc
//        limit
//                ?, ?

    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReply(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));

        query.groupBy(board);

        if((types != null &&  types.length > 0) && keyword != null){

            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types){

                switch(type){

                    case "t" :
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c" :
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w" :
                        booleanBuilder.or((board.writer.contains(keyword)));
                        break;

                }

            }
            query.where(booleanBuilder);
        }

        query.where(board.bno.gt(0L)); // bno > 0

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class, board.bno, board.title, board.writer, board.regDate, reply.count().as("replyCount")));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);

    }

}
