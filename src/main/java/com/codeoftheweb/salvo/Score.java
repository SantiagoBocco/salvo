package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
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
    private Double score;
    private LocalDateTime finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    public Score() { }

    public Score(Double score, LocalDateTime finishDate, Player player, Game game) {
        this.score = score;
        this.finishDate = finishDate;
        this.player = player;
        this.game = game;
    }

    public void setScore(Double score) { this.score = score; }
    public void setId(Long id) { this.id = id; }
    public void setFinishDate(LocalDateTime finishDate) { this.finishDate = finishDate; }
    public void setPlayer(Player player) { this.player = player; }
    public void setGame(Game game) { this.game = game; }

    public Double getScore() { return score; }
    public Long getId() { return id; }
    public LocalDateTime getFinishDate() { return finishDate; }
    public Player getPlayer() { return player; }
    public Game getGame() { return game; }
}
