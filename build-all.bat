@echo off
echo Building Hotel Management System Services...
echo.

echo Building Service Registry...
cd serviceRegistry\serviceRegistry
call mvnw.cmd clean install -q
if %errorlevel% neq 0 (
    echo ERROR: Service Registry build failed
    exit /b 1
)
cd ..\..

echo Building Auth Service...
cd auth
call mvnw.cmd clean install -q
if %errorlevel% neq 0 (
    echo ERROR: Auth Service build failed
    exit /b 1
)
cd ..

echo Building Room Service...
cd roomService
call mvnw.cmd clean install -q
if %errorlevel% neq 0 (
    echo ERROR: Room Service build failed
    exit /b 1
)
cd ..

echo Building Staff Service...
cd staffService
call mvnw.cmd clean install -q
if %errorlevel% neq 0 (
    echo ERROR: Staff Service build failed
    exit /b 1
)
cd ..

echo Building Booking Service...
cd bookingService
call mvnw.cmd clean install -q
if %errorlevel% neq 0 (
    echo ERROR: Booking Service build failed
    exit /b 1
)
cd ..

echo Building Bill Service...
cd billservice
call mvnw.cmd clean install -q
if %errorlevel% neq 0 (
    echo ERROR: Bill Service build failed
    exit /b 1
)
cd ..

echo Building API Gateway Service...
cd apigatewayservice
call mvnw.cmd clean install -q
if %errorlevel% neq 0 (
    echo ERROR: API Gateway Service build failed
    exit /b 1
)
cd ..

echo.
echo All services built successfully!
echo You can now run individual services or use docker-compose if available.