SELECT u.uid, u.username, u.password, u.created_time, ur.rid, r.rname, f.fid ,f.url from [User] u
INNER JOIN UserRole ur ON ur.uid = u.uid 
INNER JOIN [Role] r ON r.rid = ur.rid
INNER JOIN RoleFeature rf ON rf.rid = r.rid
INNER JOIN Feature f ON f.fid = rf.fid

 
-- role 
-- 1. admin 2.Director 3. Manager 4.HR 5.Employee

--feature: 
-- 1. /home  2./request/create  3. /request/list  4./request/approved  5. /admin/manager  6./reports/agenda


-- role feature 
-- 1. Admin - 1, 2, 3, 4, 5, 6
--2. Director: - 1, 2, 3, 4, 6
--3. Manager - 1, 2, 3, 4, 6
--4. HR: - 1, 2, 3, 4, 6
--5. Employee: - 1, 2, 3
select * from RequestForLeave
INSERT INTO [RequestForLeave]
           ([created_by]
           ,[created_time]
           ,[processed_by]
           ,[from]
           ,[to]
           ,[status]
           ,[did]
           ,[rtid]
           ,[reason_others]
           ,[processed_time]
           ,[decision_note])
     VALUES (8, SYSUTCDATETIME(), 4, CAST(N'2025-11-04' AS Date), CAST(N'2025-11-05' AS Date), 2, 3, 2, NULL, SYSUTCDATETIME(), N'Từ Chối Vì Thiếu Nhân Sự')
