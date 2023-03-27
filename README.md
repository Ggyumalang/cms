# cms api

## 프로젝트 개요
셀러와 고객 사이를 중개해 주는 이커머스 서버를 구축한다.

## 회원 서버

### 공통

- [x]  이메일을 통한 인증번호 발송을 통한 회원 가입

### 고객

- [x]  회원 가입
- [x]  인증 처리 ( 이메일 )
- [x]  로그인 토큰 발행
- [x]  로그인 토큰을 이용한 제어 확인 (JWT와 Filter를 이용해 간략하게)
- [ ]  예치금 관리

### 셀러

- [ ]  회원가입
- [ ]  매출 조회 (2차 구현 대상)
- [ ]  잔액 추가 (정산 전용) (2차 구현 대상)

## 주문 서버

### 셀러

- [ ]  상품 등록 & 수정
- [ ]  상품 삭제
- [ ]  매출 조회 (2차 구현 대상)
- [ ]  잔액 추가 (정산 전용) (2차 구현 대상)

### 고객

- [ ]  장바구니를 위한 Redis 연
- [ ]  상품 검색 & 상세 페이지
- [ ]  장바구니에 추가
- [ ]  장바구니 확인하기
- [ ]  주문하기
- [ ]  주문 내역 메일로 발송하기

## 사용 기술
- Spring
- Jpa
- Mysql
- Redis
- Docker
- AWS
