package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


@Entity
public class Player {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;



    private String userName;
    private String password;


    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores;

    public Player() { }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Player(String email, String pass) {
        userName = email;
        password = pass;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this); // setea este player al gameplayer pasado por parametro
    }

    public String getuserName() {
        return userName;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Map<String, Object> makePlayerDTO() {
         Map<String, Object> dto = new LinkedHashMap<String, Object>();
         dto.put("PlayerId", getId());
         dto.put("UserName", getuserName());
         return dto;
     }



    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Map<String, Object> makeLeaderDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("PlayerId", getId());
        dto.put("UserName", getuserName());
        dto.put("Scores", this.getScores().stream().map(score -> score.getScore())) ;
        return dto;
    }


}