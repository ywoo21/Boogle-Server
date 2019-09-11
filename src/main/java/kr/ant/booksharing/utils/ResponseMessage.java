package kr.ant.booksharing.utils;

public class ResponseMessage {
    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";

    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String ALREADY_USER = "이미 존재하는 Email입니다.";
    public static final String USABLE_USER = "사용 가능한 Email입니다.";

    public static final String CREATED_USER = "회원 가입 성공";
    public static final String FAIL_CREATE_USER = "회원 가입 실패";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String FAIL_UPDATE_USER = "회원 정보 수정 실패";
    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String CHANGED_PWD = "비밀번호 변경 성공";
    //public static final String FAILED_TO_CHANGE_PWD = "비밀번호 변경 실패";
    public static final String OLD_PWD_IS_WRONG = "현재 비밀번호가 잘못 되었습니다.";
    public static final String PWD_CORRECT = "비밀번호가 일치합니다.";
    public static final String NOT_SAME_PWD = "두 비밀번호가 일치하지 않습니다.";


    public static final String UNQUALIFIED = "자격 없음";


    public static final String READ_ALL_SELL_ITEMS = "모든 판매 도서 조회 성공";
    public static final String READ_SELL_ITEM = "판매 도서 조회 성공";
    public static final String NOT_FOUND_SELL_ITEM = "판매 도서가 존재하지 않습니다.";
    public static final String READ_BUY_ITEM = "구매 도서 조회 성공";
    public static final String NOT_FOUND_BUY_ITEM = "구매 도서가 존재하지 않습니다.";

    public static final String READ_ALL_SEARCHED_SELL_ITEMS = "판매 도서 검색 성공";
    public static final String NOT_FOUND_SEARCHED_SELL_ITEM = "검색한 판매 도서가 없습니다.";

    public static final String CREATE_SELL_ITEM = "판매 등록 성공";
    public static final String FAIL_CREATE_SELL_ITEM = "판매 등록 실패";

    public static final String CREATE_BUY_ITEM = "도서 구매 확정 성공";
    public static final String FAIL_CREATE_BUY_ITEM = "도서 구매 확정 실패";

    public static final String UPDATE_BUY_ITEM = "구매 도서 상태 변경 성공";
    public static final String FAIL_UPDATE_BUY_ITEM = "구매 도서 상태 변경 실패";

    public static final String UPDATE_SELL_ITEM = "판매 도서 상태 변경 성공";
    public static final String FAIL_UPDATE_SELL_ITEM = "판매 도서 상태 변경 실패";

    public static final String DELETE_SELL_ITEM = "판매 도서 삭제 성공";
    public static final String FAIL_DELETE_SELL_ITEM = "판매 도서 삭제 실패";



    public static final String AUTHORIZED = "인증 성공";
    public static final String UNAUTHORIZED = "인증 실패";
    public static final String FORBIDDEN = "인가 실패";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String SERVICE_UNAVAILABLE = "현재 서비스를 사용하실 수 없습니다. 잠시후 다시 시도해 주세요.";

    public static final String DB_ERROR = "데이터베이스 에러";
}
