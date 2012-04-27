package omoikane.sistema;

import omoikane.entities.Usuario;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;


import static org.junit.Assert.assertThat

import org.apache.log4j.Logger
import omoikane.repository.UsuarioRepo;

/**
 * Created by IntelliJ IDEA.
 * User: octavioruizcastillo
 * Date: 25/07/11
 * Time: 18:55
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=["classpath:applicationContext.xml"])
@Transactional
@TransactionConfiguration(defaultRollback=false)
public class UsuarioRepoTest extends GroovyTestCase/*AbstractTransactionalJUnit4SpringContextTests*/ {

    @Autowired
    UsuarioRepo usuarioRepo;

    Usuario usuario;
    private static Validator validator;

    public static Logger logger        = Logger.getLogger(UsuarioRepoTest.class);

    @Before
    public void setUp() {
        usuario = new Usuario();
        usuario.setNombre("Chipo");
        usuario.setHuella1(new byte[0]);
        usuario.setHuella2(new byte[0]);
        usuario.setHuella3(new byte[0]);
        usuario.setNip(1234);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @Transactional
    public void crearUsuario() {
        Usuario usuarioGuardado = usuarioRepo.save(usuario);
        assertUsuario(usuarioGuardado);
    }

    @Test
    public void buscarTodos() {
        List<Usuario> users = usuarioRepo.findAll();
        users.each {
          logger.info( "Usuario: "+it.nombre );
        }
        def countUsers = usuarioRepo.count();
        assert users.size() == countUsers;
    }

    @Test
    public void assertValidacionNombre() {
        usuario.setNombre("");
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertEquals(1, violations.size());
        assertEquals("¡El nombre es importante! No puede estár vacío", violations.iterator().next().getMessage());

        usuario.setNombre("Peluchita Peluches");
        violations = validator.validate(usuario);
        assertEquals(0, violations.size());
    }

    public void assertValidacionNIP() {
        usuario.setNip(123);
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertEquals(1, violations.size());
        assertEquals("El nip debe ser un número de 4 dígitos", violations.iterator().next().getMessage());

        usuario.setNip(12345);
        assertEquals("El nip debe ser un número de 4 dígitos", violations.iterator().next().getMessage());

        usuario.setNip(1234);
        violations = validator.validate(usuario);
        assertEquals(0, violations.size());
    }

    private void assertUsuario(Usuario usuarioGuardado) {
        Usuario user2 = usuarioRepo.readByPrimaryKey(usuarioGuardado.getId());
        assertThat(user2, is(notNullValue()));
        assertThat(user2.getId(), is(not(equalTo(null))));
        assertThat(user2.getNombre(), equalTo(usuario.getNombre()));
    }
}
