-- 브랜드
CREATE TABLE IF NOT EXISTS brand (
    brand_cd INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40) NOT NULL UNIQUE,
    deleted TINYINT DEFAULT 0
    );

COMMENT ON TABLE brand IS '브랜드';
COMMENT ON COLUMN brand.brand_cd IS '브랜드 고유코드';
COMMENT ON COLUMN brand.name IS '브랜드명';
COMMENT ON COLUMN brand.deleted IS '삭제여부';



-- 카테고리
CREATE TABLE IF NOT EXISTS category (
    category_cd SMALLINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
    );

COMMENT ON TABLE category IS '카테고리';
COMMENT ON COLUMN category.category_cd IS '카테고리 고유코드';
COMMENT ON COLUMN category.name IS '카테고리명';



-- 상품
CREATE TABLE IF NOT EXISTS product (
    product_cd BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_cd INT NOT NULL,
    category_cd SMALLINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    price INT DEFAULT 0,
    deleted TINYINT DEFAULT 0,
    CHECK (price >= 0),
    FOREIGN KEY (brand_cd) REFERENCES brand(brand_cd),
    FOREIGN KEY (category_cd) REFERENCES category(category_cd)
    );

COMMENT ON TABLE product IS '상품';
COMMENT ON COLUMN product.product_cd IS '상품 고유코드';
COMMENT ON COLUMN product.brand_cd IS '브랜드 고유코드';
COMMENT ON COLUMN product.category_cd IS '카테고리 고유코드';
COMMENT ON COLUMN product.name IS '상품명';
COMMENT ON COLUMN product.price IS '상품가격';
COMMENT ON COLUMN product.deleted IS '삭제여부';

CREATE INDEX IF NOT EXISTS idx_category_cd_deleted ON product (category_cd, deleted);