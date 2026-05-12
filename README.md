# Notas & Mazmorras
## Guía de despliegue del proyecto
**ADVERTENCIA**  
El proyecto todavía se encuentra en fase de desarrollo, por lo que todavía no tiene ninguna build desplegable.
### Requisitos
Para poder acceder al proyecto, es necesario tener Android Studio instalado en el equipo, así como Git si se quiere clonar el proyecto. También se requiere Visual Studio (O un IDE similar), tener Flutter correctamente configurado y Docker.
### Obtención del código
Para descargar el código del proyecto a tu equipo puedes descargarlo directamente desde el apartado *code* en GitHub, o bien puedes clonar el proyecto con el comando `git clone https://github.com/MelVarela/TFC-DAM.git` en la carpeta donde quieras copiar el proyecto.
### Ejecución
Una vez obtenido el código, puedes seguir los siguientes pasos para ejecutar los diversos módulos del proyecto:
#### Visualizador Web
Para ejecutar el visualizador web basta con abrir el proyecto desde Visual Studio y ejecutar en la consola de comandos el comando `flutter run`, seleccionando después Chrome como plataforma para ejecutarlo.
#### API
Para ejecutar la API, por lo general es suficiente con apretar el botón de ejecutar de tu IDE de preferencia. Ten en cuenta que, si es la primera vez que vas a levantar la API, dará error si no has levantado los contenedores docker con el comando `docker-compose build --up`. La API tampoco logrará iniciarse si ya has levantado los contenedores pero no has iniciado Docker.
#### APP
Para ejecutar la APP de Android simplemente es necesario abrir el código que se encuentra en la carpeta APP con Android Studio y ejecutarlo en el emulador que se tenga configurado.