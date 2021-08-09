package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private LocalDateTime joinDate;

    /*ManytoOne del programa donde recibe Game y Player*/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player playerID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game gameID;

    public GamePlayer() { }

    /*Constructores de cada uno de los atributos*/
    public GamePlayer(Game gameID, Player playerID, LocalDateTime joinDate) {
        this.gameID = gameID;
        this.playerID = playerID;
        this.joinDate = joinDate;
    }

    /*Get y Set de los atributos presentados en el programa*/
    public Game getGameID() { return gameID; }
    public Player getPlayerID() { return playerID; }
    public LocalDateTime getJoinDate() { return joinDate; }

    public void setGameID(Game gameID) { this.gameID = gameID; }
    public void setPlayerID(Player playerID) { this.playerID = playerID; }
    public void setJoinDate(LocalDateTime joinDate) { this.joinDate = joinDate; }


    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object>     dto = new LinkedHashMap<>();
        dto.put("id",   this.getId());
        dto.put("player",this.getPlayerID().makePlayerDTO() );

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}