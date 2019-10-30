cd /d %~dp0
@ECHO Off
@ECHO Loading Configuration...
@ECHO start renrenfast server...

set BASEPATH=%~dp0
@ECHO javaHome      : "%JAVA_HOME%"
@ECHO basePath      : "%BASEPATH%"
@ECHO classPath     : "%CLASSPATH%"


@ECHO OFF
::Java程序所在的目录（classes的上一级目录）

::需要启动的Java主程序（main方法类）
set APP_MAINCLASS=io.renren.EduApplication
echo "bootstrap class: %APP_MAINCLASS%"
::java虚拟机启动参数
set JAVA_OPTS=-ms512m -mx512m -Xmn256m -Djava.awt.headless=true -Duser.timezone=GMT+08
echo "JVM parameters setting: %JAVA_OPTS%"
echo "Starting %APP_MAINCLASS% ..."
cd %BASEPATH%
cd ../
(java -cp classes;lib/*;conf %JAVA_OPTS% %APP_MAINCLASS% renrenfast:start >> logs/log.log 2>&1) &
echo "Starting %APP_MAINCLASS% success"