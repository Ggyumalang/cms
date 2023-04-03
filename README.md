# cms api

## 프로젝트 개요
셀러와 고객 사이를 중개해 주는 이커머스 서버를 구축한다.

## ERD
<image src="images/ERD.png"/>

## 회원 서버

### 공통

- [x]  이메일을 통한 인증번호 발송을 통한 회원 가입

### 고객

- [x]  회원 가입
- [x]  인증 처리 ( 이메일 )
- [x]  로그인 토큰 발행
- [x]  로그인 토큰을 이용한 제어 확인 (JWT와 Filter를 이용해 간략하게)
- [x]  예치금 관리

### 셀러

- [x]  회원가입

## 주문 서버

### 셀러

- [x]  상품 등록 & 수정
- [x]  상품 삭제

### 고객

- [x]  장바구니를 위한 Redis 연동
- [x]  상품 검색 & 상세 페이지
- [x]  장바구니에 추가
- [x]  장바구니 확인하기
- [x]  장바구니 변경
- [x]  주문하기
- [x]  주문 내역 메일로 발송하기

---

## 1. 회원가입 API

- POST /signup/customer/seller or customer
- 파라미터 : 이메일,  이름 , 비밀번호 , 생년월 , 연락처
- 정책 : 이미 등록되어 있는 이메일이라면 실패응답
- 성공 응답 : 성공 메시지

### 상세 정보

- 저장이 필요한 정보

| 컬럼명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | pk
(Long) | primary key |
| email | String | 이메일 |
| password | String | 비밀번호 |
| name | String | 이름 |
| phone | String | 휴대폰번호(연락처) |
| balance | Long | 잔액 |
| verifyExpiredAt | LocalDateTime | 인증만료일시 |
| verificationCode | String | 인증코드 |
| verify | boolean | 인증여 |
| createdAt | LocalDateTime | 등록일시 |
| modifiedAt | LocalDateTime | 수정일시 |
- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "email" : "abc123@gmail.com", 
        "name" : "홍길동",
        "password" : "1234",
        "birth": "199X-0X-0X",
        "phone": "01011112222"
    }
    ```
    
    - 응답
    
    ```java
    "회원 가입에 성공하였습니다."
    ```
    

## 2. 로그인 API

- POST /member/signin/seller or customer
- 파라미터 : 이메일,  비밀번호
- 정책 : 등록되지 않은 이메일 , 비밀번호가 일치하지 않을 시 실패응답
- 성공 응답 : 토큰

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "email" : "xxxxx@hanmail.net",
        "password" : "1234"
    }
    ```
    
    - 응답
    
    ```java
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCY0ROWnZCMVhCZ3d2QVJBUjhOYUlzOG9Uc2JSR0NCd0VLOGlCRUdsZVFVPSIsImp0aSI6IlN1YkhlS1VSUnJlUVkrV1NPbkJrdGc9PSIsInJvbGVzIjoiQ1VTVE9NRVIiLCJpYXQiOjE2ODA0ODY3MTYsImV4cCI6MTY4MDU3MzExNn0.20b511-6Qoew34lkuMW2now_Mt-XprYdznSSGy5dnXw"
    ```
    

## 3. 회원정보 조회 API

- GET /customer or seller/getInfo
- 파라미터 : 토큰
- 정책 : 등록되지 않은 이메일 실패응답
- 성공 응답 : CustomerDto(회원id, 이메일, 이름, 잔액)

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    GET /customer OR seller/getInfo
    ```
    
    - 응답
    
    ```java
    {
        "id": 2,
        "email": "abcd@gmail.com",
        "name": "홍길",
        "balance": 81000
    }
    ```
    

## 4. 잔액 변경 API

- POST /customer/balance
- 파라미터 : 송신자 , 메시지 , 변경 금액
- 정책 : 등록되지 않은 이메일,  현재 잔액 + 변경 금액이 -보다 작은 경우 실패응
- 성공 응답 : 변경 후 잔액

### 상세 정보

- 저장이 필요한 정보

| 컬럼명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | pk
(Long) | primary key |
| customerId | Long | 회원테이블과 고객잔액내역 테이블은 1:N 관계이다. |
| changedMoney | Long | 변경금액 |
| currentMoney | Long | 변경 후 잔액 |
| fromMessage | String | 송신자 |
| description | String | 잔액 변경 메시지 |
| createdAt | LocalDateTime | 등록일시 |
| modifiedAt | LocalDateTime | 수정일시 |
- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "from" : "admin",
        "message" : "test",
        "money" : 50000
    }
    ```
    
    - 응답
    
    ```java
    131000
    ```
    

## 5. 상품 등록 API

- POST /seller/product
- 파라미터 : 상품명, 상품 설명, 상품의 옵션(item) 정보
- 성공 응답 : 상품 id, 상품명, 상품 설명, 상품의 옵션(item) 정보

### 상세 정보

- 저장이 필요한 정보

| 컬럼명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | pk
(Long) | primary key |
| sellerId | Long | 판매자테이블과 상품 테이블은 1:N 관계이다. |
| name | String | 상품명 |
| description | String | 상품 설명 |
| createdAt | LocalDateTime | 등록일시 |
| modifiedAt | LocalDateTime | 수정일시 |
- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "name" : "nike",
        "description" : "shoes",
        "items" : [
            {
                "count" : 1,
                "name" : "item",
                "price" : 1000
            }
        ]
    }
    ```
    
    - 응답
    
    ```java
    {
        "id": 5,
        "name": "nike",
        "description": "shoes",
        "items": [
            {
                "id": 10,
                "name": "item",
                "price": 1000,
                "count": 1
            }
        ]
    }
    ```
    

## 6. 상품 옵션 아이템 등록 API

- POST /seller/product/item
- 파라미터 : 상품id, 옵션 아이템 명, 옵션 아이템 수 , 가격
- 정책 : 상품이 없을 경우, 똑같은 아이템 명일 경우 실패 응답
- 성공 응답 : 상품 id, 상품명, 상품설명, 상품의 옵션(item) 정보

### 상세 정보

- 저장이 필요한 정보

| 컬럼명 | 데이터 타입 | 설명 |
| --- | --- | --- |
| id | pk
(Long) | primary key |
| sellerId | Long | 상품 테이블과 상품 아이템 테이블은 1:N 관계이다. |
| productId | Long | 상품테이블과 상품 아이템 테이블은 1:N 관계이다. |
| name | String | 상품 아이템 명 |
| price | Long | 상품 아이템 가격 |
| count | Long | 상품 아이템 수 |
| createdAt | LocalDateTime | 등록일시 |
| modifiedAt | LocalDateTime | 수정일시 |
- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "productId" : 4,
        "name" : "nike2",
        "count" : 4,
        "price" : 1500
    }
    ```
    
    - 응답
    
    ```java
    {
        "id": 4,
        "name": "nike",
        "description": "shoes",
        "items": [
            {
                "id": 8,
                "name": "item",
                "price": 3000,
                "count": 5
            },
            {
                "id": 9,
                "name": "nike",
                "price": 1000,
                "count": 9
            },
            {
                "id": 11,
                "name": "nike2",
                "price": 1500,
                "count": 4
            }
        ]
    }
    ```
    

## 7. 상품 수정 API

- PUT /seller/product
- 파라미터 : 상품id, 상품명, 상품 설명, 상품의 옵션(item) 정보
- 정책 : 상품이 없을 경우, 상품 아이템이 없을 경우 실패응답
- 성공 응답 : 상품 id, 상품명, 상품 설명, 상품의 옵션(item) 정보

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "productId" : 4,
        "name" : "nike",
        "description" : "shoes",
        "items" : [
            {
                "productItemId" : 8,
                "count" : 10,
                "name" : "item",
                "price" : 3000
            }
        ]
    }
    ```
    
    - 응답
    
    ```java
    {
        "id": 4,
        "name": "nike",
        "description": "shoes",
        "items": [
            {
                "id": 8,
                "name": "item",
                "price": 3000,
                "count": 10
            },
            {
                "id": 9,
                "name": "nike",
                "price": 1000,
                "count": 9
            },
            {
                "id": 11,
                "name": "nike2",
                "price": 1500,
                "count": 4
            }
        ]
    }
    ```
    

## 8. 상품 옵션 아이템 수정 API

- PUT /seller/product/item
- 파라미터 : 상품id, 아이템id, 옵션 아이템 명, 옵션 아이템 수 , 가격
- 정책 : 상품이 없을 경우, 상품 아이템이 없을 경우 실패응답
- 성공 응답 : 상품 id, 상품명, 상품 가격, 상품의 수
- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "productId" : 4,
        "productItemId" : 9,
        "name" : "nike",
        "count" : 10,
        "price" : 1000
    }
    ```
    
    - 응답
    
    ```java
    {
        "id": 9,
        "name": "nike",
        "price": 1000,
        "count": 10
    }
    ```
    

## 9. 상품 삭제 API

- DELETE /seller/product?productId={productId}
- 파라미터 : 상품id
- 정책 : 상품이 없을 경우, 상품 아이템이 없을 경우 실패응답
- 성공 응답 : 없음

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    DELETE /seller/product?productId={productId}
    ```
    
    - 응답
    
    ```java
    
    ```
    

## 10. 상품 아이템 삭제 API

- DELETE /seller/product/item?productItemId={productItemId}
- 파라미터 : 상품 아이템 id
- 정책 : 상품이 없을 경우, 상품 아이템이 없을 경우 실패응답
- 성공 응답 : 없음

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    DELETE /seller/product/item?productItemId={productItemId}
    ```
    
    - 응답
    
    ```java
    
    ```
    

## 11. 상품 조회 API

- GET /search/product?name={name}
- 파라미터 : 상품id
- 정책 : Like 연산을 이용해 name을 포함하는 상품들의 정보를 반환한다.
- 성공 응답 : List<상품 id, 상품명, 상품 설명>

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    GET /search/product?name={name}
    ```
    
    - 응답
    
    ```java
    [
        {
            "id": 2,
            "name": "nike",
            "description": "shoes"
        },
        {
            "id": 3,
            "name": "NIKE AIRFORCE",
            "description": "THIS IS NIKE"
        },
        {
            "id": 4,
            "name": "nike",
            "description": "shoes"
        }
    ]
    ```
    

## 12. 상품 상세 조회 API

- GET /seller/product/detail?productId={productId}
- 파라미터 : 상품id
- 정책 : 상품이 없을 경우 실패응답
- 성공 응답 : 상품 id, 상품명, 상품 설명 , List<상품 아이템 정보>

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    GET /seller/product/detail?productId={productId}
    ```
    
    - 응답
    
    ```java
    {
        "id": 4,
        "name": "nike",
        "description": "shoes",
        "items": [
            {
                "id": 8,
                "name": "item",
                "price": 1000,
                "count": 5
            },
            {
                "id": 12,
                "name": "item",
                "price": 500,
                "count": 10
            },
            {
                "id": 13,
                "name": "nike2",
                "price": 1500,
                "count": 4
            }
        ]
    }
    ```
    

## 13. 장바구니에 상품 추가 API

- POST /customer/cart
- 파라미터 : 상품id , 판매자id , 상품명 , 상품 설명, 상품 아이템 정보
- 정책 : 장바구니가 비어있을 경우 새로 생성하고 파라미터로 들어온 상품 정보를 장바구니에 저장한다.  장바구니에 같은 물건이 있을 경우 상품명을 확인해 다르면 메시지를 남기고 변경시켜주고, 수량의 경우 수량을 늘려주고 수량이 등록된 것보다 높으면 가장 높은 수량으로 변경해주고 메시지를 저장해준다.
- 성공 응답 : 고객id, 상품 정보 , 오류 메시지

### 상세 정보

- 레디스로 응답값을 저장한다. (key : customerId, value = Cart)
- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "productId":4,
        "sellerId":2,
        "name" : "nike",
        "description" : "shoes",
        "items" : [
            {
                "productItemId" : 8,
                "count" : 1,
                "name" : "item",
                "price" : 1000
            }
            ,{
                "productItemId" : 12,
                "count" : 1,
                "name" : "item",
                "price" : 1000
            }
        ]
    }
    ```
    
    - 응답
    
    ```java
    {
        "customerId": 2,
        "products": [
            {
                "id": 4,
                "sellerId": 2,
                "name": "nike",
                "description": "shoes",
                "items": [
                    {
                        "id": 8,
                        "name": "item",
                        "count": 1,
                        "price": 1000
                    },
                    {
                        "id": 12,
                        "name": "item",
                        "count": 1,
                        "price": 500
                    }
                ]
            }
        ],
        "messages": [
            "nike 상품의 변동 사항 : item 가격이 변경되었습니다., "
        ]
    }
    ```
    

## 14. 장바구니 조회 API

- GET /customer/cart
- 파라미터 : 토큰
- 정책 : 상품이나 상품의 아이템의 정보, 가격, 수량이 변경되었는 지 체크하고 그에 맞는 메세지를 제공한다. 상품의 수량, 가격을 수량의 경우 고객이 상품에 등록된 갯수보다 더 많이 신청했다면 최대 수량을 , 가격의 경우 상품에 등록된 가격으로 수정한다.
- 성공 응답 : 고객 id, 카트에 담긴 상품 정보,  오류 메시지

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    GET /customer/cart
    ```
    
    - 응답
    
    ```java
    {
        "customerId": 2,
        "products": [
            {
                "id": 4,
                "sellerId": 2,
                "name": "nike",
                "description": "shoes",
                "items": [
                    {
                        "id": 8,
                        "name": "item",
                        "count": 1,
                        "price": 1000
                    },
                    {
                        "id": 12,
                        "name": "item",
                        "count": 1,
                        "price": 500
                    }
                ]
            }
        ],
        "messages": []
    }
    ```
    

## 15. 장바구니 수정 API

- PUT /customer/cart
- 파라미터 :  List<카트에 담길 상품 정보>
- 정책 : 상품이나 상품의 아이템의 정보, 가격, 수량이 변경되었는 지 체크하고 그에 맞는 메세지를 제공한다. 상품의 수량, 가격을 수량의 경우 고객이 상품에 등록된 갯수보다 더 많이 신청했다면 최대 수량을 , 가격의 경우 상품에 등록된 가격으로 수정한다.
- 성공 응답 : 고객 id, 카트에 담긴 상품 정보,  오류 메시지

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "products": [
            {
                "id": 4,
                "sellerId": 2,
                "name": "nike",
                "description": "shoes",
                "items": [
                    {
                        "id": 8,
                        "name": "item",
                        "count": 1,
                        "price": 1000
                    },
                    {
                        "id": 12,
                        "name": "item",
                        "count": 1,
                        "price": 500
                    }
                ]
            }
        ]
    }
    ```
    
    - 응답
    
    ```java
    {
        "customerId": 2,
        "products": [
            {
                "id": 4,
                "sellerId": 2,
                "name": "nike",
                "description": "shoes",
                "items": [
                    {
                        "id": 8,
                        "name": "item",
                        "count": 1,
                        "price": 1000
                    },
                    {
                        "id": 12,
                        "name": "item",
                        "count": 1,
                        "price": 500
                    }
                ]
            }
        ],
        "messages": []
    }
    ```
    

## 16. 주문하기 API

- POST /customer/cart/order
- 파라미터 :  카트 정보
- 정책 : Cart에 잘못된 값이 담겨 있는 경우 , 잔액이 부족한 경우 실패응답하며 주문이 정상적으로 이루어진 경우 주문 내역을 고객의 이메일로 송부한다.
- 성공 응답 : 주문 내역

### 상세 정보

- 요청 / 응답 구조
    - 요청
    
    ```java
    {
        "products": [
            {
                "id": 4,
                "sellerId": 2,
                "name": "nike",
                "description": "shoes",
                "items": [
                    {
                        "id": 8,
                        "name": "item",
                        "count": 1,
                        "price": 1000
                    },
                    {
                        "id": 12,
                        "name": "item",
                        "count": 1,
                        "price": 500
                    }
                ]
            }
        ]
    }
    ```
    
    - 응답
    
    ```java
    {
        "customerId": 2,
        "products": [
            {
                "id": 4,
                "sellerId": 2,
                "name": "nike",
                "description": "shoes",
                "items": [
                    {
                        "id": 8,
                        "name": "item",
                        "count": 1,
                        "price": 1000
                    },
                    {
                        "id": 12,
                        "name": "item",
                        "count": 1,
                        "price": 500
                    }
                ]
            }
        ],
        "messages": []
    }
    ```

## 사용 기술
- Spring
- Jpa
- Mysql
- Redis
- Docker
- AWS
