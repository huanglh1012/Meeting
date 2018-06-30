@echo off  
set "Ymd=%date:~,4%%date:~5,2%%date:~8,2%"  
mysqldump --opt -u root --password=root meeting > D:/meeting_%Ymd%.sql
@echo on