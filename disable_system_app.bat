@echo off
adb shell pm disable-user com.xcheng.scannere3
adb shell pm disable-user com.xcheng.mdm
adb shell pm disable-user com.xcheng.agingtest
echo done
set /p delBuild=