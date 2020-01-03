package kr.ant.booksharing.dao;

import kr.ant.booksharing.model.Book;
import kr.ant.booksharing.model.SignIn.SignInReq;
import kr.ant.booksharing.model.SignIn.SignInRes;
import kr.ant.booksharing.model.SignUp.SignUpReq;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    // 회원 정보 저장
    @Insert("INSERT INTO user(id, name, email, password, phone, major_1, major_2) VALUES(#{signUpReq.id}, " +
            "#{signUpReq.name}, #{signUpReq.email}, #{signUpReq.password}, " +
            "#{signUpReq.phone},#{signUpReq.major_1},#{signUpReq.major_2})")
    @Options(useGeneratedKeys = true, keyProperty = "signUpReq.id", keyColumn = "id")
    void saveUser(@Param("signUpReq") final SignUpReq signUpReq);

    // 회원 정보 조회
    @Select("SELECT * FROM user WHERE email = #{signInReq.email}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "name", column = "name"),
    })
    public SignInRes findUser(@Param("signInReq") final SignInReq signInReq);

    // 회원 이름으로 회원 id 조회
    @Select("SELECT * FROM user WHERE name = #{sellerName}")
    @Results(value = {
            @Result(property = "id", column = "id"),
    })
    public int findUserIdByUserName(@Param("sellerName") final String sellerName);

    // 회원 이메일로 회원 id 조회
    @Select("SELECT * FROM user WHERE email = #{email}")
    @Results(value = {
            @Result(property = "id", column = "id"),
    })
    public int findUserIdByUserEmail(@Param("email") final String email);

    // 이메일 중복 조회
    @Select("SELECT * FROM user WHERE email = #{email}")
    @Results(value = {
            @Result(property = "email", column = "email"),
    })
    public String checkEmail(@Param("email") final String email);

    // 회원 id로 회원 전화번호
    @Select("SELECT * FROM user WHERE id = #{id}")
    @Results(value = {
            @Result(property = "phone", column = "phone"),
    })
    public String findUserPhoneNumberByUserId(@Param("id") final int id);

    // 회원 id로 회원 전화번호
    @Update("UPDATE user SET password = #{password} WHERE email = #{email}")
    public void changeUserPassword(@Param("email") final String email, final String password);

}
