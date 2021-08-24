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
    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scores;

    public Game() { }

    public Game(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public Set<GamePlayer> getGamePlayers() { return gamePlayers; }
    public Long getId() { return id; }
    public Set<Score> getScores() { return scores; }

    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public void setGamePlayers(Set<GamePlayer> gamePlayers) { this.gamePlayers = gamePlayers; }
    public void setId(Long id) { this.id = id; }
    public void setScores(Set<Score> scores) { this.scores = scores; }

    /*Creacion de un mapa con string y objeto*/
    public Map<String, Object> makeGameDTO(){
        Map<String, Object>     dto = new LinkedHashMap<>();
        dto.put("id",   this.getId());
        dto.put("created",    this.getCreationDate());
        dto.put("gamePlayers",  this.getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList()));
        dto.put("scores", this.getGamePlayers()
                .stream()
                .map(gamePlayer -> {if (gamePlayer.getScore().isPresent())
                {
                return gamePlayer.getScore().get().makeScoreDTO();
                }
                else {
                return null;
                }}));
    return dto;
    }

    /*Uso del JsonIgnore*/
    @JsonIgnore
    public List<Player> getPlayers() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }

}

