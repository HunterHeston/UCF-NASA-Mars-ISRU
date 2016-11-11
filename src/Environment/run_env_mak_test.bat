:: Run Environment Federate with Mak RTI
:: Version 1.0
:: Authors: Immanuel Neumann and Greg Yeutter

set MAK_WIN_LIBS=%MAK_RTIDIR%\bin
set MAK_LIBS=%MAK_RTIDIR%\lib
set JAVA_LIBS=%MAK_RTIDIR%\lib\java
set PATH=%JAVA_LIBS%;%MAK_LIBS%;%MAK_WIN_LIBS%;%PATH%

cd lib

java -cp .;%MAK_RTIDIR%\lib\hla.jar;jargs.jar;..\jat\lib\jatcore.jar;..\jat\lib\jatcoreNOSA.jar;Environment.jar see.smackdown.environment.EnvironmentTest
:: -r 30  :: runtime of 30 seconds
PAUSE  :: for troubleshooting