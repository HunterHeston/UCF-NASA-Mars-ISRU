:: Run Environment Test with Pitch pRTI
:: Version 1.1
:: Authors: Immanuel Neuman and Greg Yeutter

cd lib

java -cp C:\RTI\prti1516e_4.4.2.0_64bit\lib\prti1516e.jar;C:\SISO\Environment\lib\jargs.jar;C:\SISO\Environment\jat\lib\jatcore.jar;C:\SISO\Environment\jat\lib\jatcoreNOSA.jar;C:\SISO\Environment\lib\Environment.jar siso.smackdown.environment.EnvironmentTest

:: -r 30  :: runtime of 30 seconds
:: PAUSE  :: for troubleshooting