@echo off
SET "GROUP_ID=org.opendds"
SET "VERSION=3.28.1"
rem i2jrt
call mvn install:install-file -Dfile=.\i2jrt.jar -DgroupId="%GROUP_ID%" -DartifactId=i2jrt -Dversion="%VERSION%" -Dpackaging=jar -DgeneratePom=true
echo i2jrt OK
rem i2jrt_corba
call mvn install:install-file -Dfile=.\i2jrt_corba.jar -DgroupId="%GROUP_ID%" -DartifactId=i2jrt_corba -Dversion="%VERSION%" -Dpackaging=jar -DgeneratePom=true
echo i2jrt_corba OK
rem  OpenDDS_DCPS
call mvn install:install-file -Dfile=.\OpenDDS_DCPS.jar -DgroupId="%GROUP_ID%" -DartifactId=OpenDDS_DCPS -Dversion="%VERSION%" -Dpackaging=jar -DgeneratePom=true
echo OpenDDS_DCPS OK
rem tao_java
call mvn install:install-file -Dfile=.\tao_java.jar -DgroupId="%GROUP_ID%" -DartifactId=tao_java -Dversion="%VERSION%" -Dpackaging=jar -DgeneratePom=true
echo tao_java OK