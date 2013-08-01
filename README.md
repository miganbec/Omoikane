Omoikane
========

Software de punto de venta opensource que utiliza tecnologías libres de alto impacto: Java, MySQL, JavaFX, JasperReports.

![Alt MainMenu](https://github.com/Phesus/Omoikane/raw/gh-pages/images/SS-MainMenu-2013-06-20.png)

## Instalación
### Requisitos mínimos
- Windows XP y superiores: Si bien la mayor parte del programa es multiplataforma, exíste un componente que solo funciona con windows, estoy trabajando en quitar ese componente (OneTouch SDK).
- Java JRE 7u6
- Lector de huella DigitalPersona u.are.u 4000 o 4500

### Instalación de la base de datos
### Instalación del cliente
1. Descargar el último instalador de omoikane y el archivo 'config.xml' del [área de releases](https://github.com/Phesus/Omoikane/releases)
2. Ejecutar el instalador, la instalación se divide en 4 pasos:
3. Instalación de omoikane, se divide en 4 instaladores, lanzado uno después de otro automáticamente: Instalador del Omoikane, Java JRE 7u25, DigitalPersona RTE, driver u.are.u 4500
4. Copiar archivo config.xml en la carpeta de Omoikane
5. Ejecutar el siguiente comando en la línea de comandos:

```
java -classpath omoikane.jar phesus.configuratron.Configurator
```

Descargas
=========

https://github.com/Phesus/Omoikane/releases
