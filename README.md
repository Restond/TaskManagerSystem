# Task Manager System

一个基于 Spring Boot 的现代化任务管理系统，提供完整的用户认证和任务管理功能。

---

## 📋 项目简介
Task Manager System 是一个功能完整的 RESTful API 服务，用于个人或团队的任务管理。系统采用 JWT 进行身份认证，支持用户注册、登录、任务创建、查询、更新和删除等操作。

---

## 🚀 技术栈

**后端框架**
- Spring Boot 3.5.7 - 核心应用框架
- Spring Security - 安全认证和授权
- Spring Data JPA - 数据持久层
- JWT (JJWT) - JSON Web Token 认证

**数据库**
- PostgreSQL - 主数据库
- Hibernate - ORM 框架

**开发工具**
- Gradle - 项目构建工具
- Lombok - 代码简化库
- Spring Boot Actuator - 应用监控

---

## ✨ 功能特性

### 🔐 用户认证
- 用户注册（默认角色：ROLE_USER）
- JWT 令牌登录认证
- 密码加密存储（BCrypt）
- 角色权限管理

### 📝 任务管理
- 创建新任务（防止标题重复）
- 查询任务（支持多种条件）
- 更新任务信息
- 删除任务
- 按状态、优先级、截止日期筛选

### 🛡️ 安全特性
- JWT 令牌认证（10小时有效期）
- 密码加密存储
- 基于角色的访问控制
- 无状态会话管理
- CSRF 防护禁用（API场景适用）

---

## 🏗️ 项目结构
```
src/main/java/com/restond/
├── config/           # 配置类
│   └── SecurityConfig.java
├── controller/       # REST 控制器
│   ├── AuthController.java
│   └── TaskController.java
├── entity/           # 数据实体
│   ├── User.java
│   └── Task.java
├── repository/       # 数据访问层
│   ├── UserRepository.java
│   └── TaskRepository.java
├── service/          # 业务逻辑层
│   ├── UserService.java
│   ├── TaskService.java
│   └── CustomUserDetailsService.java
├── security/         # 安全相关
│   ├── JwtUtil.java
│   ├── JwtAuthenticationFilter.java
│   └── SecurityContextTaskDecorator.java
└── exception/        # 异常处理
    ├── GlobalExceptionHandler.java
    ├── TaskAlreadyExistsException.java
    └── TaskNotFoundException.java
```

---

## 🚀 快速开始

### 环境要求

- Java 17+
- PostgreSQL 12+
- Gradle 7+

### 安装步骤

**克隆项目**
```bash
git clone <repository-url>
cd task-manager-system
```

**数据库配置**
```sql
-- 创建数据库
CREATE DATABASE TaskManagerSystem;
```

**配置文件**
在 `application.properties` 中配置数据库连接：

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/TaskManagerSystem
spring.datasource.username=your-username
spring.datasource.password=your-password
```

**构建项目**
```bash
./gradlew build
```

**运行应用**
```bash
./gradlew bootRun
```

应用将在 [http://localhost:8080](http://localhost:8080) 启动。

---

## 📚 API 文档

### 认证接口

#### 用户注册
```http
POST /auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com"
}
```
**响应：**
```json
{
  "message": "用户注册成功",
  "username": "testuser"
}
```

#### 用户登录
```http
POST /auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```
**响应：**
```json
{
  "username": "testuser",
  "message": "登录成功",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### 任务管理接口

> 注意: 所有任务接口都需要在请求头中添加 JWT 令牌：
>
> `Authorization: Bearer <your-jwt-token>`

#### 创建任务
```http
POST /TaskManagerSystem/tasks
Content-Type: application/json

{
  "title": "完成项目文档",
  "description": "编写完整的项目文档",
  "priority": 2,
  "dueDate": "2024-12-31",
  "completed": false
}
```

#### 查询所有任务
```http
GET /TaskManagerSystem/tasks
```

#### 按ID查询任务
```http
GET /TaskManagerSystem/tasks/{taskId}
```

#### 按状态查询任务
```http
GET /TaskManagerSystem/tasks/status/{completed}
```

#### 按优先级查询任务
```http
GET /TaskManagerSystem/tasks/priority/{priority}
```

#### 按截止日期查询任务
```http
GET /TaskManagerSystem/tasks/due/{dueDateBefore}
```

#### 更新任务
```http
PUT /TaskManagerSystem/tasks/{taskId}
Content-Type: application/json

{
  "title": "更新后的任务标题",
  "description": "更新后的描述",
  "priority": 1,
  "dueDate": "2024-12-25",
  "completed": true
}
```

#### 删除任务
```http
DELETE /TaskManagerSystem/tasks/{taskId}
```

---

## 🔧 配置说明

**主要配置项**
```properties
# 服务器配置
server.port=8080

# 数据库配置
spring.datasource.url=jdbc:postgresql://localhost:5432/TaskManagerSystem
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT 密钥（生产环境请修改）
jwt.secret=your-secret-key-that-is-at-least-32-bytes-long!

# 日志配置
logging.level.com.restond.security=DEBUG
```

---

## 🐛 异常处理

系统提供统一的异常处理机制：

- 400 Bad Request - 请求参数错误
- 401 Unauthorized - 认证失败
- 404 Not Found - 资源不存在
- 409 Conflict - 资源冲突（如任务已存在）
- 500 Internal Server Error - 服务器内部错误

**错误响应格式：**
```json
{
  "code": 404,
  "message": "任务不存在: 123",
  "success": false
}
```

---

## 🧪 测试

运行测试套件：
```bash
./gradlew test
```

---

## 📊 监控

应用集成了 Spring Boot Actuator，可通过以下端点监控应用状态：

```http
GET /actuator/health
GET /actuator/info
```

---

## 🔒 安全说明

- 密码使用 BCrypt 加密存储
- JWT 令牌有效期为 10 小时
- 所有任务管理接口需要有效 JWT 令牌
- 认证接口 (/auth/**) 公开访问

---

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

---

## 📄 许可证

本项目采用 MIT 许可证 - 查看 LICENSE 文件了解详情。

---

## 👥 作者

- **restond**
- Email: your-email@example.com
- GitHub: [@restond](https://github.com/restond)

---

## 🙏 致谢

感谢以下开源项目：

- Spring Boot
- Spring Security
- JJWT

如有问题，请提交 Issue。