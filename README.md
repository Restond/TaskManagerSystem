# TaskManagerSystem

## 项目简介
TaskManagerSystem 是一个基于 Spring Boot 构建的任务管理系统，支持用户认证与权限管理。您可以创建、更新、删除和查询任务，并对任务按状态、优先级、截止日期进行筛选。系统安全性通过 JWT 技术实现，并包含完整的异常处理逻辑。

## 主要特性
- 用户注册与登录（JWT 认证）
- 任务创建、更新、删除、查询
- 按完成状态、优先级筛选任务
- 任务截止日期筛选
- 细致的权限控制与安全配置
- 全面异常处理机制

## 技术栈
- Java 17+
- Spring Boot
- Spring Security
- JWT（JSON Web Token）认证
- JPA（Hibernate）
- Lombok

## 安装与启动
1. **克隆项目**
   ```bash
   git clone https://github.com/Restond/TaskManagerSystem.git
   ```
2. **进入项目目录**
   ```bash
   cd TaskManagerSystem
   ```
3. **构建项目**
   ```bash
   ./mvnw clean install
   ```
4. **启动服务**
   ```bash
   ./mvnw spring-boot:run
   ```
   默认服务端口：`8080`

## 快速使用

### 用户注册
向 `/auth/register` 发送 POST 请求，提交用户名、邮箱和密码即可注册。

### 用户登录
向 `/auth/login` 发送 POST 请求，获得 JWT Token，后续请求需携带该 Token。

### 任务管理
主要 API 路径均为 `/TaskManagerSystem/tasks` 开头，具体见下：

- `POST /tasks` 新建任务
- `GET /tasks` 查询所有任务
- `PUT /tasks/{id}` 更新任务
- `DELETE /tasks/{id}` 删除任务
- `GET /tasks/status/{completed}` 按状态查询任务
- `GET /tasks/priority/{priority}` 按优先级查询任务
- `GET /tasks/due/{dueDateBefore}` 查询截止日期前的任务

## 任务实体示例

```java
public class Task {
    private Long id;
    private LocalDate dueDate;
    private int priority;
    private String title;
    private String description;
    private Boolean completed;
}
```

## 安全配置说明
- `/auth/**` 路径开放访问。
- `/TaskManagerSystem/**` 路径需 JWT 认证。
- 使用 BCrypt 加密密码。

## 许可证

MIT License

---

如需二次开发或提建议，欢迎 Issue 或 PR 交流！