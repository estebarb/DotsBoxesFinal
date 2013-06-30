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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Esteban
 */
@Entity
@Table(name = "jugadores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jugadores.findAll", query = "SELECT j FROM Jugadores j"),
    @NamedQuery(name = "Jugadores.findById", query = "SELECT j FROM Jugadores j WHERE j.id = :id"),
    @NamedQuery(name = "Jugadores.findByType", query = "SELECT j FROM Jugadores j WHERE j.type = :type")})
public class Jugadores implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "type")
    private Integer type;
    @OneToMany(mappedBy = "ganador")
    private Collection<Juegos> juegosCollection;
    @OneToMany(mappedBy = "jugador")
    private Collection<Jugadoresjuego> jugadoresjuegoCollection;
    @OneToMany(mappedBy = "jugador")
    private Collection<Participantestorneo> participantestorneoCollection;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuario;
    @JoinColumn(name = "jugadorpc", referencedColumnName = "id")
    @ManyToOne
    private Jugadorespc jugadorpc;
    @JoinColumn(name = "ganador", referencedColumnName = "id")
    @ManyToOne
    private Ganadores ganador;
    @JoinColumn(name = "equipo", referencedColumnName = "id")
    @ManyToOne
    private Equipos equipo;

    public Jugadores() {
    }

    public Jugadores(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @XmlTransient
    public Collection<Juegos> getJuegosCollection() {
        return juegosCollection;
    }

    public void setJuegosCollection(Collection<Juegos> juegosCollection) {
        this.juegosCollection = juegosCollection;
    }

    @XmlTransient
    public Collection<Jugadoresjuego> getJugadoresjuegoCollection() {
        return jugadoresjuegoCollection;
    }

    public void setJugadoresjuegoCollection(Collection<Jugadoresjuego> jugadoresjuegoCollection) {
        this.jugadoresjuegoCollection = jugadoresjuegoCollection;
    }

    @XmlTransient
    public Collection<Participantestorneo> getParticipantestorneoCollection() {
        return participantestorneoCollection;
    }

    public void setParticipantestorneoCollection(Collection<Participantestorneo> participantestorneoCollection) {
        this.participantestorneoCollection = participantestorneoCollection;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Jugadorespc getJugadorpc() {
        return jugadorpc;
    }

    public void setJugadorpc(Jugadorespc jugadorpc) {
        this.jugadorpc = jugadorpc;
    }

    public Ganadores getGanador() {
        return ganador;
    }

    public void setGanador(Ganadores ganador) {
        this.ganador = ganador;
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
        if (!(object instanceof Jugadores)) {
            return false;
        }
        Jugadores other = (Jugadores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tk.dotsboxes.dotsboxesfinal.Jugadores[ id=" + id + " ]";
    }
    
}
