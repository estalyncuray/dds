@echo off
SET "GROUP_ID=org.opensplice"
rem version 6.7.171127OSS
SET "VERSION=6.7.171127"
rem dcpssaj
call mvn install:install-file -Dfile=.\dcpssaj.jar -DgroupId="%GROUP_ID%" -DartifactId=dcpssaj -Dversion="%VERSION%" -Dpackaging=jar -DgeneratePom=true
echo dcpssaj OK