/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

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
@Table(name = "pendientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pendientes.findAll", query = "SELECT p FROM Pendientes p"),
    @NamedQuery(name = "Pendientes.findById", query = "SELECT p FROM Pendientes p WHERE p.id = :id"),
    @NamedQuery(name = "Pendientes.findByTurno", query = "SELECT p FROM Pendientes p WHERE p.turno = :turno")})
public class Pendientes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "turno")
    private Boolean turno;
    @JoinColumn(name = "jugador", referencedColumnName = "id")
    @ManyToOne
    private Usuarios jugador;
    @JoinColumn(name = "juego", referencedColumnName = "id")
    @ManyToOne
    private Juegos juego;

    public Pendientes() {
    }

    public Pendientes(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getTurno() {
        return turno;
    }

    public void setTurno(Boolean turno) {
        this.turno = turno;
    }

    public Usuarios getJugador() {
        return jugador;
    }

    public void setJugador(Usuarios jugador) {
        this.jugador = jugador;
    }

    public Juegos getJuego() {
        return juego;
    }

    public void setJuego(Juegos juego) {
        this.juego = juego;
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
        if (!(object instanceof Pendientes)) {
            return false;
        }
        Pendientes other = (Pendientes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tk.dotsboxes.dotsboxesfinal.Pendientes[ id=" + id + " ]";
    }
    
}
