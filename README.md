## 개발 환경
- 개발 언어 : Java (java21)
- 프레임워크 : Spring Boot 3.3.4 + JPA
- DB : H2 Database (mem)
- Cache : Caffeine cache
- 빌드 환경 : gradle
- 그 외 : Docker

## 설치 및 실행
#### 다운로드 및 디렉토리 이동
```
git clone https://github.com/this1771/ProductSample.git && cd ProductSample 
```

#### 초기화 및 빌드
```
gradle clean build 
```

#### 통합 및 단위테스트
- 전체 테스트
```
gradle test
```
- 개별 테스트
```
gradle test --tests "com.example.productsample.controller.search.SearchControllerTest"
gradle test --tests "com.example.productsample.controller.product.ProductControllerTest"
gradle test --tests "com.example.productsample.controller.category.CategoryControllerTest"
gradle test --tests "com.example.productsample.controller.brand.BrandControllerTest"
gradle test --tests "com.example.productsample.service.SearchServiceTest"
gradle test --tests "com.example.productsample.service.ProductServiceTest"
gradle test --tests "com.example.productsample.service.CategoryServiceTest"
gradle test --tests "com.example.productsample.service.BrandServiceTest"
```

#### 어플리케이션 실행
```
docker-compose up --build 
```

#### Frontend 페이지 접속
```
http://localhost:8080/
```

## H2 데이터베이스
- [스키마 확인하기](src/main/resources/database/schema.sql)
- 접속 정보
  - URL: http://localhost:8080/h2-console/
  - JDBC URL: jdbc:h2:mem:product
  - User Name: sa
  - Password:

## API 사용법
- [json 파일](src/main/resources/document/postman_collection.json) 다운로드
- Postman 실행 후, json 파일을 import
  - {{brandCd}}, {{productCd}} 실제 값으로 변경 필요.


## 구현 범위
- [x] 카테고리 별 최저가 상품 조회 API
- [x] 최저가 브랜드 내 카테고리별 최저가 상품 조회 API
- [x] 카테고리 별 최저/최고가 상품 조회 API
- [x] 브랜드 및 상품 추가 / 업데이트 / 삭제 API
- [x] (선택) 단위/통합 테스트 작성
- [ ] (선택) Frontend 구현 (구현1 ~ 구현3까지만 구현)
