package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String type;

    @ElementCollection
    @Column (name="column")
    private List<String> shipLocations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Ship() { }

    /*Constructores de las variables, al mismo orden del salvo.*/
    /*ID no se pone constructor*/
    public Ship(String type, GamePlayer gamePlayer, List<String> shipLocations) {
        this.type = type;
        this.gamePlayer = gamePlayer;
        this.shipLocations = shipLocations;
    }

    public GamePlayer getGamePlayer() { return gamePlayer; }
    public Long getId() { return id; }
    public String getType() { return type; }
    public List<String> getShipLocations() { return shipLocations; }

    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }
    public void setId(Long id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setShipLocations(List<String> shipLocations) { this.shipLocations = shipLocations; }

    public Map<String, Object> makeShipDTO(){
        Map<String, Object>     dto = new LinkedHashMap<>();
        dto.put("type",this.getType());
        dto.put("locations",this.getShipLocations());


        return dto;
    }


}