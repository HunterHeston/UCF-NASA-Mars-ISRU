:: Run Environment Federate with Pitch pRTI
:: Version 1.1
:: Authors: Immanuel Neumann and Greg Yeutter

cd lib

java -cp "%PITCH_RTIDIR%\lib\prti1516e.jar";"%SISO_DIR%\lib\jargs.jar";"%SISO_DIR%\jat\lib\jatcore.jar";"%SISO_DIR%\jat\lib\jatcoreNOSA.jar";"%SISO_DIR%\lib\Environment.jar" see.smackdown.environment.EnvironmentTest 

:: -r 30  :: runtime of 30 seconds
 PAUSE  :: for troubleshooting
