CREATE TABLE account
(
    id                       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 고유 번호',
    nickname                 VARCHAR(50)  NOT NULL COMMENT '회원 이름',
    email                    VARCHAR(255) NOT NULL UNIQUE COMMENT '회원 이메일',
    password                 VARCHAR(255) NOT NULL COMMENT '회원 패스워드',
    is_valid                 bool         NOT NULL COMMENT '가입 승인 여부',
    email_token              VARCHAR(500) COMMENT '이메일 토큰',
    email_token_generated_at DATETIME COMMENT '이메일 토큰 전송 시간',
    city                     VARCHAR(255) COMMENT '도시 주소',
    street                   VARCHAR(255) COMMENT '도로명 주소',
    zipcode                  VARCHAR(50) COMMENT '우편 번호',
    created_at               DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '가입날짜',
    updated_at               DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

CREATE TABLE persistent_logins
(
    series    VARCHAR(64) PRIMARY KEY COMMENT '로그인 시리즈 식별자',
    username  VARCHAR(64) NOT NULL COMMENT '사용자 이름',
    token     VARCHAR(64) NOT NULL COMMENT '토큰 값',
    last_used DATETIME COMMENT '마지막 사용 시각'
);

CREATE TABLE account_role
(
    account_role_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '고유 번호',
    account_id      BIGINT NOT NULL COMMENT '회원 고유 번호',
    role_id         BIGINT NOT NULL COMMENT '권한 고유 번호'
);

CREATE TABLE role
(
    role_id   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '권한 고유 번호',
    role_name VARCHAR(255) NOT NULL COMMENT '권한 이름'
);


CREATE TABLE cart
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT       NOT NULL COMMENT '회원 고유 번호',
    product_id BIGINT       NOT NULL COMMENT '상품 고유 번호',
    quantity   INT          NOT NULL COMMENT '수량',
    options    VARCHAR(255) NOT NULL COMMENT '추가 내용 및 옵션 (맛 종류 등)',
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

CREATE TABLE orders
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '주문 고유 번호',
    account_id  BIGINT NOT NULL COMMENT '회원 고유 번호',
    delivery_id BIGINT NOT NULL COMMENT '배송 고유 번호',
    orderDateDto   DATETIME(6)               DEFAULT CURRENT_TIMESTAMP(6),
    status      ENUM ( 'ORDER', 'CANCEL') DEFAULT 'READY' COMMENT '배송 상태'
);

CREATE TABLE delivery
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '주문 고유 번호',
    account_id BIGINT NOT NULL COMMENT '회원 고유 번호',
    city       VARCHAR(255) COMMENT '도시 주소',
    street     VARCHAR(255) COMMENT '도로명 주소',
    zipcode    VARCHAR(50) COMMENT '우편 번호',
    status     ENUM ('READY', 'DELIVERING', 'COMPLETED', 'CANCELLED') DEFAULT 'READY' COMMENT '배송 상태'
);

CREATE TABLE order_product
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT         NOT NULL COMMENT '주문 고유 번호',
    product_id  BIGINT         NOT NULL COMMENT '상품 고유 번호',
    order_price DECIMAL(10, 2) NOT NULL COMMENT '주문 시점의 상품 가격',
    quantity    INT            NOT NULL COMMENT '주문 수량',
    options     VARCHAR(255)   NOT NULL COMMENT '추가 내용 및 옵션 (맛 종류 등)'
);


CREATE TABLE product
(
    product_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(255)   NOT NULL COMMENT '상품명',
    description         TEXT COMMENT '상품 상세',
    price               DECIMAL(10, 2) NOT NULL COMMENT '가격',
    stock               INT            NOT NULL DEFAULT 0 COMMENT '재고',
    account_id          BIGINT         NOT NULL COMMENT '회원 고유 번호',
    category_id         BIGINT COMMENT '카테고리 고유 번호',
    flavor_select_count INT            NOT NULL DEFAULT 0 COMMENT '맛 선택 개수',
    active              bool           NOT NULL,
    created_at          DATETIME                DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE product_images
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT DEFAULT NULL COMMENT '상품 고유 번호',
    image_url  TEXT COMMENT '이미지 경로'
);

CREATE TABLE category
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    parent_id BIGINT DEFAULT NULL COMMENT '부모 카테고리 ID'
);

CREATE TABLE flavor
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    active bool         NOT NULL
);