@echo off
for /f %%G in (shutdown.pid) do (SET PID=%%G)
echo %PID%
taskkill /PID %PID% /f
timeout 4