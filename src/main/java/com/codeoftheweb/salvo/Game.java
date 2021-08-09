package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime creationDate;

    /*OneToMany union de Game con GamePlayer*/
    @OneToMany(mappedBy="gameID", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    /*Set y Get de las variables del OneToMany*/
    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }
    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    /*Usamos string en el caso de Player y LocalDateTime para este, estas variables ayudan a la clase*/
    public Game() { }
    public Game(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    /*Setter y Getter para getId para el uso del SalvoController*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*Creacion de un mapa con string y objeto*/
    public Map<String, Object> makeGameDTO(){
        Map<String, Object>     dto = new LinkedHashMap<>();
        dto.put("id",   this.getId());
        dto.put("created",    this.getCreationDate());
        dto.put("gamePlayers",  this.getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList()));

    return dto;
    }

    /*Uso del JsonIgnore*/
    @JsonIgnore
    public List<Player> getPlayers() {
        return gamePlayers.stream().map(sub -> sub.getPlayerID()).collect(toList());
    }

}

