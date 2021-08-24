package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Float score;
    private LocalDateTime finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    public Score() { }

    public Score(Float score, LocalDateTime finishDate, Player player, Game game) {
        this.score = score;
        this.finishDate = finishDate;
        this.player = player;
        this.game = game;
    }

    public void setScore(Float score) { this.score = score; }
    public void setFinishDate(LocalDateTime finishDate) { this.finishDate = finishDate; }
    public void setPlayer(Player player) { this.player = player; }
    public void setGame(Game game) { this.game = game; }

    public Float getScore() { return score; }
    public LocalDateTime getFinishDate() { return finishDate; }
    public Player getPlayer() { return player; }
    public Game getGame() { return game; }

    public Map<String, Object> makeScoreDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("score" , this.getScore());
        dto.put("finishDate" , this.getFinishDate());
        dto.put("player" , this.getPlayer().getId());

        return dto;
    }
}
