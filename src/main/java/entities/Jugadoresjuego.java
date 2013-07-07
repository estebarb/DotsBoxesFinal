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
@Table(name = "jugadoresjuego")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jugadoresjuego.findAll", query = "SELECT j FROM Jugadoresjuego j"),
    @NamedQuery(name = "Jugadoresjuego.findById", query = "SELECT j FROM Jugadoresjuego j WHERE j.id = :id"),
    @NamedQuery(name = "Jugadoresjuego.findByPuntaje", query = "SELECT j FROM Jugadoresjuego j WHERE j.puntaje = :puntaje")})
public class Jugadoresjuego implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "puntaje")
    private Integer puntaje;
    @JoinColumn(name = "jugador", referencedColumnName = "id")
    @ManyToOne
    private Jugadores jugador;
    @JoinColumn(name = "juego", referencedColumnName = "id")
    @ManyToOne
    private Juegos juego;

    public Jugadoresjuego() {
    }

    public Jugadoresjuego(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public Jugadores getJugador() {
        return jugador;
    }

    public void setJugador(Jugadores jugador) {
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
        if (!(object instanceof Jugadoresjuego)) {
            return false;
        }
        Jugadoresjuego other = (Jugadoresjuego) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tk.dotsboxes.dotsboxesfinal.Jugadoresjuego[ id=" + id + " ]";
    }
    
}
