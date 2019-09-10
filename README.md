# Boogle-Server
Boogle Server App Repository

# API

## USERS

| 메소드 | 경로                                      | 설명                      |      |
| ------ | ----------------------------------------- | ------------------------- | ---- |
| POST   | /users/signup                             | 회원가입                  |      |
| GET    | /users/signup/validateEmail?email={email} | 회원가입 이메일 중복 체크 |      |
| POST   | /users/signin                             | 로그인                    |      |

## LISTS

| 메소드 | 경로                              | 설명                       |      |
| ------ | --------------------------------- | -------------------------- | ---- |
| GET    | /lists                            | 모든 판매 도서 목록 조회   |      |
| GET    | /lists/searches?keyword={keyword} | 검색된 판매 도서 목록 조회 |      |
| GET    | /lists/adv?bookId={bookId}        | 판매 도서 상세 조회        |      |
| POST   | /lists/sell                       | 판매 도서 등록             |      |
| POST   | /lists/buy                        | 도서 구매 확정             |      |

## MY PAGE

| 메소드 | 경로                            | 설명                             |                    |
| ------ | ------------------------------- | -------------------------------- | ------------------ |
| GET    | /myPage/buyList?email={email}   | 마이페이지 구매 목록 조회        |                    |
| GET    | /myPage/sellList?email={email}  | 마이페이지 판매 목록 조회        |                    |
| POST   | /myPage/buyList/updateState     | 마이페이지 구매 상태 변경        | state 1 -> state 2 |
| DELETE | /myPage/buyList?bookId={bookId} | 마이페이지 등록된 판매 도서 삭제 |                    |
