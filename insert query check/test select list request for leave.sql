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




SELECT
                                  r.reqid,
                                  d.dname AS dept_name,
                                  r.created_time,
                                  e_creator.ename AS created_by_name,
                                  r.[from],
                                  r.[to],
                                  rt.rname AS reason_name,
                                  r.reason_others,
                                  r.status,
                                  e_processor.ename AS processed_by_name,
                                  r.processed_time,
                                  r.decision_note
                                , e_creator.did AS created_by_did
                                , creator_dept.dname AS created_by_dept_name
                                , e_creator.ManagerID AS created_by_ManagerID
                                , e_processor.did AS processed_by_did
                                , processor_dept.dname AS processed_by_dept_name
                                , e_processor.ManagerID AS processed_by_ManagerID
                              
                              FROM RequestForLeave r
                              INNER JOIN Employee e_creator ON r.created_by = e_creator.eid
                              LEFT JOIN Employee e_processor ON r.processed_by = e_processor.eid
                              INNER JOIN ReasonType rt ON r.rtid = rt.rtid
                              INNER JOIN Department d ON r.did = d.did
                              LEFT JOIN Department creator_dept ON e_creator.did = creator_dept.did
                              LEFT JOIN Department processor_dept ON e_processor.did = processor_dept.did
                              WHERE r.created_by = 6
                              ORDER BY r.reqid DESC
                                        
                                       SELECT COUNT(*) FROM RequestForLeave WHERE did = 2; 




									   USE [PRJ301Assignment]
GO

-- BẮT ĐẦU INSERT 12 NHÂN VIÊN MỚI
-- Mỗi phòng: 1 Trưởng phòng (ManagerID = NULL) + 2 Nhân viên (ManagerID = ID trưởng phòng)
-- Sử dụng biến để lấy ID tự động

DECLARE @eid INT, @uid INT;

-- ==================== PHÒNG IT (did=2) ====================
-- Trưởng phòng IT: Nguyễn Văn X (ManagerID = NULL)
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Nguyễn Văn X', '1985-05-15', N'123 Láng Hạ, Hà Nội', '0901234567', 2, 'x.it@gmail.com', 0, NULL);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('x.it', '123', N'Nguyễn Văn X', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 3);  -- Manager

-- Nhân viên IT 1: Trần Thị Y (ManagerID = @eid của X)
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Trần Thị Y', '1995-07-20', N'45 Nguyễn Trãi, Hà Nội', '0912345678', 2, 'y.it@gmail.com', 1, @eid);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('y.it', '123', N'Trần Thị Y', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 5);  -- Employee

-- Nhân viên IT 2: Lê Văn Z (ManagerID = @eid của X? Không, dùng ID trưởng phòng ban đầu – giả sử anh lưu ID trưởng phòng đầu tiên ở biến riêng nếu cần, nhưng ở đây dùng ID X ban đầu – em giả định anh chạy sequential)
-- Để chính xác, lưu ID trưởng phòng
DECLARE @it_manager_eid INT = (SELECT MAX(eid) FROM Employee WHERE did = 2 AND ManagerID IS NULL);  -- Lấy ID X

INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Lê Văn Z', '1998-03-10', N'78 Kim Mã, Hà Nội', '0923456789', 2, 'z.it@gmail.com', 0, @it_manager_eid);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('z.it', '123', N'Lê Văn Z', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 5);  -- Employee

-- ==================== PHÒNG MARKETING (did=3) ====================
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Phạm Thị M', '1987-09-12', N'56 Cầu Giấy, Hà Nội', '0934567890', 3, 'm.mkt@gmail.com', 1, NULL);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('m.mkt', '123', N'Phạm Thị M', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 3);  -- Manager

-- Nhân viên Marketing 1: Hoàng Văn N (ManagerID = @eid của M)
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Hoàng Văn N', '1996-11-25', N'89 Tây Sơn, Hà Nội', '0945678901', 3, 'n.mkt@gmail.com', 0, @eid);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('n.mkt', '123', N'Hoàng Văn N', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 5);

-- Nhân viên Marketing 2: Đỗ Thị P
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Đỗ Thị P', '1999-02-18', N'101 Phạm Văn Đồng, Hà Nội', '0956789012', 3, 'p.mkt@gmail.com', 1, @eid);  -- ManagerID = ID M
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('p.mkt', '123', N'Đỗ Thị P', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 5);

-- ==================== PHÒNG FINANCE (did=4) ====================
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Vũ Văn Q', '1983-06-30', N'234 Nguyễn Lương Bằng, Hà Nội', '0967890123', 4, 'q.fin@gmail.com', 0, NULL);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('q.fin', '123', N'Vũ Văn Q', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 3);

-- Nhân viên Finance 1: Bùi Thị R (ManagerID = @eid của Q)
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Bùi Thị R', '1994-08-14', N'56 Trần Phú, Hà Nội', '0978901234', 4, 'r.fin@gmail.com', 1, @eid);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('r.fin', '123', N'Bùi Thị R', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 5);

-- Nhân viên Finance 2: Lý Văn S
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Lý Văn S', '1997-12-05', N'78 Lê Văn Lương, Hà Nội', '0989012345', 4, 's.fin@gmail.com', 0, @eid);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('s.fin', '123', N'Lý Văn S', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 5);

-- ==================== PHÒNG HR (did=5) ====================
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Mai Thị T', '1986-04-22', N'90 Hoàng Quốc Việt, Hà Nội', '0990123456', 5, 't.hr@gmail.com', 1, NULL);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('t.hr', '123', N'Mai Thị T', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 3);

-- Nhân viên HR 1: Đinh Văn U (ManagerID = @eid của T)
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Đinh Văn U', '1995-10-08', N'123 Xuân Thủy, Hà Nội', '0901234560', 5, 'u.hr@gmail.com', 0, @eid);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('u.hr', '123', N'Đinh Văn U', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 5);

-- Nhân viên HR 2: Hà Thị V
INSERT INTO [dbo].[Employee] ([ename], [edob], [eaddress], [ephone], [did], [email], [egender], [ManagerID])
VALUES (N'Hà Thị V', '1998-01-30', N'45 Nguyễn Khánh Toàn, Hà Nội', '0912345601', 5, 'v.hr@gmail.com', 1, @eid);
SET @eid = SCOPE_IDENTITY();

INSERT INTO [dbo].[User] ([username], [password], [displayname], [created_time])
VALUES ('v.hr', '123', N'Hà Thị V', SYSUTCDATETIME());
SET @uid = SCOPE_IDENTITY();

INSERT INTO [dbo].[Enrollment] ([eid], [uid], [active]) VALUES (@eid, @uid, 1);
INSERT INTO [dbo].[UserRole] ([uid], [rid]) VALUES (@uid, 5);



INSERT INTO RoleFeature(rid, fid)
SELECT r.rid, f.fid FROM Role r CROSS JOIN Feature f
WHERE f.url IN ('/request/detail','/request/history')
  AND r.rcode IN ('ADMIN','DIR','MAN','HR','EMP'); -- tuỳ chính sách


  SELECT r.reqid, r.[from], r.[to], r.status, r.created_time,
           r.processed_time, r.decision_note, r.reason_others,
           rt.rname AS reason_name,
           e_creator.eid AS created_by_eid, e_creator.ename AS created_by_name, e_creator.ManagerID AS created_by_ManagerID,
           d.did, d.dname AS dept_name,
           e_proc.eid AS processed_by_eid, e_proc.ename AS processed_by_name
    FROM RequestForLeave r
    JOIN ReasonType rt ON rt.rtid = r.rtid
    JOIN Employee e_creator ON e_creator.eid = r.created_by
    JOIN Department d ON d.did = r.did
    LEFT JOIN Employee e_proc ON e_proc.eid = r.processed_by
    WHERE r.reqid = 2

	SELECT r.rcode, f.url

FROM RoleFeature rf
JOIN Role r ON r.rid = rf.rid
JOIN Feature f ON f.fid = rf.fid
WHERE f.url IN ('/request/detail','/request/history')
ORDER BY r.rcode, f.url;


SELECT rid, rcode FROM [Role] WHERE rcode = 'EMP';
SELECT fid, url FROM [Feature] WHERE url IN ('/request/detail', '/request/history');

SELECT r.rname, f.url from [Role] r JOIN RoleFeature rf ON r.rid = rf.rid JOIN Feature f on rf.fid = f.fid





SELECT r.rcode, f.url
FROM [User] u
JOIN UserRole ur ON ur.uid = u.uid
JOIN Role r ON r.rid = ur.rid
JOIN RoleFeature rf ON rf.rid = r.rid
JOIN Feature f ON f.fid = rf.fid
WHERE u.uid = 6
  AND f.url IN ('/request/detail','/request/history')
ORDER BY r.rcode, f.url;


-- 1. Lấy rid của role EMP
SELECT rid FROM [Role] WHERE rcode = 'EMP';

-- 2. Lấy fid của feature /request/detail
SELECT fid FROM [Feature] WHERE url = '/request/detail';

SELECT 1 FROM RoleFeature
    WHERE rid = (SELECT rid FROM [Role] WHERE rcode = 'EMP')
      AND fid = (SELECT fid FROM [Feature] WHERE url = '/request/detail')

	  INSERT INTO RoleFeature(rid, fid)
VALUES (
    (SELECT rid FROM [Role] WHERE rcode = 'EMP'),
    (SELECT fid FROM [Feature] WHERE url = '/request/detail')
);

-- Sửa URL (nếu lệch)
UPDATE Feature SET url = '/request/detail'
WHERE url LIKE '%/request/detail%';  -- cân nhắc điều kiện phù hợp

-- Hoặc tạo mới (nếu thiếu)

IF NOT EXISTS (SELECT 1 FROM Feature WHERE url = '/request/detail')
    INSERT INTO Feature(url) VALUES('/request/detail');


-- đảm bảo Feature tồn tại
IF NOT EXISTS (SELECT 1 FROM Feature WHERE url = '/request/detail')
    INSERT INTO Feature(url) VALUES ('/request/detail');

-- gán cho các role cần thiết
INSERT INTO RoleFeature(rid, fid)
SELECT r.rid, f.fid
FROM Role r
JOIN Feature f ON f.url = '/request/detail'
WHERE r.rcode IN ('EMP','MAN','HR','ADMIN')
  AND NOT EXISTS (
      SELECT 1 FROM RoleFeature rf
      WHERE rf.rid = r.rid AND rf.fid = f.fid
  );	









  SELECT r.rcode, f.url
FROM [User] u
JOIN UserRole     ur ON ur.uid = u.uid
JOIN Role         r  ON r.rid = ur.rid          -- << thêm join Role
JOIN RoleFeature  rf ON rf.rid = r.rid
JOIN Feature      f  ON f.fid = rf.fid
WHERE u.username = 'a2'
  AND f.url IN ('/request/detail','/request/history')
ORDER BY f.url, r.rcode;








-- 1) Đơn có created_by không có trong Employee
SELECT r.*
FROM RequestForLeave r
WHERE NOT EXISTS (SELECT 1 FROM Employee e WHERE e.eid = r.created_by);

-- 2) Đơn có did không có trong Department
SELECT r.*
FROM RequestForLeave r
WHERE NOT EXISTS (SELECT 1 FROM Department d WHERE d.did = r.did);

-- 3) Đơn có rtid không có trong ReasonType
SELECT r.*
FROM RequestForLeave r
WHERE NOT EXISTS (SELECT 1 FROM ReasonType rt WHERE rt.rtid = r.rtid);


SELECT r.reqid
FROM RequestForLeave r
INNER JOIN Employee   e_creator  ON r.created_by  = e_creator.eid
LEFT  JOIN Employee   e_processor ON r.processed_by = e_processor.eid
INNER JOIN ReasonType rt         ON r.rtid       = rt.rtid
INNER JOIN Department d          ON r.did        = d.did
ORDER BY r.reqid DESC
OFFSET (1 - 1) * 5 ROWS        -- pageindex = 1, pagesize = 5
FETCH NEXT 5 ROWS ONLY;



DECLARE @uid INT = 3; -- uid đang đăng nhập
SELECT u.uid, r.rcode, f.url
FROM [User] u
JOIN UserRole ur ON ur.uid=u.uid
JOIN Role r ON r.rid=ur.rid
JOIN RoleFeature rf ON rf.rid=r.rid
JOIN Feature f ON f.fid=rf.fid
WHERE u.uid=@uid AND f.url IN ('/request/detail', '/request/history');




-- A. Feature tồn tại?
SELECT * FROM Feature WHERE url IN ('/request/detail','/request/history');

-- B. RoleFeature: role nào có 2 feature này?
SELECT r.rcode, f.url
FROM RoleFeature rf
JOIN Role r ON r.rid = rf.rid
JOIN Feature f ON f.fid = rf.fid
WHERE f.url IN ('/request/detail','/request/history')
ORDER BY r.rcode, f.url;

-- C. User hiện đăng nhập (ví dụ uid = 3) có role nào?
SELECT u.uid, u.username, r.rcode
FROM [User] u
JOIN UserRole ur ON ur.uid = u.uid
JOIN Role r ON r.rid = ur.rid
WHERE u.uid = 3; -- thay uid của bạn


-- 2) Gán quyền cho DIR (và MAN tuỳ chọn), tránh trùng bằng NOT EXISTS
INSERT INTO RoleFeature(rid, fid)
SELECT r.rid, f.fid
FROM Role r
JOIN Feature f ON f.url IN ('/request/detail','/request/history')
WHERE r.rcode IN ('DIR')  -- thêm 'MAN' nếu cần: ('DIR','MAN')
  AND NOT EXISTS (
      SELECT 1
      FROM RoleFeature rf
      WHERE rf.rid = r.rid AND rf.fid = f.fid
  );



  -- A. Trạng thái đơn đã đổi chưa?
SELECT reqid, status, processed_by, processed_time, decision_note
FROM RequestForLeave
WHERE reqid = 2;

-- B. Có ghi lịch sử chưa?
SELECT TOP 20 hid, reqid, new_status, changed_at, changed_by_eid, note
FROM LeaveStatusHistory
WHERE reqid = 2
ORDER BY changed_at DESC;