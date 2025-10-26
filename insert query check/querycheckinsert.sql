/*
TỆP SCRIPT SQL ĐỂ KIỂM TRA (TEST) DỮ LIỆU
Chạy file này sau khi đã chạy FullDataInsertScript.sql
để xem dữ liệu đã vào đúng hay chưa.
*/
USE [PRJ301Assignment]
GO

PRINT N'1. Kiểm tra bảng Department (Phòng ban)...'
SELECT * FROM [dbo].[Department]
GO

PRINT N'2. Kiểm tra bảng Employee (Nhân viên)...'
SELECT * FROM [dbo].[Employee]
GO

PRINT N'3. Kiểm tra bảng Role (Vai trò)...'
SELECT * FROM [dbo].[Role]
GO

PRINT N'4. Kiểm tra bảng Feature (Tính năng)...'
SELECT * FROM [dbo].[Feature]
GO

PRINT N'5. Kiểm tra bảng RoleFeature (Phân quyền)...'
SELECT * FROM [dbo].[RoleFeature]
ORDER BY rid, fid -- Sắp xếp cho dễ nhìn
GO

PRINT N'6. Kiểm tra bảng User (Tài khoản)...'
SELECT * FROM [dbo].[User]
GO

PRINT N'7. Kiểm tra bảng UserRole (Vai trò của User)...'
SELECT * FROM [dbo].[UserRole]
GO

PRINT N'8. Kiểm tra bảng Enrollment (Liên kết User-Employee)...'
SELECT * FROM [dbo].[Enrollment]
GO

PRINT N'9. Kiểm tra bảng ReasonType (Loại lý do nghỉ)...'
SELECT * FROM [dbo].[ReasonType]
GO

PRINT N'10. Kiểm tra bảng RequestForLeave (Các đơn nghỉ phép)...'
SELECT * FROM [dbo].[RequestForLeave]
GO

PRINT N'11. (QUAN TRỌNG) Kiểm tra bảng LeaveStatusHistory (Lịch sử đơn)...'
-- Bảng này phải có 1 dòng, được tạo tự động bởi Trigger
SELECT * FROM [dbo].[LeaveStatusHistory]
GO