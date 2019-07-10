package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    GamePlayerRepository gamePlayerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private SalvoRepository salvoRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    //un array solo con los ids en /api/gamesid
    @RequestMapping("/gamesid")
    public List<Object> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> game.getId())
                .collect(Collectors.toList());
    }



    @RequestMapping("/games")

    public Map<String, Object> getGames(Authentication authentication) {

        Player user = CurrentPlayer(authentication);

        Map<String, Object> list = new LinkedHashMap<>();
        if (user != null) {
            list.put("CurrentPlayer", user.getuserName());
        }else{
            list.put("CurrentPlayer", "None");
        }
        list.put("Games", gameRepository
                .findAll()
                .stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList()));
        list.put("LeaderBoard", playerRepository
                .findAll()
                .stream()
                .map(player -> player.makeLeaderDTO())
                .collect(Collectors.toList()));

        return list;
    }



    @RequestMapping("game_view/{gamePlayerId}")
    public List<Object> gameView(@PathVariable long gamePlayerId, Authentication authentication) {
        Player user = CurrentPlayer(authentication);
        GamePlayer gamePlayer1 = gamePlayerRepository.findById(gamePlayerId);

        if (user.getuserName() == gamePlayer1.getPlayer().getuserName()) {
            return Collections.singletonList(gamePlayer1)
                    .stream()
                    .map(gamePlayer -> makeGamePlayerViewDTO(gamePlayer1))
                    .collect(Collectors.toList());
        } else {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Player Not Authorized to View information");

        }
    }



    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(

            @RequestParam String username, @RequestParam String password) {
        ResponseEntity<Object> response;
        if (username.isEmpty() || password.isEmpty()) {
            //    System.out.println("vacio");
            response = new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }else {

            if (playerRepository.findByUserName(username) != null) {
                response = new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
            } else {

                playerRepository.save(new Player(username, passwordEncoder.encode(password)));
                response = new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
        return response;
    }


    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> newgame(Authentication authentication) {
        Player user = CurrentPlayer(authentication);
        ResponseEntity<Object> response;
        if (user == null) {
            //    System.out.println("vacio");
            response = new ResponseEntity<>("No User Logged In", HttpStatus.FORBIDDEN);
        }else {
            Game newGame = new Game(LocalDateTime.now());
            gameRepository.save(newGame);
            GamePlayer newGamePlayer = new GamePlayer(newGame, user);
            gamePlayerRepository.save(newGamePlayer);
            response = new ResponseEntity<>(makeMap("GamePlayerId", newGamePlayer.getId()), HttpStatus.CREATED);
        }
        return response;
    }



    @RequestMapping(path = "game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> joingame(@PathVariable long gameId, Authentication authentication) {
        Player user = CurrentPlayer(authentication);
        ResponseEntity<Object> response = null;
        if (user == null) {
            //    System.out.println("vacio");
            response = new ResponseEntity<>("No User Logged In", HttpStatus.UNAUTHORIZED);
         //   response = new ResponseEntity<>(makeMap("Response", "No user logged In", HttpStatus.UNAUTHORIZED);
        } else {
            Game gameSearched = gameRepository.findById(gameId);
            //   System.out.println(gameSearched.getGamePlayers().size());
            if (gameSearched == null) {
                response = new ResponseEntity<>("No such game", HttpStatus.FORBIDDEN);
            } else {
                if (gameSearched.getGamePlayers().size() == 0) {
                    GamePlayer newGamePlayer = new GamePlayer(gameSearched, user);
                    gamePlayerRepository.save(newGamePlayer);
                    response = new ResponseEntity<>(makeMap("GamePlayerId", newGamePlayer.getId()), HttpStatus.CREATED);
                }
                if(gameSearched.getGamePlayers().size() == 1){
                    for (GamePlayer gamePlayer2 : gameSearched.getGamePlayers()) {
                        if(user.equals(gamePlayer2.getPlayer())){
                        //    response = new ResponseEntity<>("You Cannot play against yourself", HttpStatus.FORBIDDEN);
                            response = new ResponseEntity<>(makeMap("Response", "You cannot play against yourself"), HttpStatus.FORBIDDEN);
                        }else{
                            GamePlayer newGamePlayer = new GamePlayer(gameSearched, user);
                            gamePlayerRepository.save(newGamePlayer);
                            response = new ResponseEntity<>(makeMap("GamePlayerId", newGamePlayer.getId()), HttpStatus.CREATED);
                        }
                    }
                }
                if(gameSearched.getGamePlayers().size() > 1) {
                    response = new ResponseEntity<>(makeMap("Response", "Game is full"), HttpStatus.FORBIDDEN);
                    }
                }
            }
     return response;
}


// -------------------------------------------------------------------------------------------- Ships

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShip(Authentication authentication,
                      @PathVariable long gamePlayerId, @RequestBody List<Ship> ships) {
        ResponseEntity<Map<String, Object>> response;
        Player user = CurrentPlayer(authentication);
        GamePlayer gamePlayer1 = gamePlayerRepository.findById(gamePlayerId);
        if (user == null || gamePlayer1 == null) {
            response = new ResponseEntity<>(makeMap("Response", "No player Logged In or no gamePlayer created"), HttpStatus.UNAUTHORIZED);
        } else {
            if (user.equals(gamePlayer1.getPlayer())) {
                if (gamePlayer1.getShips().size() == 0 && ships.size()==5) {
                    //  agregara  barcos si no hay agregados
                    for (Ship ship : ships) {
                        ship.setGamePlayer(gamePlayer1);
                        shipRepository.save(ship);
                    }
                    response = new ResponseEntity<>(makeMap("Response", "Ships added for " + user.getuserName()), HttpStatus.CREATED);
                }else{
                    response = new ResponseEntity<>(makeMap("Response", "Post 5 ships"), HttpStatus.FORBIDDEN);
                }
            }else {
                response = new ResponseEntity<>(makeMap("Response", "User Cannot Add ships in this gamePlayer view"), HttpStatus.UNAUTHORIZED);
            }
        }
        return response;
    }



// get method
    @RequestMapping("/games/players/{gamePlayerId}/ships")
    public Map<String, Object> gamePlayerShips(@PathVariable long gamePlayerId, Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        Player user = CurrentPlayer(authentication);
        GamePlayer gamePlayer1 = gamePlayerRepository.findById(gamePlayerId);
        if (user.getuserName() == gamePlayer1.getPlayer().getuserName()) {
            dto.put("Ship", makeShipDTO(gamePlayer1.getShips()));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Player Not Authorized to View information");
        }
        return dto;
    }



    // ----------------------------------------------------------- Salvoes

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSalvo(Authentication authentication,
                             @PathVariable long gamePlayerId, @RequestBody List<String> salvo) {

        ResponseEntity<Map<String, Object>> response;
        Player user = CurrentPlayer(authentication);

        GamePlayer gamePlayer1 = gamePlayerRepository.findById(gamePlayerId);
        Game game1 = gamePlayer1.getGame();
        GamePlayer gamePlayerRival = new GamePlayer();
        if (game1.getGamePlayers().size() > 1) {
            for (GamePlayer gamePlayer2 : game1.getGamePlayers()) { // separo los 2 gamePlayers del Game
                if (gamePlayer2.equals(gamePlayer1)) {
                    // no hago nada
                } else {
                    gamePlayerRival = gamePlayer2;
                }
            }
        }
        if(game1.getScores().size() == 0) { // Prosigue si no es game over
            Boolean goAhead;
            Boolean player2;
            if (gamePlayer1.getId() < gamePlayerRival.getId()) { // se define si es el turno del jugador
                if (gamePlayer1.getSalvos().size() == gamePlayerRival.getSalvos().size()) { //Es el jugador 1 y ambos  tienen mismos turnos. Ok
                    goAhead = true;
                    player2 = false;
                } else { // Es el jugador 1 y no tienen los mismos turnos. Not Ok
                    goAhead = false;
                    player2 = false;
                }
            } else {
                if (gamePlayer1.getSalvos().size() == gamePlayerRival.getSalvos().size() - 1) { //Es el jugador 2 y su turno es uno menos que el otro jugador. Ok
                    goAhead = true;
                    player2 = true;
                } else { // Es el jugador 2 y su turno no es uno menos que el otro jugador. Not Ok
                    goAhead = false;
                    player2 = true;
                }
            }
            if (user == null || gamePlayer1 == null) { // esto es por si hay un intento de submitr salvoes sin user logged o gameplayer no exist
                response = new ResponseEntity<>(makeMap("Response", "No player Logged In or no gamePlayer created or not your turn"), HttpStatus.UNAUTHORIZED);
            } else {
                if (gamePlayer1.getShips().size() == 5) { // debe haber barcos cargados del jugador
                    if (gamePlayerRival.getShips().size() == 5) { // el rival debe tener barcos cargados
                        if (user.equals(gamePlayer1.getPlayer())) { // el user logueado debe ser el del gameplayer
                            if (goAhead) { // prosigue si es el turno del jugador
                                Map<String, Object> hundioRival = new LinkedHashMap<>();
                                List<Map> info1 =  makeListHits(gamePlayer1, gamePlayerRival);
                                hundioRival = makeHitsDTO(info1, false); // obtengo el map con los hudidos y turno
                                Integer i=0;
                                if(player2){ // en caso de ser el player2, tendra tantos salvos disponibles como barcos tenia
                              //      al comenzar el turno, por eso debo eliminar si hubo barcos eliminados en la ultima tirada del jugador 1
                                    for (Object item : hundioRival.values()){  // obtengo los turnos en que se hundieron los barcos del jugaor 2
                                        if(parseInt(item.toString())-1 == gamePlayer1.getSalvos().size()){ // si el turno es en la ultima tirada del jugador1 debo eliminarla para calcular los salvos que le quedan al jugador2
                                            i++;   //  cuantas veces aparece un hundido en la ultima tirada del jugador1
                                        }
                                    }
                                }
                                if(salvo.size() == (5-hundioRival.size()+i)) { // aqui descuento los barcoss hundidos. (en caso de jugador2 i puede valer 0, 1, etc. los salvos posted deben coincidir con esta cantidad, sino mensaje de error
                                        Salvo salvo1 = new Salvo(gamePlayer1, salvo, (gamePlayer1.getSalvos().size() + 1));
                                        salvoRepository.save(salvo1);
                                        salvo1.setGamePlayer(gamePlayer1);
                                        response = new ResponseEntity<>(makeMap("Response", "Salvoes added for " + user.getuserName()), HttpStatus.CREATED);
                                    } else {
                                        response = new ResponseEntity<>(makeMap("Response", "No, you had "+(5-hundioRival.size()+i)+" ships afloat on last turn. Submit "+(5-hundioRival.size()+i)+" salvoes"), HttpStatus.FORBIDDEN);
                                    }
                                    } else {
                                response = new ResponseEntity<>(makeMap("Response", "Not your turn"), HttpStatus.FORBIDDEN);
                            }
                        } else {
                            response = new ResponseEntity<>(makeMap("Response", "User Cannot Add salvoes in this gamePlayer view"), HttpStatus.UNAUTHORIZED);
                        }
                    } else {
                        response = new ResponseEntity<>(makeMap("Response", "Rival s ship should be placed first"), HttpStatus.FORBIDDEN);
                    }
                } else {
                    response = new ResponseEntity<>(makeMap("Response", "Ships should be placed first"), HttpStatus.FORBIDDEN);
                }
            }
        }else{
            response = new ResponseEntity<>(makeMap("Response", "Game Over"), HttpStatus.FORBIDDEN);
        }
        return response;
    }



    // get method
    @RequestMapping("/games/players/{gamePlayerId}/salvoes")
    public Map<String, Object> gamePlayerSalvoes(@PathVariable long gamePlayerId, Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        Player user = CurrentPlayer(authentication);
        GamePlayer gamePlayer1 = gamePlayerRepository.findById(gamePlayerId);
        if (user.getuserName() == gamePlayer1.getPlayer().getuserName()) {
            dto.put("Salvo", makeSalvoDTO(gamePlayer1.getSalvos()));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Player Not Authorized to View information");
        }
        return dto;
    }




    // -----------------------------------------------metodos
// -----------------------------------------------------------------
//-------------------------------------------------------------------makemap para responses
    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

// -------------------------------------------------------------------------------------------GamePlayerView

   private Map<String, Object> makeGamePlayerViewDTO(GamePlayer gamePlayer) {
       Map<String, Object> dto = new LinkedHashMap<String, Object>();
       dto.put("Game", gamePlayer.getGame().makeGameDTO()); // siempre visible en json
       dto.put("Ship", makeShipDTO(gamePlayer.getShips())); // siempre visible en json
       dto.put("SalvosPlayer", makeSalvoDTO(gamePlayer.getSalvos())); // siempre visible en json
       Game game1 = gamePlayer.getGame();
       GamePlayer gamePlayer1 = null;
       String result = "Playing";
       if (game1.getGamePlayers().size() > 1) { // si hay 2 jugadores en game prosigo
           for (GamePlayer gamePlayer2 : game1.getGamePlayers()) { // obtengo los 2 gamePlayers involucrados
               if (gamePlayer2.equals(gamePlayer)) {
                   // nada
               } else {
                   gamePlayer1 = gamePlayer2;
               }
           }
           if (gamePlayer.getShips().size() > 0 && gamePlayer1.getShips().size() > 0) { // debe haber ships cargados
               dto.put("SalvosRival", makeSalvoDTO(gamePlayer1.getSalvos()));
               Map<String, Object> hundioPlayer = new LinkedHashMap<>();
               List<Map> info = makeListHits(gamePlayer1, gamePlayer);
               hundioPlayer = makeHitsDTO(info, false); // map con los que hundio el player
               dto.put("PlayerHits", makeHitsDTO(info, true));
               dto.put("RivalShipsSunk", hundioPlayer);
               Map<String, Object> hundioRival = new LinkedHashMap<>();
               List<Map> info1 = makeListHits(gamePlayer, gamePlayer1);
               hundioRival = makeHitsDTO(info1, false); // map con los que hundio el rival
               dto.put("RivalHits", makeHitsDTO(info1, true));
               dto.put("PlayerShipsSunk", hundioRival);
               if (game1.getScores().size() == 0) { // si no hay scores cargados, chequeo que no sea el final de juego antes de enviar json
                   Score score1;
                   Score score2;
                   if (hundioPlayer.size() == 5 && gamePlayer1.getSalvos().size() == gamePlayer.getSalvos().size()) {
                        // si el player hundio 5 barcos al rival, y estan en el mismo turno, cargo scores. Esto se ejecutara solo una vez
                       if (hundioRival.size() < 5) { // si rival no llego a hundir los 5  (el player gano)
                           score1 = new Score(game1, gamePlayer.getPlayer(), LocalDateTime.now(), 1);
                           score2 = new Score(game1, gamePlayer1.getPlayer(), LocalDateTime.now(), 0);
                           result = "Game Over. You Won!";
                       } else { // si el rival tambien hunido los 5 es empate
                           score1 = new Score(game1, gamePlayer.getPlayer(), LocalDateTime.now(), 0.5);
                           score2 = new Score(game1, gamePlayer1.getPlayer(), LocalDateTime.now(), 0.5);
                           result = "Game Over. It s a Tie";
                       }
                       scoreRepository.save(score1);
                       scoreRepository.save(score2);
                       dto.put("Game", gamePlayer.getGame().makeGameDTO()); // sobreescribe Game en el DTO
                   }
                   if (hundioPlayer.size() < 5 && hundioRival.size() == 5 && gamePlayer1.getSalvos().size() == gamePlayer.getSalvos().size()) {
                       // si se da el caso de que el rival hundio 5 y el jugador no (gano el rival)
                       score1 = new Score(game1, gamePlayer.getPlayer(), LocalDateTime.now(), 0);
                       score2 = new Score(game1, gamePlayer1.getPlayer(), LocalDateTime.now(), 1);
                       scoreRepository.save(score1);
                       scoreRepository.save(score2);
                       result = "Game Over. You Lost";
                       dto.put("Game", gamePlayer.getGame().makeGameDTO()); // sobreescribiendo Game en el dto
                   }
               } else { // en caso de que al ejecutarse este dto, el game este termiando (osea que hay scores ya cargados)
                   for (Score score : game1.getScores()) {
                       if (score.getScore() == 1) {
                            if(gamePlayer.getPlayer().getuserName() == score.getPlayer().getuserName()){
                                result = "Game Over. You Won!";
                            }else{
                                result = "Game Over. You Lost";
                            }
                       }
                       if (score.getScore() == 0.5) {
                           result = "Game Over. It s a Tie";
                       }
                   }
               }
           } else { // faltan ships
               if(gamePlayer.getShips().size() == 0 && gamePlayer1.getShips().size() == 0){
                   result = "Add Ships";

               }else if(gamePlayer.getShips().size() == 0) {
                   result = "Add Ships";
               }else{
                   result = "Waiting for Opponet s Ships";
               }
           }
       }else { // falta 1 jugador al game
           result = "Waiting for an Opponent";
       }

        if(result == "Playing"){ // si no se dieron las condiciones anteriores, el juego esta activo. Aqui determino los turnos
            if(gamePlayer.getId()<gamePlayer1.getId()){
                if(gamePlayer.getSalvos().size() == gamePlayer1.getSalvos().size()) {
                    result = "Your Turn";
                }else{
                    result = gamePlayer1.getPlayer().getuserName() + " s Turn";
                }
            }
            if(gamePlayer.getId()>gamePlayer1.getId()){
                if(gamePlayer.getSalvos().size() == gamePlayer1.getSalvos().size()) {
                    result = gamePlayer1.getPlayer().getuserName() + " s Turn";
                }else{
                    result = "Your Turn";
                }
            }
        }
         dto.put("GameState", result);
        return dto;
    }



// -------------------------------------------------- ShipDto
// Devuelve el set de ships (con shiptype y locations)
    private List<Object> makeShipDTO(Set<Ship> ships) {
        List<Object> shipsList = new LinkedList<>();
        for(Ship ship : ships){ //esto es for extendido.  for ( TipoARecorrer nombreVariableTemporal : nombreDeLaColección ) {Instrucciones}
            Map<String, Object> oneShipDTO = new LinkedHashMap<>();
            oneShipDTO.put("ShipType", ship.getShipType());
            oneShipDTO.put("ShipLocations", ship.getShipLocations());
            shipsList.add(oneShipDTO);
        }
        return shipsList;
    }


//----------------------------------------------------------SalvoDto
    // map con los salvos por turno.
    private List<Object> makeSalvoDTO(Set<Salvo> salvos) {
        List<Object> salvosList = new LinkedList<>();
        for(Salvo salvo : salvos){ //esto es for extendeido.  for ( TipoARecorrer nombreVariableTemporal : nombreDeLaColección ) {Instrucciones}
            Map<String, Object> oneSalvoDTO = new LinkedHashMap<>();
            oneSalvoDTO.put("Turn", salvo.getTurn());
            oneSalvoDTO.put("Locations", salvo.getSalvoLocations());
            salvosList.add(oneSalvoDTO);
        }
        return salvosList;
    }

//------------------------------------------------------------------------ Autenticacion
    // metodo pubico que detecta el estado de logueo. O null o un user en particular
    public Player CurrentPlayer(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return playerRepository.findByUserName(authentication.getName());
    }


// ------------------------------------------------------------------------- Lista con tocados
// extraigo los datos de barco, donde fue tocado y en que turno. Se devuelve una lista de maps
    private List<Map> makeListHits(GamePlayer gamePlayer, GamePlayer gamePlayerRival) {
        List<Map> aciertos = new LinkedList<>();
        Set<Ship> shipPlayer = gamePlayer.getShips();
        Set<Salvo> salvoRival = gamePlayerRival.getSalvos();
        Integer turno = 0;
        for (Ship ship : shipPlayer) {
            String shipType = ship.getShipType();
            for (Salvo salvo : salvoRival) {
                for (String coordShip : ship.getShipLocations()) {
                    turno = salvo.getTurn();
                    for (String coordSalvo : salvo.getSalvoLocations()) {
                        if (coordSalvo.equals(coordShip)) {
                            // extraigo la info que necesito en un array de maps {Turn: ShipType, Location)
                            Map<String, String> acierto = new LinkedHashMap<String, String>(); //declarandolo aca resetea el map
                            acierto.put("Turn", turno.toString());
                            acierto.put("ShipType", shipType);
                            acierto.put("Location", coordShip);
                            aciertos.add(acierto);
                        }
                    }
                }
            }
        }
        return aciertos;
    }


// ---------------------------------------------- Dto para Tocados y para hundidos
    // con la lista del metodo anterior preparo informacion de Tocados o hundidos, dependiendo del estado de "onlyHits"
    private Map<String, Object> makeHitsDTO(List<Map> aciertos, Boolean onlyHits) {
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            List<String> coordPatrolBoat = new LinkedList<>();
            List<String> coordBattleShip = new LinkedList<>();
            List<String> coordDestroyer = new LinkedList<>();
            List<String> coordSubmarine = new LinkedList<>();
            List<String> coordAircraftCarrier = new LinkedList<>();
            Integer tpb = 0, tbs=0, tsm=0, tdest=0, tacc=0;
            String shipType1;
            String turn;
            //separe los for anidados de este metodo //   System.out.println(aciertos);
            //son apenas 5 lineas mas de codigo, y me permite separar como obtengo la informacion y como la uslo para generar el dto.
            // en caso de de los hits, paso un array por cada barco, con un string par turno/coordenada del tiro

        for(Map onTarget : aciertos) {

                shipType1 = onTarget.get("ShipType").toString();
                turn = onTarget.get("Turn").toString();
                switch(shipType1) {
                    case "PatrolBoat":
                        coordPatrolBoat.add(turn);
                        if (tpb<parseInt(turn)) {
                            tpb = parseInt(turn);
                        }
                        break;
                    case "BattleShip":
                        coordBattleShip.add(turn);
                        if (tbs<parseInt(turn)) {
                            tbs = parseInt(turn);
                        }
                        break;
                    case "Submarine":
                        coordSubmarine.add(turn);
                        if (tsm<parseInt(turn)) {
                            tsm = parseInt(turn);
                        }
                        break;
                    case "Destroyer":
                        coordDestroyer.add(turn);
                        if (tdest<parseInt(turn)) {
                            tdest = parseInt(turn);
                        }
                        break;
                    case "AircraftCarrier":
                        coordAircraftCarrier.add(turn);
                        if (tacc<parseInt(turn)) {
                            tacc = parseInt(turn);
                        }
                        break;
                }
            }
            if (onlyHits) {
                dto.put("PatrolBoat", coordPatrolBoat);
                dto.put("Submarine", coordSubmarine);
                dto.put("BattleShip", coordBattleShip);
                dto.put("Destroyer", coordDestroyer);
                dto.put("AircraftCarrier", coordAircraftCarrier);
            }else {
                // en caso de los hundidos, paso el barco hundido y el turno en que se termino de hundir
                if (coordPatrolBoat.size() == 2) {
                    dto.put("PatrolBoat",tpb);
                }
                if (coordSubmarine.size() == 3) {
                    dto.put("Submarine", tsm);
                }
                if (coordBattleShip.size() == 3) {
                    dto.put("BattleShip", tbs);
                }
                if (coordDestroyer.size() == 4) {
                    dto.put("Destroyer", tdest);
                }
                if (coordAircraftCarrier.size() == 5) {
                    dto.put("AircraftCarrier", tacc);
                }
            }
        return dto;
    }

}