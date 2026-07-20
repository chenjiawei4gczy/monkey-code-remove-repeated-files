# 需求文档：删除目录中重复文件

## 简介

开发一个 Java 命令行工具，将目录A中与目录B重复的文件移动到目录C。当目录A（含任意层次子目录）中存在与目录B（含任意层次子目录）中文件名和文件大小完全相同的文件时，将该文件从目录A移动到目录C，保留其相对目录结构。

## 术语表

- **目录A**：源目录，其中的重复文件将被移出
- **目录B**：参考目录，作为比对基准，不会被修改
- **目录C**：目标目录，用于存放从目录A移出的重复文件，保留相对路径结构
- **重复文件**：文件名相同且文件大小相同的两个文件
- **遍历**：递归地扫描目录及其所有层次的子目录中的所有文件

## 需求

### R1：命令行参数接收

**User Story:** AS 用户, I want 通过命令行参数指定目录A、目录B和目录C的路径, so that 工具知道需要处理哪些目录。

#### Acceptance Criteria

1. The system SHALL accept a source directory path (目录A), a reference directory path (目录B), and a target directory path (目录C) as command-line arguments in that order.
2. IF any required argument is missing, the system SHALL display usage information and exit with a non-zero status code.
3. IF directory A or directory B does not exist, the system SHALL report the error and exit with a non-zero status code.
4. IF directory C does not exist, the system SHALL create directory C and all necessary parent directories.

### R2：递归文件对比

**User Story:** AS 用户, I want 工具递归遍历两个目录的所有子目录并对比文件, so that 不遗漏任何层次中的重复文件。

#### Acceptance Criteria

1. WHEN the tool runs, the system SHALL recursively traverse directory B and build an index of all files mapping each `(file name, file size)` tuple to the set of relative paths in directory B.
2. WHEN the tool runs, the system SHALL recursively traverse directory A and compare each file against the index built from directory B.
3. The system SHALL identify a file as duplicate when a file in directory A has the same file name and the same file size as at least one file in directory B.
4. The system SHALL only compare regular files, skipping directories and symbolic links.

### R3：移动重复文件到目录C

**User Story:** AS 用户, I want 工具将目录A中发现的重复文件移动到目录C并保留相对路径结构, so that 操作可逆且目录A保持整洁。

#### Acceptance Criteria

1. WHEN a duplicate file is identified in directory A, the system SHALL calculate the file's relative path from directory A.
2. The system SHALL create the corresponding parent directories under directory C as needed.
3. The system SHALL move the file from directory A to the corresponding path under directory C.
4. The system SHALL NOT modify any files in directory B.

### R4：执行报告

**User Story:** AS 用户, I want 工具在执行后输出操作摘要, so that 了解处理结果。

#### Acceptance Criteria

1. WHEN the operation completes, the system SHALL output a summary including: total files scanned in directory A, duplicate files found, files successfully moved, and files that failed to move.
2. WHILE processing, the system SHALL output each file being moved with its source and destination relative paths.

### R5：错误处理与健壮性

**User Story:** AS 用户, I want 工具在遇到个别文件错误时继续处理, so that 单个失败不影响整体任务。

#### Acceptance Criteria

1. IF a file cannot be read during traversal, the system SHALL log a warning and skip that file.
2. IF a file move operation fails, the system SHALL log the error detail and continue processing remaining files.
3. WHEN all processing is complete, the system SHALL exit with code 0 on success, or exit with code 1 if any errors occurred.
