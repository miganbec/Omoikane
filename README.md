Omoikane
========

Software de punto de venta opensource que utiliza tecnologías libres de alto impacto: Java, MySQL, JavaFX, JasperReports.

![Alt MainMenu](https://github.com/Phesus/Omoikane/raw/gh-pages/images/SS-MainMenu-2013-06-20.png)

## Características
- Opensource. Cualquiera puede extender nuestro código para implementar o modificar funciones en sus negocios, puede ser un excelente punto de partida para negocios que por su tamaño o especialización requiern una solución propia. Ésto siempre que respeten los términos de nuestra licencia :).
- Gratuito. Excelente para pequeños, medianos y grandes negocios que requieran un ahorro en su infraestructura TI. Además las actualizaciones seguirá siendo gratuitas para siempre.
- Diseño oscuro que reduce el cansancio visual de los usuarios
- Tuchscreen y touchless: No solo su diseño es perfectamente apto para terminales touchscreen, además presenta una navegación por teclado, finalmente cuidada para terminales que no tienen touchscreen, ésto es útil en negocios de mucho tráfico.
- Seguridad de transacciones: Una de las características más cuidadas ha sido la protección contra robo por parte del personal que labora en caja.
- Autenticación de usuarios por tecnología biométrica de reconocimiento de huellas dactilares. Por el momento solo disponible para lectores U.Are.U 4000 y 4500.
- Autenticación estándar usando nip. ( Se recomienda usar la autenticación biométrica de ser posible ).
- Multi-código de producto. Cada producto puede tener asignado un código de barras (o interno) e ilimitados códigos secundarios.
- Paquetes de productos. Es posible agrupar distintos productos y en distintas cantidades bajo un mismo nombre y código de producto. Además esa agrupación de productos puede tener un precio especial que no necesariamente es la suma de sus componentes.
- Impuestos. Omoikane es agnóstico en cuanto a impuestos, siempre que los impuestos se calculen como una proporcion del valor del producto. Por ejemplo en México se puede cargar un 0%, 11% o 16% del valor del producto como impuesto.
- Categorización de productos. Es posible categorizar un producto por línea y por grupo. 
- Descuentos por producto, por grupo y por línea. 
- Robusta y confiable base de datos libre y gratuita MySQL. 
- Escalabilidad y capacidad comprobadas: El rendimiento no depende del tamaño del negocio, nuestra aplicación funciona en negocios con más de dos millones de registros y consideramos que puede soportar decenas de millones sin afectar nunca el rendimiento.

## Características agendadas:
- Agrupación por departamento. Actualmente se puede usar la agrupación por líneas como suplemento.
- Descuentos por cliente: Ésta característica se bloqueo hace poco para arreglar detalles.
- Multi-almacén. Estamos analizando de que manera implementar esta característica sin impactar la simplicidad del sistema.
- Descuentos por cantidad. Una solución en la versión actual es usar los paquetes para dar descuentos por cantidad. Es útil para negocios que venden pocos productos en diferentes cantidades.
Además se agendarán las características sugeridas por nuestros usuarios, previo análisis por nuestra parte.

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
