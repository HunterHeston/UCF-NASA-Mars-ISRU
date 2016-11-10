:: Run Environment Federate with Mak RTI
:: Version 1.0
:: Authors: Immanuel Neumann and Greg Yeutter

cd lib

java -cp "%MAK_RTIDIR%\lib\hla.jar";"%SISO_DIR%\lib\jargs.jar";"%SISO_DIR%\jat\lib\jatcore.jar";"%SISO_DIR%\jat\lib\jatcoreNOSA.jar";"%SISO_DIR%\lib\Environment.jar" see.smackdown.environment.Environment 

:: -r 30  :: runtime of 30 seconds
PAUSE  :: for troubleshooting
