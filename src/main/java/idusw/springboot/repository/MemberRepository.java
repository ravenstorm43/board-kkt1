package idusw.springboot.repository;


import idusw.springboot.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>,
        QuerydslPredicateExecutor<MemberEntity> {
    // JPQL
    @Query("select m from MemberEntity m where m.email = :email and m.pw = :pw")
    MemberEntity getByEmailPw(@Param("email") String email, @Param("pw") String pw);
    // select * from member_b201912065 where email = ? and pw = ?
    // javax.sql.* 질의문을 실행하는 객체, statement object (Statement, PreparedStatement, CallableStatement)
    // pstmt.setString(1, "egyou@induk.ac.kr");
    // pstmt.setString(2, "cometrue");
}
