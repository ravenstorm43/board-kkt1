package idusw.springboot.service;

import idusw.springboot.domain.Board;

import java.util.List;

public interface BoardService {
    int registerBoard(Board board);
    Board findBoardById(Board board); // 게시물의 id(유일한 식별자) - 즉, bno로 조회
    List<Board> findBoardAll(); // 게시물 목록 출력 (페이지 처리나 정렬, 필터)
    int updateBoard(Board board); // 게시물 정보 필요
    int deleteBoard(Board board); // 게시물 ID값만 필요
}
