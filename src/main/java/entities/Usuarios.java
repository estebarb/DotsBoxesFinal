/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Esteban
 */
@Entity
@Table(name = "usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u"),
    @NamedQuery(name = "Usuarios.findById", query = "SELECT u FROM Usuarios u WHERE u.id = :id"),
    @NamedQuery(name = "Usuarios.findByEmail", query = "SELECT u FROM Usuarios u WHERE u.email = :email"),
    @NamedQuery(name = "Usuarios.findByNombre", query = "SELECT u FROM Usuarios u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "Usuarios.findByPassword", query = "SELECT u FROM Usuarios u WHERE u.password = :password"),
    @NamedQuery(name = "Usuarios.findBySalt", query = "SELECT u FROM Usuarios u WHERE u.salt = :salt"),
    @NamedQuery(name = "Usuarios.findByLastlogin", query = "SELECT u FROM Usuarios u WHERE u.lastlogin = :lastlogin"),
    @NamedQuery(name = "Usuarios.findByRanking", query = "SELECT u FROM Usuarios u WHERE u.ranking = :ranking")})
public class Usuarios implements Serializable {
    @OneToMany(mappedBy = "amigo")
    private Collection<Amigos> amigosCollection;
    @OneToMany(mappedBy = "persona")
    private Collection<Amigos> amigosCollection1;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 32)
    @Column(name = "password")
    private String password;
    @Size(max = 32)
    @Column(name = "salt")
    private String salt;
    @Column(name = "lastlogin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastlogin;
    @Column(name = "ranking")
    private Integer ranking;
    @OneToMany(mappedBy = "jugador")
    private Collection<Pendientes> pendientesCollection;
    @OneToMany(mappedBy = "administrador")
    private Collection<Torneos> torneosCollection;
    @OneToMany(mappedBy = "jugador")
    private Collection<Miembrosequipo> miembrosequipoCollection;
    @OneToMany(mappedBy = "usuario")
    private Collection<Jugadores> jugadoresCollection;

    public Usuarios() {
    }

    public Usuarios(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    @XmlTransient
    public Collection<Pendientes> getPendientesCollection() {
        return pendientesCollection;
    }

    public void setPendientesCollection(Collection<Pendientes> pendientesCollection) {
        this.pendientesCollection = pendientesCollection;
    }

    @XmlTransient
    public Collection<Torneos> getTorneosCollection() {
        return torneosCollection;
    }

    public void setTorneosCollection(Collection<Torneos> torneosCollection) {
        this.torneosCollection = torneosCollection;
    }

    @XmlTransient
    public Collection<Miembrosequipo> getMiembrosequipoCollection() {
        return miembrosequipoCollection;
    }

    public void setMiembrosequipoCollection(Collection<Miembrosequipo> miembrosequipoCollection) {
        this.miembrosequipoCollection = miembrosequipoCollection;
    }

    @XmlTransient
    public Collection<Jugadores> getJugadoresCollection() {
        return jugadoresCollection;
    }

    public void setJugadoresCollection(Collection<Jugadores> jugadoresCollection) {
        this.jugadoresCollection = jugadoresCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tk.dotsboxes.dotsboxesfinal.Usuarios[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Amigos> getAmigosCollection() {
        return amigosCollection;
    }

    public void setAmigosCollection(Collection<Amigos> amigosCollection) {
        this.amigosCollection = amigosCollection;
    }

    @XmlTransient
    public Collection<Amigos> getAmigosCollection1() {
        return amigosCollection1;
    }

    public void setAmigosCollection1(Collection<Amigos> amigosCollection1) {
        this.amigosCollection1 = amigosCollection1;
    }
    
}
