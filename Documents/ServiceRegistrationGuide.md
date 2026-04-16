# OmniCure.ai — Service Registration Guide

## .NET Services (C#)

### 1. `Program.cs` của mỗi API Service

```csharp
var builder = WebApplication.CreateBuilder(args);

// ── Bắt buộc từ Core ──────────────────────────────────────────────
builder.Services.AddCoreServices(builder.Configuration);
// → Tự động đăng ký:
//   - AddAuthorization() với tất cả Permission Policies (từ PermissionConstants)

// ── DbContext riêng của Service ───────────────────────────────────
builder.Services.AddDbContext<BillingWriteDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString(KeyConstants.ConnectionStrings.WriteDb)));

builder.Services.AddDbContext<BillingReadDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString(KeyConstants.ConnectionStrings.ReadDb))
           .UseQueryTrackingBehavior(QueryTrackingBehavior.NoTracking));

// ── Persistence từ Core ───────────────────────────────────────────
builder.Services.AddScoped<IUnitOfWork, UnitOfWork<BillingWriteDbContext>>();
builder.Services.AddScoped<IQueryExecutor, QueryExecutor>();

// ── Repositories riêng của Service (nếu cần mở rộng) ─────────────
// builder.Services.AddScoped<IInvoiceRepository, InvoiceRepository>();

// ── Application Layer ─────────────────────────────────────────────
// builder.Services.AddMediatR(cfg => cfg.RegisterServicesFromAssembly(typeof(Program).Assembly));

var app = builder.Build();

// ── Migration khi khởi động ───────────────────────────────────────
await app.Services.ApplyMigrationsAsync<BillingWriteDbContext, BillingReadDbContext>();

app.Run();
```

### 2. Tóm tắt từng thứ cần đăng ký

| # | Đăng ký | Namespace | Bắt buộc? |
|---|---------|-----------|-----------|
| 1 | `AddCoreServices(configuration)` | `OmniCure.Core.WebShared.Extensions` | ✅ Bắt buộc |
| 2 | `AddDbContext<TWriteDbContext>` | Service tự định nghĩa | ✅ Bắt buộc |
| 3 | `AddDbContext<TReadDbContext>` (NoTracking) | Service tự định nghĩa | ✅ Bắt buộc |
| 4 | `IUnitOfWork` → `UnitOfWork<TWriteDbContext>` | `OmniCure.Core.Infrastructure` | ✅ Bắt buộc |
| 5 | `IQueryExecutor` → `QueryExecutor` | `OmniCure.Core.Infrastructure` | ✅ Bắt buộc |
| 6 | `ApplyMigrationsAsync<TWrite, TRead>()` | `OmniCure.Core.Infrastructure.Extensions` | ✅ Bắt buộc |
| 7 | `IReadRepository<T, ID>` → `ReadRepository<...>` | `OmniCure.Core.Infrastructure` | Tuỳ service |
| 8 | `IWriteRepository<T, ID>` → `WriteRepository<...>` | `OmniCure.Core.Infrastructure` | Tuỳ service |
| 9 | Service-specific Repositories | Service tự định nghĩa | Tuỳ service |
| 10 | MediatR / CQRS Handlers | Service tự định nghĩa | Tuỳ service |

---

## Java Services (Spring Boot)

### 1. `Application.java` của mỗi Service

Không cần đăng ký thủ công — Spring Boot tự quét qua `scanBasePackages`:

```java
@SpringBootApplication(scanBasePackages = {
    "ai.omnicure.billing",   // Package của service hiện tại
    "ai.omnicure.core"       // Quét toàn bộ Core để pick up @Bean, @Component, @Service
})
public class BillingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }
}
```

> [!IMPORTANT]
> `scanBasePackages` phải bao gồm cả `"ai.omnicure.core"` để Spring Boot tự động quét và đăng ký tất cả các Bean trong Core.

### 2. `application.yml` của mỗi Service

```yaml
spring:
  datasource:
    write:
      url: jdbc:postgresql://localhost:5432/billing_write
      username: ${DB_USERNAME}
      password: ${DB_PASSWORD}
    read:
      url: jdbc:postgresql://localhost:5432/billing_read
      username: ${DB_USERNAME}
      password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true

database-initialization:
  max-retries: 10
  retry-interval: 3
```

### 3. Config class riêng của mỗi Service (ReadDb / WriteDb)

Mỗi service cần tạo 2 `@Configuration` class để bind DataSource cho Read/Write:

```java
// WriteDbConfig.java
@Configuration
@EnableJpaRepositories(
    basePackages = "ai.omnicure.billing.infrastructure.repository.write",
    entityManagerFactoryRef = "writeEntityManagerFactory",
    transactionManagerRef = "writeTransactionManager"
)
public class WriteDbConfig { ... }

// ReadDbConfig.java  
@Configuration
@EnableJpaRepositories(
    basePackages = "ai.omnicure.billing.infrastructure.repository.read",
    entityManagerFactoryRef = "readEntityManagerFactory"
)
public class ReadDbConfig { ... }
```

### 4. Tóm tắt từng thứ cần làm

| # | Thứ cần làm | Nơi thực hiện | Bắt buộc? |
|---|-------------|---------------|-----------|
| 1 | `scanBasePackages` bao gồm `"ai.omnicure.core"` | `Application.java` | ✅ Bắt buộc |
| 2 | DataSource Write config + EntityManagerFactory | `WriteDbConfig.java` (service tự tạo) | ✅ Bắt buộc |
| 3 | DataSource Read config + EntityManagerFactory | `ReadDbConfig.java` (service tự tạo) | ✅ Bắt buộc |
| 4 | `application.yml` với connection strings | `resources/` của service | ✅ Bắt buộc |
| 5 | Flyway migration scripts | `resources/db/migration/` | ✅ Bắt buộc |
| 6 | Service-specific `@Repository` interfaces | Service tự định nghĩa | Tuỳ service |
| 7 | Service-specific `@Service` / `@UseCase` classes | Service tự định nghĩa | Tuỳ service |
| 8 | `SecurityConfig` (JWT filter, CORS) | Kế thừa từ `CoreWebShared` | Tuỳ chỉnh |

---

## Lưu ý quan trọng

> [!NOTE]
> **`QueryExecutor`** (Java): đã được đánh dấu `@Component` nên Spring Boot sẽ tự đăng ký khi `scanBasePackages` bao gồm `ai.omnicure.core`.

> [!NOTE]  
> **`IUnitOfWork`** (.NET): phải đăng ký thủ công với đúng generic `TWriteDbContext` của service đó. Ví dụ: `UnitOfWork<BillingWriteDbContext>`.

> [!WARNING]
> **Read DbContext** (.NET): phải bật `UseQueryTrackingBehavior(QueryTrackingBehavior.NoTracking)` để tránh EF Core theo dõi entity không cần thiết, giảm bộ nhớ.
