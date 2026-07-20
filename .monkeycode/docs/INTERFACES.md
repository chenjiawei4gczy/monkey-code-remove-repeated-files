# 接口文档

## CLI 命令

### 基本用法

```bash
java -jar rmdup.jar <目录A> <目录B> <目录C>
```

### 参数

| 参数 | 必需 | 描述 |
|------|------|------|
| `<目录A>` | 是 | 源目录，其中的重复文件将被移出 |
| `<目录B>` | 是 | 参考目录，作为比对基准，不会被修改 |
| `<目录C>` | 是 | 目标目录，存放从目录A移出的重复文件 |

### 参数不足时

输出用法提示并退出（退出码 1）：

```
Usage: java -jar rmdup.jar <directoryA> <directoryB> <directoryC>
```

### 目录不存在时

如果目录A或目录B不存在，输出错误并退出（退出码 1）：

```
Error: directory A does not exist: /path/to/A
```

如果目录C不存在，工具自动创建目录C及其所有父目录。

### 退出码

| 退出码 | 含义 |
|--------|------|
| 0 | 操作成功完成，所有文件移动成功或无重复文件 |
| 1 | 发生错误：参数错误、目录不存在、部分文件移动失败 |

## 输出格式

### 有重复文件时

```
Found 2 duplicate file(s). Moving to /tmp/C ...

Moved: sub/d.txt -> sub/d.txt
Moved: b.txt -> b.txt

=== Summary ===
Total files scanned in directory A: 4
Duplicate files found: 2
Files successfully moved: 2
Files failed to move: 0
```

### 无重复文件时

```
No duplicate files found.

=== Summary ===
Total files scanned in directory A: 4
Duplicate files found: 0
Files successfully moved: 0
Files failed to move: 0
```

### 部分失败时

```
Failed: problematic.txt - Permission denied

=== Summary ===
Total files scanned in directory A: 4
Duplicate files found: 2
Files successfully moved: 1
Files failed to move: 1

Failed files:
  /path/to/A/problematic.txt - Permission denied
```

## 判定规则

- 文件被视为"重复"当且仅当：在目录A中的文件与目录B中至少一个文件**文件名相同**且**文件大小(字节数)相同**
- 仅处理普通文件（`Files.isRegularFile()`），跳过目录和符号链接
- 递归遍历所有子目录层级
