:: Run Environment Test with Pitch pRTI
:: Version 1.0
:: Authors: Immanuel Neumann and Greg Yeutter

cd lib

java -cp C:\RTI\makRti4.2_64bit\lib\hla.jar;C:\SISO\Environment\lib\jargs.jar;C:\SISO\Environment\jat\lib\jatcore.jar;C:\SISO\Environment\jat\lib\jatcoreNOSA.jar;C:\SISO\Environment\lib\Environment.jar siso.smackdown.environment.EnvironmentTest

:: -r 30  :: runtime of 30 seconds
:: PAUSE  :: for troubleshooting