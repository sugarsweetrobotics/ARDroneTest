set RTM_JAVA_ROOT=.
set CLASSPATH=bin;%RTM_JAVA_ROOT%\jar\OpenRTM-aist-1.1.0.jar;%RTM_JAVA_ROOT%\jar\commons-cli-1.1.jar
cd /d %~dp0
@rem cd bin
java ARDroneTestComp -f rtc.conf %*
pause;
