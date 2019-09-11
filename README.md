# Boogle-Server
Boogle Server App Repository

# API

## USERS

| 메소드 | 경로                                      | 설명                      |
| ------ | ----------------------------------------- | ------------------------- |
| POST   | /users/signup                             | 회원가입                  |
| GET    | /users/signup/validateEmail?email={email} | 회원가입 이메일 중복 체크 |
| POST   | /users/signin                             | 로그인                    |

## LISTS

| 메소드 | 경로                              | 설명                       |
| ------ | --------------------------------- | -------------------------- |
| GET    | /lists                            | 모든 판매 도서 목록 조회   |
| GET    | /lists/searches?keyword={keyword} | 검색된 판매 도서 목록 조회 |
| GET    | /lists/adv?bookId={bookId}        | 판매 도서 상세 조회        |
| POST   | /lists/sell                       | 판매 도서 등록             |
| POST   | /lists/buy                        | 도서 구매 확정             |

## MY PAGE

| 메소드 | 경로                            | 설명                             | 비고               |
| ------ | ------------------------------- | -------------------------------- | ------------------ |
| GET    | /myPage/buyList?email={email}   | 마이페이지 구매 목록 조회        |                    |
| GET    | /myPage/sellList?email={email}  | 마이페이지 판매 목록 조회        |                    |
| POST   | /myPage/buyList/updateState     | 마이페이지 구매 상태 변경        | state 1 -> state 2 |
| DELETE | /myPage/buyList?bookId={bookId} | 마이페이지 등록된 판매 도서 삭제 |                    |

## NAVER BOOK API

| 메소드 | 경로                             | 설명                                 |
| ------ | -------------------------------- | ------------------------------------ |
| GET    | /naver/bookApi?keyword={keyword} | 네이버 도서 검색 API 일반 검색(제목) |
| GET    | /naver/bookApi/adv?isbn={isbn}   | 네이버 도서 검색 API 상세 검색(ISBN) |

![북을_ERD](https://user-images.githubusercontent.com/23696493/64671045-2742f200-d4a2-11e9-863e-014acd1bde87.png)

