/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.dotsboxes.entities;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Esteban
 */
@Entity
@Table(name = "equipos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Equipos.findAll", query = "SELECT e FROM Equipos e"),
    @NamedQuery(name = "Equipos.findById", query = "SELECT e FROM Equipos e WHERE e.id = :id"),
    @NamedQuery(name = "Equipos.findByNombre", query = "SELECT e FROM Equipos e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Equipos.findByRanking", query = "SELECT e FROM Equipos e WHERE e.ranking = :ranking")})
public class Equipos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "ranking")
    private Integer ranking;
    @OneToMany(mappedBy = "equipo")
    private Collection<Miembrosequipo> miembrosequipoCollection;
    @OneToMany(mappedBy = "equipo")
    private Collection<Jugadores> jugadoresCollection;

    public Equipos() {
    }

    public Equipos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
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
        if (!(object instanceof Equipos)) {
            return false;
        }
        Equipos other = (Equipos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tk.dotsboxes.dotsboxesfinal.Equipos[ id=" + id + " ]";
    }
    
}
