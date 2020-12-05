# DPS_login



El entorno que se va a utilizar es el siguiente:

-Procesador: Intel(R) Core(TM) i5-6300HQ CPU @ 2.30GHz

-Sistema operativo: Ubuntu 18.04.5 LTS.

-Versión de mysql: mysql  Ver 14.14 Distrib 5.7.32, for Linux (x86_64) using  EditLine wrapper.

-IDE: Visual Studio Code.

-Java version: 11.0.1

-Java(TM) SE Runtime Environment 18.9

-Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.1+13-LTS, mixed mode).

-Tomcat 9.0.27


Primero, copio el código de la página https://www.studytonight.com/servlet/login-system-example-in-servlet.php y lo subo al repositorio, es el primer commit: "Código copiado". 


Se ha añadido la librería servlet-api.jar para que funcionen los imports.
Se ha añadido el jar de https://dev.mysql.com/downloads/connector/j/5.1.html para que funcione la conexión a la base de datos mysql.


Para crear la base de datos en local, ejecutar el script db.sql de la carpeta scripts con el comando: 
$ "mysql -u root -p _password_ < scripts/db.sql" siendo _password_ la contraseña del usuario root de mysql.
El script crea la base de datos correspondiente con el usuario necesario para que funcione de acuerdo con el programa. 


Para la creación del servidor web se utilizará Tomcat. Instalación de Tomcat: https://phoenixnap.com/kb/how-to-install-tomcat-ubuntu

1) Creación de un usuario con su grupo para que Tomcat se ejecute sin usuario con permisos de administrador:
    -$ "sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat"

2) Instalación de Tomcat:
    -$ "wget https://ftp.cixug.es/apache/tomcat/tomcat-9/v9.0.40/bin/apache-tomcat-9.0.40.tar.gz -P /tmp"
    -$ "sudo tar xf /tmp/apache-tomcat-9*.tar.gz -C /opt/tomcat"
    -$ "cd /opt/tomcat"
    -$ "sudo chgrp –R tomcat /opt/tomcat"
    -$ "sudo chmod –R g+r conf"
    -$ "sudo chmod g+x conf"
    -$ "sudo chown –R tomcat webapps/ work temp/ logs"

3) Crear fichero tomcat.service en la ruta /etc/systemd/system/ y cuyo contenido será:
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

Environment=JAVA_HOME=/usr/lib/jvm/java-11-oracle
Environment=CATALINA_PID=/opt/tomcat/latest/temp/tomcat.pid
Environment=CATALINA_HOME=/opt/tomcat
Environment=CATALINA_BASE=/opt/tomcat
Environment=’CATALINA_OPTS=-Xms512M –Xmx1024M –server –XX:+UserParallelGC’
Environment=’JAVA_OPTS=-Djava.awt.headless=true Djava.security.egd=file:/dev/./urandom’

ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh

User=tomcat
Group=tomcat
UMast=0007
RestartSec=10
Restart=always

[Install]
WantedBy=multi-user.target

4) Arrancar el servicio Tomcat
    -$ "sudo systemctl daemon-reload"
    -$ "sudo systemctl start tomcat"
    -$ "sudo systemctl status tomcat"

5) Configurar firewall:
    -$ "sudo ufw allow 8080/tcp"
    -Para comprobar que funcione entrar en http://localhost:8080



Para que funcione correctamente en Tomcat, he creado un soft-link de la carpeta que contiene el código fuente y la he metido en /opt/tomcat/webapps

El framework servlet requiere de las clases de java compiladas para poder enlazarlas correctamente, para ello se han compilado con el comando:
-$ "cd DPS_login/"
-$ "javac -cp /opt/tomcat/webapps/DPS_login/WEB-INF/lib/servlet-api.jar -d WEB-INF/classes/ WEB-INF/src/*.java"

Accediendo a http://localhost:8080/DPS_Login la práctica funcionará.

La clase App.java es para hacer la prueba de conexión y validación a la base de datos. Se debería omitir del resultado final.

A continuación, se pasarán a explicar los fallos de seguridad. Para ello, es muy importante tener actualizados tanto el servidor en el que se
aloja la aplicación como las libererías que se utilizan, puesto que pueden tener lugar fallos de seguridad más allá del código
(por ejemplo, SQL Injection en la función prepareStatement de la clase Connection). 


PARA REPLICAR LA PRÁCTICA:

Descargar el repositorio y moverlo a la carpeta webapps del directorio donde se encuentre instalado tomcat.
Para acceder a él, arrancar el servicio de tomcat y entrar en http://localhost:8080/DPS_login


FALLOS DE SEGURIDAD:

-(ARREGLADO EN CÓDIGO) SEC00-J. Do not allow privileged blocks to leak sensitive information across a trust boundary El tipo de entrada de la contraseña en la página no es "password", por lo que se ve en la pantalla al escribirla.

-(ARREGLADO EN CÓDIGO) MSC00-J. Use SSLSocket rather than Socket for secure data exchange. No utiliza SSL en la conexión con la base de datos, arreglado añadiendo useSSL=true.

-(ARREGLADO EN CÓDIGO) El usuario con el que accede a la base de datos es el usuario root. Arreglado, teniendo únicamente un usuario con permisos a esa base de datos.

-IDS01-J. Normalize strings before validating them: clase Login.java, líneas 13 y 14. Aún sanitizando los Strings con el método prepareStatement, deberían comprobarse antes.

-ENV01-J. Place all security-sensitive code in a single JAR and sign and seal it. El proceso de validar y loggear se encuentran en clases diferentes.

-MSC03-J. Never hard code sensitive information. Se encuentran dirección, usuario y contraseña de la base de datos en la clase Validate.

-La contraseña está almacenada en limpio y no con una función hash. 


