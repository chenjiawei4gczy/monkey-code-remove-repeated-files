# rmdup 文档

rmdup 是一个 Java 命令行工具，用于检测和移出目录间的重复文件。本文档面向使用者和开发者，涵盖架构设计、接口规范和开发指南。

**快速链接**: [架构](./ARCHITECTURE.md) | [接口](./INTERFACES.md) | [开发者指南](./DEVELOPER_GUIDE.md)

---

## 核心文档

### [架构](./ARCHITECTURE.md)
系统设计、技术栈、组件结构和数据流程。从这里开始了解系统如何运作。

### [接口](./INTERFACES.md)
CLI 命令、参数说明、输出格式和判定规则。使用工具的参考。

### [开发者指南](./DEVELOPER_GUIDE.md)
环境搭建、开发工作流、编码规范和常见任务。贡献者必读。

---

## 模块

| 模块 | 描述 |
|------|------|
| `App.java` | 入口类，参数解析与流程协调 |
| `FileIndex.java` | 目录B文件索引，O(1) 查询 |
| `FileScanner.java` | 递归文件扫描器 |
| `DuplicateFinder.java` | 重复文件检测 |
| `FileMover.java` | 文件移动器，保留相对路径 |
| `Report.java` | 执行报告输出 |
| `FileKey.java` | 文件键数据类 |
| `MoveResult.java` | 移动操作结果密封类型 |

---

## 核心概念

| 概念 | 描述 |
|------|------|
| [重复判定](./专有概念/重复判定.md) | 文件名+文件大小的重复匹配规则 |
| [安全移动](./专有概念/安全移动.md) | 移动而非删除的可逆设计 |

---

## 入门指南

### 使用者？

1. **[接口](./INTERFACES.md)** - 了解命令行用法和输出格式

### 开发者？

1. **[架构](./ARCHITECTURE.md)** - 了解全局结构和组件关系
2. **[开发者指南](./DEVELOPER_GUIDE.md)** - 搭建环境和开发规范

---

## 快速参考

### 命令

```bash
mvn compile                     # 编译
mvn package -DskipTests         # 打包
java -jar target/rmdup-1.0.0.jar <A> <B> <C>   # 运行
```

### 重要文件

| 文件 | 目的 |
|------|------|
| `pom.xml` | Maven 构建配置 |
| `src/main/java/com/monkeycode/rmdup/App.java` | 应用入口 |
| `.monkeycode/specs/remove-duplicate-files/requirements.md` | 需求规格 |
| `.monkeycode/specs/remove-duplicate-files/design.md` | 技术设计 |
