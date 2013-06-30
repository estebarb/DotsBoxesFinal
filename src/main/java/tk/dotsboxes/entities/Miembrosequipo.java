/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.dotsboxes.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Esteban
 */
@Entity
@Table(name = "miembrosequipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Miembrosequipo.findAll", query = "SELECT m FROM Miembrosequipo m"),
    @NamedQuery(name = "Miembrosequipo.findById", query = "SELECT m FROM Miembrosequipo m WHERE m.id = :id")})
public class Miembrosequipo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "jugador", referencedColumnName = "id")
    @ManyToOne
    private Usuarios jugador;
    @JoinColumn(name = "equipo", referencedColumnName = "id")
    @ManyToOne
    private Equipos equipo;

    public Miembrosequipo() {
    }

    public Miembrosequipo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuarios getJugador() {
        return jugador;
    }

    public void setJugador(Usuarios jugador) {
        this.jugador = jugador;
    }

    public Equipos getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipos equipo) {
        this.equipo = equipo;
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
        if (!(object instanceof Miembrosequipo)) {
            return false;
        }
        Miembrosequipo other = (Miembrosequipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tk.dotsboxes.dotsboxesfinal.Miembrosequipo[ id=" + id + " ]";
    }
    
}
