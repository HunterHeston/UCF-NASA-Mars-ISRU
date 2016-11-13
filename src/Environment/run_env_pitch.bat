:: Run Environment Federate with Pitch pRTI
:: Version 1.1
:: Authors: Immanuel Neumann and Greg Yeutter

cd lib

java -cp .;"%PITCH_RTIDIR%\lib\prti1516e.jar";jargs.jar;..\jat\lib\jatcore.jar;..\jat\lib\jatcoreNOSA.jar;Environment.jar see.smackdown.environment.Environment

:: -r 30  :: runtime of 30 seconds
 PAUSE  :: for troubleshooting
