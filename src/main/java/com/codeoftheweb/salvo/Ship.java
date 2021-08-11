package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String type;

    @ElementCollection
    @Column (name="")
    private List<String> locations  = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayerID;

    public Ship() { }

    /*Constructores de las variables, al mismo orden del salvo.*/
    /*ID y set no se ponene constructor*/
    public Ship(String type, GamePlayer gamePlayerID, List<String> locations) {
        this.type = type;
        this.gamePlayerID = gamePlayerID;
        this.locations = locations;
    }

    public GamePlayer getGamePlayerID() { return gamePlayerID; }
    public Long getId() { return id; }
    public String getType() { return type; }
    public List<String> getLocations() { return locations; }

    public void setGamePlayerID(GamePlayer gamePlayerID) { this.gamePlayerID = gamePlayerID; }
    public void setId(Long id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setLocations(List<String> locations) { this.locations = locations; }

    public Map<String, Object> makeShipDTO(){
        Map<String, Object>     dto = new LinkedHashMap<>();
        dto.put("type",this.getType());
        dto.put("locations",this.getLocations());


        return dto;
    }


}