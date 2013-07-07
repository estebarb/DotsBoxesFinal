/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

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
@Table(name = "jugadorespc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jugadorespc.findAll", query = "SELECT j FROM Jugadorespc j"),
    @NamedQuery(name = "Jugadorespc.findById", query = "SELECT j FROM Jugadorespc j WHERE j.id = :id"),
    @NamedQuery(name = "Jugadorespc.findByNombre", query = "SELECT j FROM Jugadorespc j WHERE j.nombre = :nombre"),
    @NamedQuery(name = "Jugadorespc.findByDescripcion", query = "SELECT j FROM Jugadorespc j WHERE j.descripcion = :descripcion"),
    @NamedQuery(name = "Jugadorespc.findByRanking", query = "SELECT j FROM Jugadorespc j WHERE j.ranking = :ranking")})
public class Jugadorespc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "ranking")
    private Integer ranking;
    @OneToMany(mappedBy = "jugadorpc")
    private Collection<Jugadores> jugadoresCollection;

    public Jugadorespc() {
    }

    public Jugadorespc(Long id) {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
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
        if (!(object instanceof Jugadorespc)) {
            return false;
        }
        Jugadorespc other = (Jugadorespc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tk.dotsboxes.dotsboxesfinal.Jugadorespc[ id=" + id + " ]";
    }
    
}
