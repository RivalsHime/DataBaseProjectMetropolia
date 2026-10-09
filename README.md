# ProjectJavaDataBase — Webshop API

Java 21 / Spring Boot / Spring Data JPA / MariaDB .

## Endpoints
- `GET /api/products?page=0&size=20`
- `GET /api/products/{id}`
- `POST /api/orders`
- `GET /api/orders/{id}`
- `POST /api/orders/{id}/ship`
- `POST /api/orders/{id}/cancel`

Example order:
```json
{"customerId":1,"shippingAddressId":2,"lines":[{"productId":3,"quantity":2}]}
```

## Important
This is a learning project and is **not production-ready**. It has no authentication or authorization, so do not expose the order endpoints publicly. Unit tests cover basic service rules; MariaDB integration tests, rollback tests, concurrency tests.
