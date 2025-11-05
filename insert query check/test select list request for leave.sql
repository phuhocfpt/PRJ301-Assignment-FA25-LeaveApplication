USE [PRJ301Assignment]
GO

SELECT r.reqid
      ,r.created_by
      ,r.created_time
      ,r.processed_by
      ,r.[from]
      ,r.[to]
      ,r.[status]
      ,r.did
      ,r.rtid
      ,r.reason_others
      ,r.processed_time
      ,r.decision_note
	  ,e_creator.eid AS created_by_eid
	  ,e_creator.ename AS created_by_name
	  ,e_processor.eid AS processed_by_eid
	  ,e_processor.ename AS processed_by_name
	  ,rt.rname AS reason_name
	  ,d.dname AS dept_name

  FROM RequestForLeave r
  INNER JOIN Employee e_creator ON r.created_by = e_creator.eid
  LEFT JOIN Employee e_processor ON r.processed_by = e_processor.eid
  INNER JOIN ReasonType rt ON r.rtid = rt.rtid
  INNER JOIN Department d ON r.did = d.did
  WHERE r.created_by = 1
  ORDER BY r.reqid DESC



  SELECT r.reqid
                                        ,r.created_by
                                        ,r.created_time
                                        ,r.processed_by
                                        ,r.[from]
                                        ,r.[to]
                                        ,r.[status]
                                        ,r.did
                                        ,r.rtid
                                        ,r.reason_others
                                        ,r.processed_time
                                        ,r.decision_note
                                  	  ,e_creator.eid AS created_by_eid
                                  	  ,e_creator.ename AS created_by_name
                                  	  ,e_processor.eid AS processed_by_eid
                                  	  ,e_processor.ename AS processed_by_name
                                  	  ,rt.rname AS reason_name
                                  	  ,d.dname AS dept_name
                                  
                                    FROM RequestForLeave r
                                    INNER JOIN Employee e_creator ON r.created_by = e_creator.eid
                                    LEFT JOIN Employee e_processor ON r.processed_by = e_processor.eid
                                    INNER JOIN ReasonType rt ON r.rtid = rt.rtid
                                    INNER JOIN Department d ON r.did = d.did
                                    WHERE r.did = 2
                                    ORDER BY r.reqid DESC

GO

USE [PRJ301Assignment]
GO

PRINT N'Thêm cột ManagerID vào bảng Employee...'
-- 1. Thêm cột ManagerID (cho phép NULL)
ALTER TABLE [dbo].[Employee]
ADD ManagerID INT NULL
GO

-- 2. Tạo khóa ngoại (Foreign Key) tự tham chiếu
-- (ManagerID của một Employee sẽ trỏ về eid của một Employee khác)
ALTER TABLE [dbo].[Employee] WITH CHECK 
ADD CONSTRAINT [FK_Employee_Manager] FOREIGN KEY([ManagerID])
REFERENCES [dbo].[Employee] ([eid])
GO
ALTER TABLE [dbo].[Employee] CHECK CONSTRAINT [FK_Employee_Manager]
GO

PRINT N'Cập nhật dữ liệu ManagerID cho 10 nhân viên mẫu...'
-- Dựa trên file FullDataInsertScript.sql (5 phòng ban)
-- Giả sử:
-- - Admin (eid 7) và Director (eid 3) là sếp cao nhất (không có sếp)
-- - Manager Marketing (eid 4) và Manager Finance (eid 5) báo cáo cho Director (eid 3)
-- - Manager HR (eid 2) báo cáo cho Director (eid 3)
-- - Các Employee (eid 1, 6) báo cáo cho Manager IT (Giả sử là Admin - eid 7)
-- - Các Employee (eid 8) báo cáo cho Manager Mkt (eid 4)
-- - Các Employee (eid 10) báo cáo cho Manager Fin (eid 5)
-- - Các Employee (eid 11) báo cáo cho Manager HR (eid 2)

UPDATE [dbo].[Employee] SET ManagerID = NULL WHERE [eid] IN (7, 3) -- 7(Admin), 3(Director)
GO
UPDATE [dbo].[Employee] SET ManagerID = 3 WHERE [eid] IN (4, 5, 2) -- 4(Man Mkt), 5(Man Fin), 2(Man HR)
GO
UPDATE [dbo].[Employee] SET ManagerID = 7 WHERE [eid] IN (1, 6) -- Emp IT (1, 6)
GO
UPDATE [dbo].[Employee] SET ManagerID = 4 WHERE [eid] IN (8) -- Emp Mkt (8)
GO
UPDATE [dbo].[Employee] SET ManagerID = 5 WHERE [eid] IN (10) -- Emp Fin (10) - (Lưu ý: eid 10 là Tâm Thị I)
GO
UPDATE [dbo].[Employee] SET ManagerID = 2 WHERE [eid] IN (11) -- Emp HR (11)
GO

PRINT N'Hoàn thành cập nhật CSDL.'
