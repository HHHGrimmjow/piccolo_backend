# Piccolo Backend 🎲

> 帮你做选择，投票更有趣！

Piccolo 是一个面向年轻人的选择投票网站后端服务，帮助用户发起投票话题，让朋友们帮你做决定，解决选择困难症。

## 技术栈

- **框架**: Spring Boot 3.1.5
- **语言**: Java 17
- **ORM**: MyBatis-Plus 3.5.4
- **认证**: JWT (jjwt 0.12.3)
- **安全**: Spring Security
- **数据库**: MySQL 8+
- **构建工具**: Maven

## 功能特性

### 核心功能
- ✅ 用户注册/登录（JWT 认证）
- ✅ 发布投票话题（支持图片、多选项、截止时间）
- ✅ 参与投票（每人每话题限投一票）
- ✅ 评论系统（支持嵌套回复）
- ✅ 投票历史查看
- ✅ 个人中心（资料编辑、统计数据）

### 特色功能
- 🎲 **随机投票** - 帮选择困难症用户随机选择
- 📊 **实时结果** - 投票后显示百分比和进度条
- 🏆 **排行榜** - 热门话题和活跃用户 TOP 10
- 🔥 **热门排序** - 按投票数排序推荐
- 🔍 **搜索功能** - 关键词搜索话题
- 📤 **图片上传** - 话题支持配图

## 项目结构

```
src/main/java/com/piccolo/
├── common/          # 通用类（统一响应、异常处理、常量）
├── config/          # 配置类（Security、JWT、CORS、MyBatisPlus）
├── controller/      # REST API 控制器
├── dto/             # 请求数据传输对象
├── entity/          # 数据库实体类
├── mapper/          # MyBatis Mapper 接口
├── service/         # 业务逻辑层
│   └── impl/        # Service 实现类
├── util/            # 工具类（JWT）
└── vo/              # 响应视图对象
```

## 数据库设计

### 表结构
- `user` - 用户表
- `topic` - 投票话题表
- `topic_option` - 投票选项表
- `vote_record` - 投票记录表
- `comment` - 评论表

详细建表 SQL 见 `src/main/resources/schema.sql`

## API 接口

### 认证相关
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/login` | 用户登录 |
| GET | `/api/auth/me` | 获取当前用户信息 |

### 话题相关
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/topics` | 话题列表（分页、搜索、排序） |
| GET | `/api/topics/{id}/detail` | 话题详情 |
| POST | `/api/topics` | 创建话题 |
| POST | `/api/topics/{id}/close` | 关闭话题 |

### 投票相关
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/topics/{id}/vote` | 投票 |
| GET | `/api/topics/my/history` | 投票历史 |

### 评论相关
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/topics/{id}/comments` | 获取评论列表 |
| POST | `/api/topics/{id}/comments` | 发表评论 |

### 用户相关
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/users/{id}` | 获取用户信息 |
| PUT | `/api/users/me` | 更新个人资料 |

### 其他
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/rankings` | 排行榜 |
| POST | `/api/upload` | 文件上传 |

## 快速开始

### 1. 环境要求
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 2. 数据库配置
```sql
-- 执行建表脚本
mysql -u root -p < src/main/resources/schema.sql
```

### 3. 修改配置
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/piccolo?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 4. 启动项目
```bash
mvn spring-boot:run
```

服务启动在 `http://localhost:8080`

## 配置说明

### JWT 配置
```yaml
piccolo:
  jwt:
    secret: YourSecretKeyHere  # JWT 签名密钥
    expiration: 86400000        # Token 有效期（毫秒），默认 24 小时
```

### 文件上传配置
```yaml
piccolo:
  upload:
    path: uploads/                    # 上传目录
    allowed-types: jpg,jpeg,png,gif  # 允许的文件类型
```

## 开发计划

- [ ] 成就徽章系统
- [ ] 表情回应功能
- [ ] 分享链接生成
- [ ] 暗色模式 API 支持
- [ ] WebSocket 实时通知
- [ ] 话题标签分类

## License

MIT License
