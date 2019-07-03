# Mojang-Service

Nonblocking mojang haspaid with ktor & corountines!

### Status

Not ready to use yet.

### Config
```
Hostname = Host server can be changed in application.conf
Port = Port server can be changed in application.conf
Nickname = Your nickname to check status.
URL example: http://localhost:8080/api/mojang/haspaid/Otekplay
```

### Usage

##### Kotlin
```
val url = URL("http://localhost:8080/api/mojang/haspaid/Otekplay")
println("Otekplay status is: ${url.readText()}")
```
##### Java
```
URL url = new URL("http://localhost:8080/api/mojang/haspaid/Otekplay");
BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
System.out.println("Otekplay status is: "+in.readLine());
in.close();
```

##### Output: 
```
Otekplay status is: true
```
