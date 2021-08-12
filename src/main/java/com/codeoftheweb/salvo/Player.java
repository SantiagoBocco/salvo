package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    /*Definicion de variables --> Se ponen cerca de la variable "private string userName" para que se le asigne*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String userName;

    /*OneToMany union de Player con GamePlayer*/
    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    /*Constructores por default --> Metodo de la clase que se llama automaticamente cada vez que se crea un objeto*/
    public Player() { }

    public Player(String userName) {
        this.userName = userName;
    }

    public Set<GamePlayer> getGamePlayers() { return gamePlayers; }
    public String getUserName() { return userName; }
    public Long getId() { return id; }

    public void setUserName(String userName) { this.userName = userName; }
    public void setId(Long id) { this.id = id; }
    public void setGamePlayers(Set<GamePlayer> gamePlayers) { this.gamePlayers = gamePlayers; }

    public Map<String, Object> makePlayerDTO(){
        Map<String, Object>     dto = new LinkedHashMap<>();
        dto.put("id",   this.getId());
        dto.put("email",    this.userName);

        return dto;
    }

    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());
    }

}
