@echo off
SET "GROUP_ID=org.opensplice.dds.api.dcps.java.saj"
SET "VERSION=1.0"
rem dcpssaj
call mvn install:install-file -Dfile=.\dcpssaj-1.0.jar -DgroupId="%GROUP_ID%" -DartifactId=dcpssaj -Dversion="%VERSION%" -Dpackaging=jar -DgeneratePom=true
echo dcpssaj OK