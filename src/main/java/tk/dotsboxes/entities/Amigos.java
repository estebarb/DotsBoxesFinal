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
@Table(name = "amigos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Amigos.findAll", query = "SELECT a FROM Amigos a"),
    @NamedQuery(name = "Amigos.findById", query = "SELECT a FROM Amigos a WHERE a.id = :id")})
public class Amigos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "amigo", referencedColumnName = "id")
    @ManyToOne
    private Usuarios amigo;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne
    private Usuarios persona;

    public Amigos() {
    }

    public Amigos(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuarios getAmigo() {
        return amigo;
    }

    public void setAmigo(Usuarios amigo) {
        this.amigo = amigo;
    }

    public Usuarios getPersona() {
        return persona;
    }

    public void setPersona(Usuarios persona) {
        this.persona = persona;
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
        if (!(object instanceof Amigos)) {
            return false;
        }
        Amigos other = (Amigos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tk.dotsboxes.entities.Amigos[ id=" + id + " ]";
    }
    
}
