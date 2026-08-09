# Workspace Guidelines

## Agent Directives
- You **MUST ALWAYS** read and follow all Markdown files inside the `.agents/rules/` directory (e.g., `[.../.agents/rules/*.md]`). These files contain domain-specific, architectural, and situational rules that take precedence over general instructions. Do this before making any implementation decisions.


## Environment Variables
If a configuration or variable relates to the environment (e.g., database URLs, credentials, external API keys, secrets), it **MUST** be loaded from `.env` and `properties` files. Under no circumstances should environment-specific configurations be hardcoded in the source code.

## Error Handling & Exception Throwing
When handling business logic errors, **MUST ALWAYS** throw `HttpException` (e.g., `throw new HttpException(HttpStatus.BAD_REQUEST, MessageCodeConstant.SOME_ERROR)`) directly from the Service layer or Utility classes. 
- **DO NOT** manually construct and return error responses (e.g., `ResponseEntity.status(400).body(...)`) inside Controllers.
- Let the `GlobalExceptionHandler` catch the `HttpException` and automatically format it into the standard `ApiResponse` format.
- The `error.code` passed to `HttpException` **MUST** be defined in `com.tronhanh.constant.MessageCodeConstant` and **MUST** have a corresponding message definition in the application's message resources (e.g., `messages.properties`).

## Entity Design & Modeling
All JPA entity classes (mapped to database tables) **MUST** extend the `com.tronhanh.entity.BaseEntity` class. This ensures all tables consistently include auditing fields (`created_at`, `updated_at`, `created_by`, `updated_by`), soft-delete logic, and optimistic locking (`version`). Do not define these fields manually in individual entities.

## Code Refactoring & Helpers
When writing code inside the Service layer, if a method becomes too long or contains complex, isolated logic, **MUST** extract that logic into a separate helper class. These helper classes **MUST** be placed in the `com.tronhanh.helper` package. This keeps Service classes clean, readable, and focused solely on orchestration.

## Code Documentation & Comments
Inside any method containing business logic, you **MUST** include single-line comments (`// ...`) to explain the purpose of individual code blocks or complex logic. Do not write code without brief, clear inline documentation explaining *why* and *what* the logic is doing step-by-step.

## JPA Search & Specifications
When implementing searching or filtering logic using JPA, you **MUST** read and utilize the classes inside the `com.tronhanh.dto.specification` package. Do not write raw queries or redundant search logic if a standard specification pattern is already provided in this folder.

## Code Generation & Verification
After writing or generating any code, you **MUST ALWAYS** run the build process (e.g., `./mvnw clean compile` or `./mvnw test-compile`) to verify that the source code compiles successfully without any syntax or dependency errors. If there are compilation errors, you must immediately fix them while ensuring all project rules are still strictly followed. Do not hand over code that fails to compile.
