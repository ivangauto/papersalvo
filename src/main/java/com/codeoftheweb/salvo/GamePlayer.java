package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    // (del libro) We use Set<Pet> rather than List because JPA may retrieve duplicate results from the database as more relationships with people and pets are created. This is an artifact of how JPA retrieves data using joined tables. The Set collection automatically ignores duplicate entries when added.
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salvo> salvos = new HashSet<>();

    public GamePlayer() { }


    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public Map<String, Object> makeGamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        List<Object> list = new LinkedList<>();
        dto.put("GamePlayerId", this.getId());
        dto.put("PlayerInfo", this.getPlayer().getuserName());
        Game game1 = this.getGame();
        Double score1;
        if (game1.getScores().size() > 1) {
            for(Score score2 : game1.getScores()){
                if (score2.getPlayer().getuserName() == this.getPlayer().getuserName()){
                    score1 = score2.getScore();
                    dto.put("Score", score1);
                }
            }
        }


        return dto;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    // metodo para agregar los barcos al gameplayer
    // del libro: The addShip() method lets us connect a gamePlayer to a ship.
    // If we call the line of code gamePlayer.addShip(ship); java sets this variable to the instance of GamePlayer in gamePlayer
    public void addShip(Ship ship) {
        ship.setGamePlayer(this); // conecta con el gamePlayer que pertenece el ship que se pasa por parametro
        this.ships.add(ship);    // agrega el ship al Set<ship> creado en el one to many arriba
    }

    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this); // conecta con el gamePlayer que pertenece el salvo que se pasa por parametro
        this.salvos.add(salvo);    // agrega el salvo al Set<salvo> creado en el one to many arriba
    }

    public Set<Ship> getShips() {
        return ships;
    }



    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvos() {
        return salvos;
    }

    public void setSalvos(Set<Salvo> salvos) {
        this.salvos = salvos;
    }






    //  public Map<String, Object> makeGamePlayerDTO() {
    //      Map<String, Object> dto = new LinkedHashMap<>();
    //      dto.put("gameId", getGame());
    //      dto.put("playerId", getPlayer());
    //      return dto;
    //  }


}