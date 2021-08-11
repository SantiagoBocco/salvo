package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy="gamePlayerID", fetch=FetchType.EAGER)
    Set<Ship> ship;

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
    public Long getId() { return id; }
    public Set<Ship> getShip() { return ship; }

    public void setGameID(Game gameID) { this.gameID = gameID; }
    public void setPlayerID(Player playerID) { this.playerID = playerID; }
    public void setJoinDate(LocalDateTime joinDate) { this.joinDate = joinDate; }
    public void setId(Long id) {
        this.id = id;
    }
    public void setShip(Set<Ship> ship) { this.ship = ship; }

    /*Mapa de string y objeto*/
    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object>     dto = new LinkedHashMap<>();
        dto.put("id",   this.getId());
        dto.put("player",this.getPlayerID().makePlayerDTO() );
        dto.put("ship",this.getShip()
                .stream()
                .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList()));

        return dto;
    }

}