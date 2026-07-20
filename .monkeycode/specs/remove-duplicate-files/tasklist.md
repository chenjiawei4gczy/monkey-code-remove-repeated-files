# 需求实施计划

- [x] 1. 设置 Maven 项目结构和构建配置
  - 创建 `pom.xml`，配置 Java 17、maven-jar-plugin、maven-shade-plugin
  - 创建包目录结构 `src/main/java/com/monkeycode/rmdup/`
  - 创建 `src/test/java/com/monkeycode/rmdup/` 测试目录
  - 对应需求：R1 命令行工具整体框架

- [ ] 2. 实现数据模型层
  - [x] 2.1 实现 `FileKey` 记录类（fileName、fileSize），覆盖 equals/hashCode
    - 作为 FileIndex 的键使用，依据设计文档 Data Models 部分
  - [x] 2.2 实现 `MoveResult` 密封接口及其 Success/Failure 记录
    - 作为 FileMover 返回值，依据设计文档 Data Models 部分
  - [x] 2.3 实现 `Report` 类（totalScanned、moved 列表、failed 列表）
    - 含 addMoved()、addFailed()、addScanned()、printSummary() 方法
    - 对应需求：R4 执行报告
  - [ ]* 2.4 为数据模型编写单元测试
    - 测试 FileKey equals/hashCode 正确性
    - 测试 MoveResult pattern matching
    - 测试 Report 统计准确性

- [x] 3. 检查点 - 数据模型编译通过，如有疑问请询问用户

- [ ] 4. 实现 FileIndex（目录B文件索引）
  - [x] 4.1 实现 `build(Path directoryB)` 方法
    - 递归遍历目录B，对每个普通文件创建 FileKey 并关联其相对路径
    - 跳过目录和符号链接
    - 对应需求：R2 AC1
  - [x] 4.2 实现 `contains(FileKey key)` 查询方法
    - 基于 HashMap 实现 O(1) 查询
    - 对应需求：R2 AC3
  - [ ]* 4.3 为 FileIndex 编写单元测试
    - 测试空目录B的索引构建
    - 测试同名同大小文件的索引命中
    - 测试同名不同大小文件的索引不命中

- [ ] 5. 实现 FileScanner（递归文件扫描器）
  - [x] 5.1 实现 `scan(Path directory)` 方法
    - 使用 Files.walk() 递归遍历目录，过滤目录和符号链接
    - 对不可读文件记录警告并跳过，不中断遍历
    - 对应需求：R2 AC4、R5 AC1
  - [ ]* 5.2 为 FileScanner 编写单元测试
    - 使用临时目录测试多层嵌套目录扫描
    - 测试符号链接被跳过
    - 测试不可读文件的容错处理

- [ ] 6. 实现 DuplicateFinder（重复检测器）
  - [x] 6.1 实现 `findDuplicates(Path directoryA, FileIndex indexB)` 方法
    - 扫描目录A中每个文件，构建 FileKey 查询 FileIndex
    - 返回目录A中所有重复文件的路径列表
    - 对应需求：R2 AC2、R2 AC3
  - [ ]* 6.2 为 DuplicateFinder 编写单元测试
    - 测试两个目录文件完全重复的场景
    - 测试部分重复的场景
    - 测试无重复的场景

- [ ] 7. 实现 FileMover（文件移动器）
  - [x] 7.1 实现 `move(Path sourceFile, Path baseA, Path baseC)` 方法
    - 计算 sourceFile 相对于 baseA 的相对路径
    - 在 baseC 下创建对应父目录（Files.createDirectories）
    - 使用 Files.move 执行原子移动
    - 返回 MoveResult（Success 或 Failure）
    - 对应需求：R3 AC1、R3 AC2、R3 AC3
  - [ ]* 7.2 为 FileMover 编写单元测试
    - 测试文件成功移动到目标目录且保留相对路径
    - 测试目标目录自动创建
    - 测试权限不足场景的 Failure 返回

- [x] 8. 检查点 - 所有核心组件编译通过，如有疑问请询问用户

- [ ] 9. 实现 App 入口类
  - [x] 9.1 实现 `parseArgs(String[] args)` 参数解析逻辑
    - 接收 3 个参数：目录A、目录B、目录C
    - 参数不足时输出用法说明并退出
    - 对应需求：R1 AC1、R1 AC2
  - [x] 9.2 实现 `main(String[] args)` 协调整体流程
    - 校验目录A和B存在性，目录C不存在则创建
    - 依次调用 FileIndex.build → DuplicateFinder.findDuplicates → FileMover.move → Report.printSummary
    - 对应需求：R1 AC3、R1 AC4、R5 AC2
  - [x] 9.3 实现退出码逻辑
    - 全部成功返回 0，有任何错误返回 1
    - 对应需求：R5 AC3
  - [ ]* 9.4 编写集成测试
    - 使用临时目录构建完整测试场景
    - 测试多层嵌套子目录的重复文件移动
    - 测试部分文件不重复正确保留
    - 测试目录B文件不被修改的不变性

- [x] 10. 检查点 - 构建可执行 JAR 验证整体功能，如有疑问请询问用户
