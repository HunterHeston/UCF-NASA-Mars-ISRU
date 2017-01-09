:: Run Environment Federate with Mak RTI
:: Version 1.0
:: Authors: Immanuel Neumann and Greg Yeutter

cd lib

java -cp C:\RTI\makRti4.2_64bit\lib\hla.jar;C:\SISO\Environment_Mak\lib\jargs.jar;C:\SISO\Environment_Mak\jat\lib\jatcore.jar;C:\SISO\Environment_Mak\jat\lib\jatcoreNOSA.jar;C:\SISO\Environment_Mak\lib\Environment.jar siso.smackdown.environment.Environment 

:: -r 30  :: runtime of 30 seconds
PAUSE  :: for troubleshooting
