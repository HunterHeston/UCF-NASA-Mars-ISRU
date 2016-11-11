:: Run Environment Federate with Mak RTI
:: Version 1.0
:: Authors: Immanuel Neumann and Greg Yeutter

cd lib

java -cp rti1516e.jar;jargs.jar;..\jat\lib\jatcore.jar;..\jat\lib\jatcoreNOSA.jar;Environment.jar see.smackdown.environment.Environment

:: -r 30  :: runtime of 30 seconds
PAUSE  :: for troubleshooting