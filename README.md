# Database Project — Webshop REST API

A Java Spring Boot REST API for managing products and customer orders using a relational MariaDB database.

**Course:** Metropolia University of Applied Sciences — Java Database Project  
**Repository:** https://github.com/RivalsHime/DataBaseProjectMetropolia

## 1. Project Overview

This project implements the backend of a simple webshop. It connects a Java application to an existing MariaDB database and provides REST endpoints for browsing products, creating orders, retrieving orders, shipping orders, and cancelling orders.

The main objective is to demonstrate how Java, Spring Boot, Spring Data JPA, and a relational database can work together to implement business operations while maintaining data consistency.

The application focuses on backend functionality. It does not include a graphical user interface.

## 2. Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 3.5.11 | Application framework |
| Spring Web | REST API endpoints |
| Spring Data JPA | Database access through repositories |
| Hibernate | Object-relational mapping |
| MariaDB | Relational database |
| Maven | Dependency management and build |
| JUnit 5 | Automated testing |
| Mockito | Mocking repository dependencies in unit tests |
| YAML | Application configuration |

## 3. Project Structure

```text
ProjectJavaDataBase/
├── src/
│   ├── main/
│   │   ├── java/fi/metropolia/omagr/grimaux/
│   │   │   ├── controller/
│   │   │   │   └── ApiController.java
│   │   │   ├── dto/
│   │   │   │   └── ProductView.java
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   │   └── OrderService.java
│   │   │   └── GrimauxApplication.java
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
│       └── java/fi/metropolia/omagr/grimaux/
│           ├── service/
│           │   └── OrderServiceTest.java
│           └── GrimauxApplicationTests.java
└── pom.xml
```

The project separates responsibilities into different layers:

- **Controller:** Receives HTTP requests and returns API responses.
- **DTO:** Defines data returned to API clients.
- **Entity:** Maps Java objects to database tables.
- **Repository:** Provides database operations through Spring Data JPA.
- **Service:** Implements order processing, validation, and stock management.
- **Test:** Contains automated tests for application startup and business logic.

## 4. API Documentation

The API runs locally at:

```text
http://localhost:8080
```

All endpoints use the `/api` prefix. JSON is used for request and response bodies.

### Endpoint Summary

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/products` | List products |
| GET | `/api/products/{id}` | Retrieve one product |
| POST | `/api/orders` | Create an order |
| GET | `/api/orders/{id}` | Retrieve an order |
| POST | `/api/orders/{id}/ship` | Ship an order |
| POST | `/api/orders/{id}/cancel` | Cancel an order |

### 4.1 List Products

**Endpoint:** `GET /api/products`

**Purpose:** Returns products from the database, including their identifiers, names, prices, and available stock quantities.

**Optional query parameters:**

| Parameter | Type | Default | Description |
|---|---|---|---|
| `page` | Integer | `0` | Zero-based page number |
| `size` | Integer | `20` | Number of products requested |

The application limits the page size to a maximum of 100. Negative page numbers are treated as zero, and page sizes below one are treated as one.

**Example request:**

```http
GET /api/products?page=0&size=10
```

**Example response (illustrative values):**

```json
[
  {
    "id": 1,
    "name": "Example Product",
    "price": 19.99,
    "stockQuantity": 25
  },
  {
    "id": 2,
    "name": "Another Product",
    "price": 34.50,
    "stockQuantity": 12
  }
]
```

**Response format:** A JSON array of product objects. The endpoint returns the product list directly, without pagination metadata such as total pages or total elements.

### 4.2 Retrieve a Product

**Endpoint:** `GET /api/products/{id}`

**Purpose:** Retrieves one product by its database identifier.

**Path parameter:** `id` — integer product identifier.

**Example request:**

```http
GET /api/products/1
```

**Example response:**

```json
{
  "id": 1,
  "name": "Example Product",
  "price": 19.99,
  "stockQuantity": 25
}
```

**Possible responses:**

- `200 OK` — Product found.
- `404 Not Found` — Product does not exist.

### 4.3 Create an Order

**Endpoint:** `POST /api/orders`

**Purpose:** Creates a new customer order, saves its order items, and deducts the purchased quantities from product stock.

**Request format:**

```json
{
  "customerId": 1,
  "shippingAddressId": 2,
  "lines": [
    {
      "productId": 3,
      "quantity": 2
    }
  ]
}
```

**Request fields:**

| Field | Type | Description |
|---|---|---|
| `customerId` | Integer | Existing customer identifier |
| `shippingAddressId` | Integer | Existing shipping address identifier |
| `lines` | Array | Products included in the order |
| `lines[].productId` | Integer | Existing product identifier |
| `lines[].quantity` | Integer | Quantity to purchase |

The order must contain at least one product line.

**Example successful response:**

```json
{
  "id": 4,
  "customerId": 1,
  "status": "NEW",
  "lines": [
    {
      "productId": 3,
      "quantity": 2,
      "unitPrice": 12.50
    }
  ]
}
```

**Response fields:**

- `id`: Newly created order identifier.
- `customerId`: Customer associated with the order.
- `status`: Initial order status, `NEW`.
- `lines`: Ordered products, quantities, and prices recorded at the time of ordering.

**Implemented validation and business rules:**

- The customer must exist.
- The shipping address must exist.
- The shipping address must belong to the specified customer.
- At least one order line is required.
- Every product must exist.
- Quantities must be positive.
- Sufficient stock must be available.
- Duplicate product identifiers within the same request are combined into one order line.
- Product stock is reduced when the order is created.
- The order date is recorded automatically.
- Each order item stores its unit price at the time of purchase.

**Possible responses:**

- `200 OK` — Order created and returned.
- `400 Bad Request` — Invalid order data, missing customer, invalid address, missing product, or invalid quantity.
- `409 Conflict` — Insufficient product stock.

The controller does not explicitly return `201 Created`; the successful response uses Spring's default `200 OK`.

### 4.4 Retrieve an Order

**Endpoint:** `GET /api/orders/{id}`

**Purpose:** Retrieves an existing order with its status and ordered items.

**Example request:**

```http
GET /api/orders/4
```

**Example response:**

```json
{
  "id": 4,
  "customerId": 1,
  "status": "NEW",
  "lines": [
    {
      "productId": 3,
      "quantity": 2,
      "unitPrice": 12.50
    }
  ]
}
```

**Possible responses:**

- `200 OK` — Order found.
- `404 Not Found` — Order does not exist.

### 4.5 Ship an Order

**Endpoint:** `POST /api/orders/{id}/ship`

**Purpose:** Changes an eligible order's status to `SHIPPED`.

**Example request:**

```http
POST /api/orders/4/ship
```

**Request body:** None.

**Example response:**

```json
{
  "id": 4,
  "customerId": 1,
  "status": "SHIPPED",
  "lines": [
    {
      "productId": 3,
      "quantity": 2,
      "unitPrice": 12.50
    }
  ]
}
```

**Business rules:**

- Orders with status `NEW` or `LATE` can be shipped.
- Other order statuses cannot be shipped.
- Shipping changes the order status without deducting stock again.

**Possible responses:**

- `200 OK` — Order successfully marked as shipped.
- `404 Not Found` — Order does not exist.
- `409 Conflict` — Order cannot be shipped in its current state.

### 4.6 Cancel an Order

**Endpoint:** `POST /api/orders/{id}/cancel`

**Purpose:** Cancels an eligible order and restores the quantities of its products to stock.

**Example request:**

```http
POST /api/orders/4/cancel
```

**Request body:** None.

**Example response:**

```json
{
  "id": 4,
  "customerId": 1,
  "status": "CANCELLED",
  "lines": [
    {
      "productId": 3,
      "quantity": 2,
      "unitPrice": 12.50
    }
  ]
}
```

**Business rules:**

- Orders with status `NEW` or `LATE` can be cancelled.
- Cancelling an order restores the quantities recorded in its order items.
- Shipped and already cancelled orders cannot be cancelled again.

**Possible responses:**

- `200 OK` — Order cancelled.
- `404 Not Found` — Order does not exist.
- `409 Conflict` — Order cannot be cancelled in its current state.

## 5. Database Design

The application uses a relational MariaDB database with JPA entity mappings.

The Java application includes entities representing the following tables:

| Entity | Database table | Purpose |
|---|---|---|
| `Product` | `products` | Product information, prices, and stock |
| `ProductCategory` | Category table | Product classification |
| `Supplier` | Supplier table | Product suppliers |
| `SupplierAddress` | Supplier address table | Supplier contact addresses |
| `Customer` | Customer table | Customer information |
| `CustomerAddress` | `customeraddresses` | Customer delivery addresses |
| `Contact` | Contact table | Contact information |
| `ShopOrder` | `orders` | Customer orders and their statuses |
| `OrderItem` | `orderitems` | Products and quantities in each order |
| `OrderItemId` | Embedded key | Composite identifier for order items |

`OrderItemId` is an embeddable identifier rather than a separate database table.

### 5.1 Entity Relationships

The JPA mappings establish relationships between different parts of the webshop:

- A product can reference a product category and a supplier.
- An order belongs to a customer.
- An order can reference a customer shipping address.
- An order contains individual order items.
- Each order item references a product.

These relationships allow the application to connect customer orders with product records while avoiding unnecessary duplication of data.

### 5.2 Primary and Composite Keys

The entities use primary keys to uniquely identify database records.

The `orderitems` table uses a composite key containing:

- `order_id`
- `product_id`

This is represented in Java using `@EmbeddedId` and `@MapsId`.

### 5.3 Database Constraints

The Java mappings specify constraints such as:

- Non-null product names.
- Non-null product prices.
- Non-null stock quantities.
- Non-null order dates.
- Non-null order item quantities and unit prices.
- Foreign-key relationships between associated entities.
- Decimal precision and scale for monetary values.

For example, product prices and order item unit prices use `BigDecimal` in Java, avoiding floating-point rounding problems associated with `double`.

The exact database-level constraints depend on the MariaDB schema used by the project.

### 5.4 Indexes

The application uses primary-key lookups and foreign-key relationships through JPA repositories.

Primary-key indexes support operations such as finding products and orders by ID.

The exact additional indexes defined in MariaDB are not documented here because they must be verified against the database schema. No custom index definitions are declared in the inspected Java entity mappings.

### 5.5 Views, Stored Procedures, and Triggers

The API operates through Spring Data JPA repositories and mapped database tables.

No SQL views or stored procedures are directly used by the inspected API implementation.

Any database triggers, views, or additional database objects created outside the Java application should be documented separately from a MariaDB schema export. Their existence and behavior cannot be confirmed from the application code alone.

## 6. Transactions and Data Consistency

Order operations are implemented in `OrderService`.

### Transaction Management

Spring's `@Transactional` annotation is used for:

- Creating orders.
- Retrieving orders with a read-only transaction.
- Shipping orders.
- Cancelling orders.

Transactions help ensure that related database operations are processed together.

For example, creating an order involves saving an order, creating order items, and updating product stock.

### Stock Management

The application implements stock management rules:

1. Validate the requested order.
2. Check that products exist.
3. Check available quantities.
4. Save the order and its items.
5. Deduct purchased quantities from stock.

When an eligible order is cancelled, the application adds the ordered quantities back to stock.

### Concurrency Control

The product repository declares a pessimistic write lock:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
Optional<Product> findById(Integer id);
```

This requests database-level locking when products are retrieved through that repository method within a transaction.

During order creation, product IDs are processed in sorted order. This provides a consistent locking order and can help reduce some concurrency conflicts.

Concurrent behavior has not been verified through dedicated MariaDB concurrency tests.

## 7. Security and Validation

### Implemented Protection

The service implements business-level validation to prevent invalid operations:

- Rejecting nonexistent customers and products.
- Preventing customers from using another customer's shipping address.
- Rejecting zero or negative quantities.
- Rejecting orders when stock is insufficient.
- Preventing shipping or cancellation from invalid order states.

These checks protect the consistency of the application's business data.

### Current Security Limitations

The application does **not** implement user authentication or role-based authorization.

There is no Spring Security configuration protecting the API endpoints.

Consequently:

- API clients are not required to log in.
- Customer identity is supplied through request data.
- Order operations are not restricted to authenticated customers or administrators.

**This is an educational project and should not be exposed as a public production webshop.**

## 8. Application Configuration

The project uses `application.yaml` for Spring Boot configuration.

An example configuration is:

```yaml
spring:
  application:
    name: grimaux

  datasource:
    url: ${DB_URL:jdbc:mariadb://127.0.0.1:3306/proj}
    username: ${DB_USER:root}
    password: ${DB_PASSWORD:}

  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        show_sql: true
        format_sql: true
```

The actual MariaDB connection details must be adapted to the local environment.

### JPA Configuration

- `ddl-auto: validate` checks whether the database schema matches the entity mappings.
- `show_sql: true` displays Hibernate SQL statements.
- `format_sql: true` formats SQL output for readability.

The application does not automatically create the required MariaDB tables with this configuration.

## 9. Installation and Running the Project

### Requirements

- Java Development Kit 21
- Apache Maven
- MariaDB
- An existing database containing the required webshop tables

### Step 1 — Clone the Repository

```bash
git clone https://github.com/RivalsHime/DataBaseProjectMetropolia.git
```

Open the `ProjectJavaDataBase` directory containing `pom.xml`.

### Step 2 — Configure the Database

Create or import the required MariaDB database schema.

Configure the connection through `application.yaml` or environment variables.

The local development database used for this project is named `proj`.

### Step 3 — Run the Application

From the directory containing `pom.xml`:

```bash
mvn spring-boot:run
```

Alternatively, open the project in IntelliJ IDEA and run `GrimauxApplication.java`.

### Step 4 — Test the API

Once Spring Boot has started, open:

```text
http://localhost:8080/api/products
```

The endpoint should return a JSON array of products if the database connection and schema are correctly configured.

## 10. Testing

The project includes automated tests under `src/test/java`.

### OrderServiceTest

The service tests use JUnit 5 and Mockito to test order business logic without requiring a running MariaDB server.

The implemented tests cover:

- Deducting stock when creating an order.
- Rejecting an order with insufficient stock.
- Rejecting a shipping address belonging to another customer.
- Rejecting non-positive quantities.

### GrimauxApplicationTests

This class contains a Spring Boot context-loading test.

Its purpose is to verify that the application context can initialize with its test configuration.

### Running Tests

```bash
mvn test
```

### Testing Limitations

The inspected test suite does not include dedicated MariaDB integration tests for every endpoint or automated tests for all shipping, cancellation, and concurrency scenarios.

These are potential areas for future improvement.

## 11. Implemented Features Summary

The main implemented features are:

- REST endpoints for listing and retrieving products.
- Pagination parameters for product listing.
- Creation of customer orders.
- Validation of customers, addresses, products, and quantities.
- Automatic stock deduction when placing orders.
- Order retrieval.
- Order status changes for shipping.
- Order cancellation with stock restoration.
- Transactional order processing.
- JPA entity relationships and composite identifiers.
- Pessimistic product locking.
- JSON request and response handling.
- YAML application configuration.
- Automated service tests and an application context test.

## 12. Limitations and Future Improvements

This project is intended for educational purposes rather than production use.

Potential future improvements include:

- User authentication and authorization.
- A frontend interface for customers and administrators.
- More comprehensive API integration tests.
- Automated concurrency tests against MariaDB.
- Improved error response formatting.
- A database schema export or reproducible database setup.
- Additional filtering and searching for products.
- More detailed order information in API responses.
- Database migration management if required in future development.

## 13. Conclusion

This project demonstrates a functional Spring Boot backend connected to a relational MariaDB database.

It combines REST API development, Spring Data JPA repositories, entity relationships, transaction management, business validation, and order stock management.

The main focus is to implement webshop operations in Java while keeping the database consistent and the application structure understandable and maintainable.
