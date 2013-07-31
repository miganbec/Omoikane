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
1. Descargar el último instalador de omoikane y el archivo 'config.xml' del área de releases
2. Ejecutar el instalador, la instalación se divide en 4 pasos:
3. Instalación de omoikane, se divide en 4 instaladores, lanzado uno después de otro automáticamente: Instalador del Omoikane, Java JRE 7u25, DigitalPersona RTE, driver u.are.u 4500
4. Copiar archivo config.xml en la carpeta de Omoikane
5. Ejecutar el siguiente comando en la línea de comandos:

```
java -classpath omoikane.jar phesus.configuratron.Configurator
```

### Welcome to GitHub Pages.
This automatic page generator is the easiest way to create beautiful pages for all of your projects. Author your page content here using GitHub Flavored Markdown, select a template crafted by a designer, and publish. After your page is generated, you can check out the new branch:

```
$ cd your_repo_root/repo_name
$ git fetch origin
$ git checkout gh-pages
```

If you're using the GitHub for Mac, simply sync your repository and you'll see the new branch.

### Designer Templates
We've crafted some handsome templates for you to use. Go ahead and continue to layouts to browse through them. You can easily go back to edit your page before publishing. After publishing your page, you can revisit the page generator and switch to another theme. Your Page content will be preserved if it remained markdown format.

### Rather Drive Stick?
If you prefer to not use the automatic generator, push a branch named `gh-pages` to your repository to create a page manually. In addition to supporting regular HTML content, GitHub Pages support Jekyll, a simple, blog aware static site generator written by our own Tom Preston-Werner. Jekyll makes it easy to create site-wide headers and footers without having to copy them across every page. It also offers intelligent blog support and other advanced templating features.

### Authors and Contributors
You can @mention a GitHub username to generate a link to their profile. The resulting `<a>` element will link to the contributor's GitHub Profile. For example: In 2007, Chris Wanstrath (@defunkt), PJ Hyett (@pjhyett), and Tom Preston-Werner (@mojombo) founded GitHub.

### Support or Contact
Having trouble with Pages? Check out the documentation at http://help.github.com/pages or contact support@github.com and we’ll help you sort it out.
