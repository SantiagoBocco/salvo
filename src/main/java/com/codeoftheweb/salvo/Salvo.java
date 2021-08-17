package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private Long Turn;

    @ElementCollection
    @Column(name="column")
    private List<String> salvoLocation  = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Salvo() { }

    public Salvo(Long turn, List<String> salvoLocation, GamePlayer gamePlayer) {
        Turn = turn;
        this.salvoLocation = salvoLocation;
        this.gamePlayer = gamePlayer;
    }

    public Long getId() { return id; }
    public Long getTurn() { return Turn; }
    public List<String> getSalvoLocation() { return salvoLocation; }
    public GamePlayer getGamePlayer() { return gamePlayer; }

    public void setSalvoLocation(List<String> salvoLocation) { this.salvoLocation = salvoLocation; }
    public void setId(Long id) { this.id = id; }
    public void setTurn(Long turn) { Turn = turn; }
    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }

    public Map<String, Object> makeSalvoDTO(){
        Map<String, Object>     dto = new LinkedHashMap<>();
        dto.put("turn",this.getTurn());
        dto.put("salvoLocation",this.getSalvoLocation());

        return dto;
    }
}
