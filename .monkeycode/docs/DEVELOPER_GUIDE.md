# 开发者指南

## 项目目的

rmdup 是一个 Java 命令行工具，用于检测目录A中与目录B重复的文件，并将重复文件移动到目录C。

**核心职责**:
- 递归对比两个目录的文件（按文件名 + 文件大小匹配）
- 将目录A中的重复文件移动到目录C，保留相对路径结构
- 目录B作为只读基准，目录C作为可恢复的回收站

## 环境搭建

### 前置条件

- Java 17 或更高版本
- Maven 3.8 或更高版本

### 安装

```bash
# 克隆仓库
git clone <repo-url>
cd rmdup

# 编译
mvn compile

# 打包为可执行 JAR
mvn package -DskipTests
```

### 运行

```bash
# 开发运行
mvn compile exec:java -Dexec.mainClass="com.monkeycode.rmdup.App" -Dexec.args="<A> <B> <C>"

# 或使用打包好的 JAR
java -jar target/rmdup-1.0.0.jar <目录A> <目录B> <目录C>
```

## 开发工作流

### 代码质量工具

| 工具 | 命令 | 目的 |
|------|------|------|
| Maven Compile | `mvn compile` | 编译检查 |
| Maven Test | `mvn test` | 运行测试 |
| Maven Package | `mvn package -DskipTests` | 打包构建 |

### 分支策略

- `main` - 生产就绪代码
- `feature/*` - 新功能
- `fix/*` - Bug 修复

## 常见任务

### 添加新的判定策略

当前判定标准为「文件名 + 文件大小」。如需添加文件内容哈希判定：

1. 修改 `FileKey` 添加可选的哈希字段
2. 修改 `DuplicateFinder.findDuplicates()` 添加哈希比对逻辑
3. 添加对应的命令行参数控制判定模式

### 添加命令行选项

在 `App.java` 的 `parseArgs()` 方法中扩展参数解析：

```java
// 解析可选参数示例
for (int i = 0; i < args.length; i++) {
    switch (args[i]) {
        case "--verbose" -> verbose = true;
        case "--dry-run" -> dryRun = true;
    }
}
```

### 修复 Bug

1. 编写复现 bug 的测试用例
2. 定位根因
3. 最小改动修复
4. 运行 `mvn test` 验证

## 编码规范

### 文件组织

- 每个类一个文件，文件名与类名一致
- 所有源代码在 `com.monkeycode.rmdup` 包下
- 测试代码镜像源码包结构

### 命名

| 类型 | 约定 | 示例 |
|------|------|------|
| 类 | PascalCase | `DuplicateFinder` |
| 方法 | camelCase | `findDuplicates` |
| 常量 | SCREAMING_SNAKE | `USAGE` |

### 设计模式

本项目采用组件化设计，各组件职责单一：

- **不可变数据**：`FileKey` 使用 Java `record`，自动生成 equals/hashCode
- **密封类型**：`MoveResult` 使用 `sealed interface`，编译时穷尽匹配
- **组合优于继承**：各组件通过依赖注入组合（如 FileIndex 持有 FileScanner 实例）
