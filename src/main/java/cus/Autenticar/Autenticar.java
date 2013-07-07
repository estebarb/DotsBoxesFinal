package cus.Autenticar;

import entities.Usuarios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Arrays;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;
import utils.Base64Conversions;
import utils.EMF;
import utils.EmailValidator;

/**
 * Sistema de autenticación y registro de usuarios Este sistema está basado en
 * la recomendación de OWASP para guardar contraseñas en Java. Está disponible
 * en https://www.owasp.org/index.php/Hashing_Java
 *
 * La mayor parte de los métodos fueron tomados (con modificaciones) de allí.
 *
 * @author Esteban
 */
public class Autenticar {

    @PersistenceContext(unitName = "DotsBoxesPU")
    private EntityManager em;
    private final static int ITERATION_NUMBER = 1000;

    public Autenticar() {
        em = EMF.createEntityManager();
    }
    /**
     * SecretoAplicacion
     *
     * Este secreto se inicializa cada vez que se hace deploy de la aplicación.
     * Permite verificar que los datos de la cookie del usuario realmente fueron
     * validados por este programa.
     */
    private final static String SecretoAplicacion = "0"; // new SecureRandom().nextLong().toString();

    /**
     * Valida que el usuario y el verificationHash declarados por un usuario
     * realmente fueron generados por este programa. El algoritmo es:
     * verificationHash == sha1(userid + sessionID + SecretoAplicacion)
     *
     * @param userid
     * @param sessionID
     * @param verificationHash
     * @return
     */
    public boolean ValidateUser(String userid, byte[] verificationHash) {
        return Arrays.equals(GenerateSessionHash(userid), verificationHash);
    }

    public byte[] GenerateSessionHash(String userid) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();

            byte[] bUserID = Base64Conversions.base64ToByte(userid);
            byte[] bSecretoAplicacion = Base64Conversions.base64ToByte(SecretoAplicacion);

            digest.update(bUserID);
            digest.update(bSecretoAplicacion);
            return digest.digest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Autenticar.class.getName()).log(Level.SEVERE, "No tenemos SHA-1", ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Autenticar.class.getName()).log(Level.SEVERE, "No se pudo convertir a base64", ex);
            return null;
        }
    }

    public Long getUserFromMail(String email) {
        List<Usuarios> users = em.createNamedQuery("Usuarios.findByEmail")
                .setParameter("email", email)
                .getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0).getId();
        }
    }

    /**
     *
     * @param email
     * @param password
     * @return el ID del usuario o null si no existe/no valida
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public Long AutenticarUsuario(String email, String password) throws SQLException, NoSuchAlgorithmException {
        String digest, salt;

        boolean userExist/* = false*/;
        if (email == null || password == null) {
            email = "";
            password = "";
        }

        // esto obtiene la lista de usuarios con el email dado
        List<Usuarios> users = em.createNamedQuery("Usuarios.findByEmail")
                .setParameter("email", email)
                .getResultList();

        // ¿Se encontró el usuario?
        if (!users.isEmpty()) {
            // El usuario sí existe
            digest = users.get(0).getPassword();
            salt = users.get(0).getSalt();
            userExist = true;
        } else {
            // No se encontró el usuario
            // Igual vamos a ejecutar TODO el algoritmo
            digest = "000000000000000000000000000=";
            salt = "00000000000=";
            userExist = false;
        }

        byte[] bDigest;
        byte[] bSalt;
        try {
            bDigest = Base64Conversions.base64ToByte(digest);
            bSalt = Base64Conversions.base64ToByte(salt);
        } catch (IOException ioex) {
            Logger.getLogger(Autenticar.class.getName()).log(Level.SEVERE, "La base de datos es inconsistente o alguien modifico el password", ioex);
            throw new SQLException("La base de datos es inconsistente o alguien modifico el password");
        }

        // Compute the new DIGEST
        byte[] proposedDigest;
        proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);

        if (Arrays.equals(proposedDigest, bDigest) && userExist) {
            // Devuelve el ID del usuario...
            return users.get(0).getId();
        } else {
            return null;
        }
    }

    /**
     * Inserts a new user in the database
     *
     * @param con Connection An open connection to a databse
     * @param login String The login of the user
     * @param password String The password of the user
     * @return boolean Returns true if the login and password are ok (not null
     * and length(login)<=100
     * @throws SQLException If the database is unavailable
     * @throws NoSuchAlgorithmException If the algorithm SHA-1 or the
     * SecureRandom is not supported by the JVM
     */
    public boolean createUser(String email, String name, String password)
            throws SQLException, NoSuchAlgorithmException {
        try {
            if (email != null && password != null && EmailValidator.ValidateEMail(email)) {
                // Primero hay que validar que el email no haya sido usado antes...
                Query query = em.createNamedQuery("Usuarios.findByEmail");
                List<Usuarios> users = query.setParameter("email", email).getResultList();

                // El correo ya había sido usado. NO SE PUEDE
                if (!users.isEmpty()) {
                    return false;
                }

                // Uses a secure Random not a simple Random
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                // Salt generation 64 bits long
                byte[] bSalt = new byte[8];
                random.nextBytes(bSalt);
                // Digest computation
                byte[] bDigest = getHash(ITERATION_NUMBER, password, bSalt);
                String sDigest = Base64Conversions.byteToBase64(bDigest);
                String sSalt = Base64Conversions.byteToBase64(bSalt);

                // Ahora inserta al nuevo usuario en la tabla de Usuarios
                em.getTransaction().begin();
                Usuarios user = new Usuarios();
                user.setEmail(email);
                user.setNombre(name);
                user.setPassword(sDigest);
                user.setSalt(sSalt);
                user.setLastlogin(new java.util.Date());
                user.setRanking(0);
                em.persist(user);
                em.getTransaction().commit();
                return true;
            } else {
                return false;
            }
        } finally {
        }
    }

    /**
     * From a password, a number of iterations and a salt, returns the
     * corresponding digest
     *
     * @param iterationNb int The number of iterations of the algorithm
     * @param password String The password to encrypt
     * @param salt byte[] The salt
     * @return byte[] The digested password
     * @throws NoSuchAlgorithmException If the algorithm doesn't exist
     */
    public byte[] getHash(int iterationNb, String password, byte[] salt) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(salt);
            byte[] input;
            input = digest.digest(password.getBytes("UTF-8"));
            for (int i = 0; i < iterationNb; i++) {
                digest.reset();
                input = digest.digest(input);
            }
            return input;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Autenticar.class.getName()).log(Level.SEVERE, "No tenemos el algoritmo SHA-1", ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Autenticar.class.getName()).log(Level.SEVERE, "No soportamos UTF-8", ex);
        }
        return null;
    }
}
