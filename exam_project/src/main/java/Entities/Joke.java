/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Martin
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Joke.deleteAllRows", query = "DELETE from Joke"),
    @NamedQuery(name = "Joke.getAll", query = "SELECT m FROM Joke m"),
    @NamedQuery(name = "Joke.getByName", query = "SELECT m FROM Joke m WHERE m.Creator LIKE :Creator")
})
public class Joke implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String Joke, Creator;
    private double Score;
    private int Votes;

    public Joke(String Joke, String Creator, double Score, int Votes) {
        this.Joke = Joke;
        this.Creator = Creator;
        this.Score = Score;
        this.Votes = Votes;
    }

    public Joke() {
    }

    public String getJoke() {
        return Joke;
    }

    public void setJoke(String Joke) {
        this.Joke = Joke;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String Creator) {
        this.Creator = Creator;
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double Score) {
        this.Score = Score;
    }

    public int getVotes() {
        return Votes;
    }

    public void setVotes(int Votes) {
        this.Votes = Votes;
    }
    
    
            
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Joke)) {
            return false;
        }
        Joke other = (Joke) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Joke[ id=" + id + " ]";
    }
    
}
