package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
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
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Ship> ships;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Salvo> salvo;

    @ElementCollection
    @Column(name="hits_self")
    private List<String> self = new ArrayList<>();

    @ElementCollection
    @Column(name="hits_opponent")
    private List<String> opponent = new ArrayList<>();

    public GamePlayer() { }

    /*Constructores de cada uno de los atributos*/
    public GamePlayer(Game game, Player player, LocalDateTime joinDate) {
        this.game = game;
        this.player = player;
        this.joinDate = joinDate;
    }

    /*Get y Set de los atributos presentados en el programa*/
    public Game getGame() { return game; }
    public Player getPlayer() { return player; }
    public LocalDateTime getJoinDate() { return joinDate; }
    public Long getId() { return id; }
    public Set<Ship> getShips() { return ships; }
    public Set<Salvo> getSalvo() { return salvo; }

    public void setGame(Game game) { this.game = game; }
    public void setPlayer(Player player) { this.player = player; }
    public void setJoinDate(LocalDateTime joinDate) { this.joinDate = joinDate; }
    public void setId(Long id) {
        this.id = id;
    }
    public void setShips(Set<Ship> ships) { this.ships = ships; }
    public void setSalvo(Set<Salvo> salvo) { this.salvo = salvo; }

    public Optional<Score> getScore() {
        return this.getPlayer().getScore(this.getGame());
    }

    /*Mapa de string y objeto*/
    public Map<String, Object> makeGamePlayerDTO(){
        Map<String, Object>     dto = new LinkedHashMap<>();
        dto.put("id",   this.getId());
        dto.put("player",this.getPlayer().makePlayerDTO());

        return dto;
    }

    public Map<String, Object> makeHitsDTO(){
        Map <String, Object> dto = new LinkedHashMap<>();
        GamePlayer opponent = getOpponent();
        if(opponent == null || this.getShips().size() == 0){
            dto.put("self", new ArrayList<>());
            dto.put("opponent", new ArrayList<>());
        }
        else {
            dto.put("self", this.makeListHits());
            dto.put("opponent", opponent.makeListHits());
        }
        return dto;
    }

    public GamePlayer getOpponent(){
        GamePlayer opponent = this.getGame().getGamePlayers().stream().filter(gp -> !gp.getId().equals(this.getId())).findFirst().orElse(null);
        return opponent;
    }

    public List<Map<String, Object>>makeListHits(){
        List<Map<String, Object>> listHits= new ArrayList<>();
        Ship carrier = this.getShips().stream().filter(s -> s.getType().equals("carrier")).findFirst().get();
        List<String> carrierLocations = carrier.getShipLocations();
        Ship battleship = this.getShips().stream().filter(s -> s.getType().equals("battleship")).findFirst().get();
        List<String> battleshipLocations = battleship.getShipLocations();
        Ship submarine = this.getShips().stream().filter(s -> s.getType().equals("submarine")).findFirst().get();
        List<String> submarineLocations = submarine.getShipLocations();
        Ship destroyer = this.getShips().stream().filter(s -> s.getType().equals("destroyer")).findFirst().get();
        List<String> destroyerLocations = destroyer.getShipLocations();
        Ship patrolboat = this.getShips().stream().filter(s -> s.getType().equals("patrolboat")).findFirst().get();
        List<String> patrolboatLocations = patrolboat.getShipLocations();

        int carrierTotal = 0;
        int battleshipTotal = 0;
        int submarineTotal = 0;
        int destroyerTotal = 0;
        int patrolboatTotal = 0;

        for (Salvo salvoes : this.getOpponent().getSalvo()) {
            Map<String, Object> hitsTurn= new LinkedHashMap<>();

            int carrierTurn = 0;
            int battleshipTurn = 0;
            int submarineTurn = 0;
            int destroyerTurn = 0;
            int patrolboatTurn = 0;
            int missed = salvoes.getSalvoLocations().size();

            List<String> hitLocations= new ArrayList<>();
            for (String salvoLocation : salvoes.getSalvoLocations()) {
                if (carrierLocations.contains(salvoLocation)) {
                    hitLocations.add(salvoLocation);
                    carrierTotal++;
                    carrierTurn++;
                    missed--;
                }
                if (battleshipLocations.contains(salvoLocation)) {
                    hitLocations.add(salvoLocation);
                    battleshipTotal++;
                    battleshipTurn++;
                    missed--;
                }
                if (submarineLocations.contains(salvoLocation)) {
                    hitLocations.add(salvoLocation);
                    submarineTotal++;
                    submarineTurn++;
                    missed--;
                }
                if (destroyerLocations.contains(salvoLocation)) {
                    hitLocations.add(salvoLocation);
                    destroyerTotal++;
                    destroyerTurn++;
                    missed--;
                }
                if (patrolboatLocations.contains(salvoLocation)) {
                    hitLocations.add(salvoLocation);
                    patrolboatTotal++;
                    patrolboatTurn++;
                    missed--;
                }
            }

            Map<String, Object> listDamages= new LinkedHashMap<>();
            listDamages.put("carrierHits", carrierTurn);
            listDamages.put("battleshipHits", battleshipTurn);
            listDamages.put("submarineHits", submarineTurn);
            listDamages.put("destroyerHits", destroyerTurn);
            listDamages.put("patrolboatHits", patrolboatTurn);
            listDamages.put("carrier", carrierTotal);
            listDamages.put("battleship", battleshipTotal);
            listDamages.put("submarine", submarineTotal);
            listDamages.put("destroyer", destroyerTotal);
            listDamages.put("patrolboat", patrolboatTotal);

            hitsTurn.put("turn", salvoes.getTurn());
            hitsTurn.put("hitLocations", hitLocations);
            hitsTurn.put("damages", listDamages);
            hitsTurn.put("missed", missed);

            listHits.add(hitsTurn);
        }
        return listHits;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }


    public Map<String, Object> makeGameViewDTO(){
        Map<String, Object>     dto = new LinkedHashMap<>();
        dto.put("id",   this.getGame().getId());
        dto.put("created", this.getGame().getCreationDate());
        dto.put("gameState", gamestate());
        dto.put("gamePlayers",this.getGame().getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList()));

        dto.put("ships",this.getShips()
                .stream()
                .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList()));

        dto.put("salvoes",this.getGame().getGamePlayers()
                .stream()
                .flatMap(gamePlayer -> gamePlayer.getSalvo()
                        .stream()
                        .map(salvo -> salvo.makeSalvoDTO())
                )
                .collect(Collectors.toList()));

        dto.put("hits", this.makeHitsDTO());

        return dto;
    }


public String gamestate () {

        String gamestate = "UNDEFINED";

        GamePlayer currentPlayer=this;
        GamePlayer opponent = this.getOpponent();

        // Obtener player 1
        GamePlayer player1 = this.getGame().getGamePlayers().stream().min(Comparator.comparing(gamePlayer -> gamePlayer.getId())).get();
        //System.out.println("player1:" + player1.getId());

        // Obtener player 2
        GamePlayer player2 = this.getGame().getGamePlayers().stream().max(Comparator.comparing(gamePlayer -> gamePlayer.getId())).get();
        //System.out.println("player2:" + player2.getId());

        int player1Turns = player1.getSalvo().size();
        int player2Turns = player2.getSalvo().size();



        // PLACESHIPS STATE
        if (this.getShips().size() != 5) {
            gamestate = "PLACESHIPS";
            return gamestate;
        }

        // WAITING FOR OPPONENT STATE
        if (this.getOpponent() == null) {
            gamestate = "WAITINGFOROPP";
            return gamestate;
        }

        // WAIT STATE
        if (this.getOpponent().getShips().size() != 5) {
            gamestate =  "WAIT";
            return gamestate;
        }


        // TIE, WIN Y LOST STATE

        if (this.getId()==player1.getId()){
            if (this.barcosHundidos(opponent, currentPlayer)){
                if (player1Turns>player2Turns){
                    gamestate =  "WAIT";
                    return gamestate;
                }
                else if (this.barcosHundidos(currentPlayer,opponent)){
                    gamestate =  "TIE";
                    return gamestate;

                }
                else {
                    gamestate =  "WON";
                    return gamestate;
                }
            }
            if (this.barcosHundidos(currentPlayer,opponent)){
                gamestate =  "LOST";
                return gamestate;
            }
        }

        else {
            if(this.barcosHundidos(opponent,currentPlayer)){
                if (this.barcosHundidos(currentPlayer,opponent)){
                    gamestate = "TIE";
                    return gamestate;
                }
                else {
                    gamestate = "WIN";
                    return gamestate;
                }
            }
            if(this.barcosHundidos(currentPlayer,opponent)){
                if (player1Turns==player2Turns){
                    gamestate = "LOST";
                    return gamestate;
                }
            }
        }

        //WAIT STATE

        if (player1Turns > player2Turns && this.getId() == player1.getId()) {
            gamestate =  "WAIT";
            return gamestate;
        }

        if (player1Turns == player2Turns && this.getId() == player2.getId()) {
            gamestate =  "WAIT";
            return gamestate;
        }

        // PLAY STATE
        if (this.getOpponent().getShips().size() == 5) {
            gamestate =  "PLAY";
            return gamestate;
        }

        return gamestate;
    }

    private boolean barcosHundidos(GamePlayer gpBarcos, GamePlayer gpSalvos) {

        GamePlayer opponent = this.getOpponent();

        if (!gpBarcos.getShips().isEmpty() && !gpSalvos.getSalvo().isEmpty()) {
            return  gpSalvos.getSalvo()
                    .stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList())
                    .containsAll(gpBarcos.getShips()
                            .stream().flatMap(ship -> ship.getShipLocations().stream())
                            .collect(Collectors.toList()));
        }
        return false;
    }
}
