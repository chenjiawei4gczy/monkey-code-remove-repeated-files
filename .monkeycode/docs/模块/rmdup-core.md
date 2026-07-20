# rmdup 核心模块

`com.monkeycode.rmdup` 包包含 rmdup 工具的全部源代码，由 7 个 Java 类组成，分为数据模型层和业务逻辑层。

## 结构

```
com/monkeycode/rmdup/
├── App.java              # 应用入口
├── FileKey.java          # 文件键数据类
├── MoveResult.java       # 移动结果密封类型
├── Report.java           # 执行报告
├── FileIndex.java        # 文件索引
├── FileScanner.java      # 文件扫描器
├── DuplicateFinder.java  # 重复检测器
└── FileMover.java        # 文件移动器
```

## 关键文件

| 文件 | 目的 |
|------|------|
| `App.java` | 入口协调器，解析参数、校验输入、串联各组件完成完整流程 |
| `FileIndex.java` | 遍历目录B建立 HashMap 索引，提供 O(1) 重复查询 |
| `DuplicateFinder.java` | 遍历目录A，对每个文件构建 FileKey 并查询 FileIndex |
| `FileMover.java` | 计算相对路径、创建目标目录、执行原子文件移动 |

## 依赖

**内部依赖关系**：
```
App
├── FileIndex ── FileScanner, FileKey
├── DuplicateFinder ── FileScanner, FileIndex, FileKey
├── FileMover ── MoveResult
└── Report ── MoveResult
```

**外部依赖**：无，仅使用 Java 17 标准库 `java.nio.file`

## 规范

### 文件命名
- 每个类一个文件，文件名与 public 类名一致（PascalCase）
- 数据类位于包顶层，不从属于某个"模块"子包

### 错误处理
- 遍历过程：对不可读文件记录 Warning 并跳过
- 移动过程：记录 Failure 并继续处理剩余文件
- 顶层错误（参数错误、目录不存在）：输出错误信息并退出码 1
