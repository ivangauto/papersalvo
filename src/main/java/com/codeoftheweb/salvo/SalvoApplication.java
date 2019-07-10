package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SalvoApplication  extends SpringBootServletInitializer {


	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}


	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository , GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {
			// save a couple of customers
			playerRepository.save(new Player("j.bauer@ctu.gov", passwordEncoder().encode("24")));
			playerRepository.save(new Player("c.obrian@ctu.gov", passwordEncoder().encode("42")));
			playerRepository.save(new Player("kim_bauer@gmail.com", passwordEncoder().encode("kb")));
			playerRepository.save(new Player("t.almeida@ctu.gov", passwordEncoder().encode("mole")));
			playerRepository.save(new Player("Michelle@Dessler", passwordEncoder().encode("iv")));
			// save game now, now plus 60 minutes, now plus 120 minutes
			gameRepository.save(new Game(LocalDateTime.now()));
			gameRepository.save(new Game(LocalDateTime.now().plusMinutes(60)));
			gameRepository.save(new Game(LocalDateTime.now().plusMinutes(120)));
 			// agrego game-player combination
			Game game1 = new Game(LocalDateTime.now());
			Game game2 = new Game(LocalDateTime.now().plusMinutes(120));
			Game game3 = new Game(LocalDateTime.now().plusMinutes(180));
			Game game4 = new Game(LocalDateTime.now().plusHours(4));
			Game game5 = new Game(LocalDateTime.now().plusHours(5));
			Game game6 = new Game(LocalDateTime.now().plusHours(6));
			Game game7 = new Game(LocalDateTime.now().plusHours(7));
			Game game8 = new Game(LocalDateTime.now().plusHours(10));
			Game game9 = new Game(LocalDateTime.now().plusHours(11));

			Player player1 = new Player("john1@john1.com", passwordEncoder().encode("iv"));
			Player player2 = new Player("paul1@paul1.com",  passwordEncoder().encode("iv"));
			Player player3 = new Player("george1@george1.com",  passwordEncoder().encode("iv"));
			Player player4 = new Player("asd@234.com",  passwordEncoder().encode("iv"));
			Player player5 = new Player("zxc@543.com",  passwordEncoder().encode("iv"));
			Player player6 = new Player("lkj@456.com",  passwordEncoder().encode("iv"));
			Player player7 = new Player("gtgt@135.com",  passwordEncoder().encode("iv"));
			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);
			playerRepository.save(player5);
			playerRepository.save(player6);
			playerRepository.save(player7);
			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);
			gameRepository.save(game6);
			gameRepository.save(game7);
			gameRepository.save(game8);
			gameRepository.save(game9);
			GamePlayer gamePlayer11 = new GamePlayer(game1,player1);
			GamePlayer gamePlayer12 = new GamePlayer(game1,player2);
			GamePlayer gamePlayer22 = new GamePlayer(game2,player2);
			GamePlayer gamePlayer23 = new GamePlayer(game2,player3);
			GamePlayer gamePlayer33 = new GamePlayer(game3,player3);
			GamePlayer gamePlayer34 = new GamePlayer(game3,player4);
			GamePlayer gamePlayer44 = new GamePlayer(game4,player4);
			GamePlayer gamePlayer45 = new GamePlayer(game4,player5);
			GamePlayer gamePlayer55 = new GamePlayer(game5,player5);
			GamePlayer gamePlayer56 = new GamePlayer(game5,player6);
			GamePlayer gamePlayer66 = new GamePlayer(game6,player6);
			GamePlayer gamePlayer67 = new GamePlayer(game6,player7);
			GamePlayer gamePlayer77 = new GamePlayer(game7,player7);
			GamePlayer gamePlayer71 = new GamePlayer(game7,player1);
			GamePlayer gamePlayer85 = new GamePlayer(game8,player5);
			GamePlayer gamePlayer82 = new GamePlayer(game8,player2);
			GamePlayer gamePlayer94 = new GamePlayer(game9,player4);


			gamePlayerRepository.save(gamePlayer11);
			gamePlayerRepository.save(gamePlayer12);
			gamePlayerRepository.save(gamePlayer22);
			gamePlayerRepository.save(gamePlayer23);
			gamePlayerRepository.save(gamePlayer33);
			gamePlayerRepository.save(gamePlayer34);
			gamePlayerRepository.save(gamePlayer44);
			gamePlayerRepository.save(gamePlayer45);
			gamePlayerRepository.save(gamePlayer55);
			gamePlayerRepository.save(gamePlayer56);
			gamePlayerRepository.save(gamePlayer66);
			gamePlayerRepository.save(gamePlayer67);
			gamePlayerRepository.save(gamePlayer77);
			gamePlayerRepository.save(gamePlayer71);
			gamePlayerRepository.save(gamePlayer85); // lo creo para ver el null en ships y salvos
			gamePlayerRepository.save(gamePlayer82);// lo creo para ver el null en ships y salvos
			gamePlayerRepository.save(gamePlayer94);// gameplayer con solo 1 jugador agregado al game, para testear



			// creo listas para locations barcos


			List<String> LocShip1 = new ArrayList<>();
			List<String> LocShip2 = new ArrayList<>();
			List<String> LocShip3 = new ArrayList<>();
			List<String> LocShip4 = new ArrayList<>();
			List<String> LocShip5 = new ArrayList<>();
			List<String> LocShip1a = new ArrayList<>();
			List<String> LocShip2a = new ArrayList<>();
			List<String> LocShip3a = new ArrayList<>();
			List<String> LocShip4a = new ArrayList<>();
			List<String> LocShip5a = new ArrayList<>();

			// creo lisstas para locations salvos

			List<String> LocSalvo1 = new ArrayList<>();
			List<String> LocSalvo2 = new ArrayList<>();
			List<String> LocSalvo3 = new ArrayList<>();
			List<String> LocSalvo4 = new ArrayList<>();
			List<String> LocSalvo5 = new ArrayList<>();
			List<String> LocSalvo6 = new ArrayList<>();
			List<String> LocSalvo7 = new ArrayList<>();
			List<String> LocSalvo8 = new ArrayList<>();
			List<String> LocSalvo9 = new ArrayList<>();
			List<String> LocSalvo10 = new ArrayList<>();


			// agrego locationes a la lista, barco patrolBoat
			LocShip1.add("D7"); LocShip1.add("D8");
			LocShip1a.add("E7"); LocShip1a.add("E8");

			// agrego locationes a la lista, barco battleship
			LocShip2.add("H1"); LocShip2.add("H2"); LocShip2.add("H3");
			LocShip2a.add("H4"); LocShip2a.add("H5"); LocShip2a.add("H6");

			// agrego locationes a la lista, barco 3 submarine
			LocShip3.add("B5"); LocShip3.add("B6"); LocShip3.add("B7");
			LocShip3a.add("J5"); LocShip3a.add("J6"); LocShip3a.add("J7");

			// agrego locationes a la lista, barco destroyer
			LocShip4.add("A1"); LocShip4.add("B1"); LocShip4.add("C1"); LocShip4.add("D1");
			LocShip4a.add("A4"); LocShip4a.add("B4"); LocShip4a.add("C4"); LocShip4a.add("D4");

			// agrego locations barco aircraftcarrier
			LocShip5.add("A3"); LocShip5.add("A4"); LocShip5.add("A5"); LocShip5.add("A6"); LocShip5.add("A7");
			LocShip5a.add("C6"); LocShip5a.add("C7"); LocShip5a.add("C8"); LocShip5a.add("C9"); LocShip5a.add("C10");


			// agrego locations a la lista de salvos, 5 por game player
			LocSalvo1.add("A1"); LocSalvo1.add("D8"); LocSalvo1.add("A3"); LocSalvo1.add("J8"); LocSalvo1.add("F1");
			LocSalvo2.add("C1"); LocSalvo2.add("H8"); LocSalvo2.add("A8"); LocSalvo2.add("F6"); LocSalvo2.add("F4");
			LocSalvo3.add("D1"); LocSalvo3.add("J5"); LocSalvo3.add("B8"); LocSalvo3.add("B9"); LocSalvo3.add("C5");
			LocSalvo4.add("J1"); LocSalvo4.add("F7"); LocSalvo4.add("C4"); LocSalvo4.add("C5"); LocSalvo4.add("B7");
			LocSalvo5.add("I1"); LocSalvo5.add("E3"); LocSalvo5.add("A7"); LocSalvo5.add("D7"); LocSalvo5.add("H1");
			LocSalvo6.add("G1"); LocSalvo6.add("A3"); LocSalvo6.add("J7"); LocSalvo6.add("G4"); LocSalvo6.add("C2");
			LocSalvo7.add("H1"); LocSalvo7.add("I9"); LocSalvo7.add("F5"); LocSalvo7.add("J10"); LocSalvo7.add("B10");
			LocSalvo8.add("D3"); LocSalvo8.add("C10"); LocSalvo8.add("D8"); LocSalvo8.add("B3"); LocSalvo8.add("C3");
			LocSalvo9.add("B4"); LocSalvo9.add("D8"); LocSalvo9.add("A3"); LocSalvo9.add("A5"); LocSalvo9.add("H3");
			LocSalvo10.add("A1"); LocSalvo10.add("D8"); LocSalvo10.add("A3"); LocSalvo10.add("J8"); LocSalvo10.add("I1");


			Ship ship111 = new Ship(gamePlayer11, LocShip1, "PatrolBoat");
			Ship ship211 = new Ship(gamePlayer11, LocShip2, "BattleShip");
			Ship ship311 = new Ship(gamePlayer11, LocShip3, "Submarine");
			Ship ship411 = new Ship(gamePlayer11, LocShip4, "Destroyer");
			Ship ship511 = new Ship(gamePlayer11, LocShip5, "AircraftCarrier");
			Salvo salvo111 = new Salvo(gamePlayer11, LocSalvo1, 1);
			Salvo salvo211 = new Salvo(gamePlayer11, LocSalvo2, 2);
			Salvo salvo311 = new Salvo(gamePlayer11, LocSalvo3, 3);

			Ship ship112 = new Ship(gamePlayer12, LocShip1a, "PatrolBoat");
			Ship ship212 = new Ship(gamePlayer12, LocShip2a, "BattleShip");
			Ship ship312 = new Ship(gamePlayer12, LocShip3a, "Submarine");
			Ship ship412 = new Ship(gamePlayer12, LocShip4a, "Destroyer");
			Ship ship512 = new Ship(gamePlayer12, LocShip5a, "AircraftCarrier");
			Salvo salvo112 = new Salvo(gamePlayer12, LocSalvo2, 1);
			Salvo salvo212 = new Salvo(gamePlayer12, LocSalvo4, 2);
			Salvo salvo312 = new Salvo(gamePlayer12, LocSalvo5, 3);


			// game2 . player 2
			Ship ship122 = new Ship(gamePlayer22, LocShip1, "PatrolBoat");
			Ship ship222 = new Ship(gamePlayer22, LocShip2, "BattleShip");
			Ship ship322 = new Ship(gamePlayer22, LocShip3, "Submarine");
			Ship ship422 = new Ship(gamePlayer22, LocShip4, "Destroyer");
			Ship ship522 = new Ship(gamePlayer22, LocShip5, "AircraftCarrier");
			Salvo salvo122 = new Salvo(gamePlayer22, LocSalvo3, 1);

			// game2 player 3
			Ship ship123 = new Ship(gamePlayer23, LocShip1a, "PatrolBoat");
			Ship ship223 = new Ship(gamePlayer23, LocShip2a, "BattleShip");
			Ship ship323 = new Ship(gamePlayer23, LocShip3a, "Submarine");
			Ship ship423 = new Ship(gamePlayer23, LocShip4a, "Destroyer");
			Ship ship523 = new Ship(gamePlayer23, LocShip5a, "AircraftCarrier");
			Salvo salvo123 = new Salvo(gamePlayer23, LocSalvo4, 1);

			// game3 player 3
			Ship ship133 = new Ship(gamePlayer33, LocShip1a, "PatrolBoat");
			Ship ship233 = new Ship(gamePlayer33, LocShip2a, "BattleShip");
			Ship ship333 = new Ship(gamePlayer33, LocShip3a, "Submarine");
			Ship ship433 = new Ship(gamePlayer33, LocShip4a, "Destroyer");
			Ship ship533 = new Ship(gamePlayer33, LocShip5a, "AircraftCarrier");
			Salvo salvo133 = new Salvo(gamePlayer33, LocSalvo5, 1);

			// game3 player 4
			Ship ship134 = new Ship(gamePlayer34, LocShip1, "PatrolBoat");
			Ship ship234 = new Ship(gamePlayer34, LocShip2, "BattleShip");
			Ship ship334 = new Ship(gamePlayer34, LocShip3, "Submarine");
			Ship ship434 = new Ship(gamePlayer34, LocShip4, "Destroyer");
			Ship ship534 = new Ship(gamePlayer34, LocShip5, "AircraftCarrier");
			Salvo salvo134 = new Salvo(gamePlayer34, LocSalvo6, 1);

			// game4 player 4
			Ship ship144 = new Ship(gamePlayer44, LocShip1a, "PatrolBoat");
			Ship ship244 = new Ship(gamePlayer44, LocShip2a, "BattleShip");
			Ship ship344 = new Ship(gamePlayer44, LocShip3a, "Submarine");
			Ship ship444 = new Ship(gamePlayer44, LocShip4a, "Destroyer");
			Ship ship544 = new Ship(gamePlayer44, LocShip5a, "AircraftCarrier");
			Salvo salvo144 = new Salvo(gamePlayer44, LocSalvo7, 1);

			// game4 player 5
			Ship ship145 = new Ship(gamePlayer45, LocShip1, "PatrolBoat");
			Ship ship245 = new Ship(gamePlayer45, LocShip2, "BattleShip");
			Ship ship345 = new Ship(gamePlayer45, LocShip3, "Submarine");
			Ship ship445 = new Ship(gamePlayer45, LocShip4, "Destroyer");
			Ship ship545 = new Ship(gamePlayer45, LocShip5, "AircraftCarrier");
			Salvo salvo145 = new Salvo(gamePlayer45, LocSalvo8, 1);

			// game5 player 5
			Ship ship155 = new Ship(gamePlayer55, LocShip1a, "PatrolBoat");
			Ship ship255 = new Ship(gamePlayer55, LocShip2a, "BattleShip");
			Ship ship355 = new Ship(gamePlayer55, LocShip3a, "Submarine");
			Ship ship455 = new Ship(gamePlayer55, LocShip4a, "Destroyer");
			Ship ship555 = new Ship(gamePlayer55, LocShip5a, "AircraftCarrier");
			Salvo salvo155 = new Salvo(gamePlayer55, LocSalvo9, 1);

			// game5 player 6
			Ship ship156 = new Ship(gamePlayer56, LocShip1, "PatrolBoat");
			Ship ship256 = new Ship(gamePlayer56, LocShip2, "BattleShip");
			Ship ship356 = new Ship(gamePlayer56, LocShip3, "Submarine");
			Ship ship456 = new Ship(gamePlayer56, LocShip4, "Destroyer");
			Ship ship556 = new Ship(gamePlayer56, LocShip5, "AircraftCarrier");
			Salvo salvo156 = new Salvo(gamePlayer56, LocSalvo10, 1);

			// game6 player 6
			Ship ship166 = new Ship(gamePlayer66, LocShip1a, "PatrolBoat");
			Ship ship266 = new Ship(gamePlayer66, LocShip2a, "BattleShip");
			Ship ship366 = new Ship(gamePlayer66, LocShip3a, "Submarine");
			Ship ship466 = new Ship(gamePlayer66, LocShip4a, "Destroyer");
			Ship ship566 = new Ship(gamePlayer66, LocShip5a, "AircraftCarrier");
			Salvo salvo166 = new Salvo(gamePlayer66, LocSalvo1, 1);

			// game6 player 7
			Ship ship167 = new Ship(gamePlayer67, LocShip1, "PatrolBoat");
			Ship ship267 = new Ship(gamePlayer67, LocShip2, "BattleShip");
			Ship ship367 = new Ship(gamePlayer67, LocShip3, "Submarine");
			Ship ship467 = new Ship(gamePlayer67, LocShip4, "Destroyer");
			Ship ship567 = new Ship(gamePlayer67, LocShip5, "AircraftCarrier");
			Salvo salvo167 = new Salvo(gamePlayer67, LocSalvo2, 1);

			// game7 player 7
			Ship ship177 = new Ship(gamePlayer77, LocShip1a, "PatrolBoat");
			Ship ship277 = new Ship(gamePlayer77, LocShip2a, "BattleShip");
			Ship ship377 = new Ship(gamePlayer77, LocShip3a, "Submarine");
			Ship ship477 = new Ship(gamePlayer77, LocShip4a, "Destroyer");
			Ship ship577 = new Ship(gamePlayer77, LocShip5a, "AircraftCarrier");
			Salvo salvo177 = new Salvo(gamePlayer77, LocSalvo3, 1);

			// game7 player 1
			Ship ship171 = new Ship(gamePlayer71, LocShip1a, "Destroyer");
			Ship ship271 = new Ship(gamePlayer71, LocShip2, "Submarine");
			Ship ship371 = new Ship(gamePlayer71, LocShip3a, "PatrolBoat");
			Ship ship471 = new Ship(gamePlayer71, LocShip4, "Submarine");
			Ship ship571 = new Ship(gamePlayer71, LocShip5a, "PatrolBoat");
			Salvo salvo171 = new Salvo(gamePlayer71, LocSalvo9, 1);

			// los barcos fueron creados, ahora debo agregarlos a los gameplayers
			// del libro: person.addPet(pet);
			//petRepository.save(pet); --- We need to save pet because we have changed data in it that is stored in the database, namely the owner. We do not need to save person if it was already in the database

			gamePlayer11.addShip(ship111); shipRepository.save(ship111);
			gamePlayer11.addShip(ship211); shipRepository.save(ship211);
			gamePlayer11.addShip(ship311); shipRepository.save(ship311);
			gamePlayer11.addShip(ship411); shipRepository.save(ship411);
			gamePlayer11.addShip(ship511); shipRepository.save(ship511);
			gamePlayer11.addSalvo(salvo111); salvoRepository.save(salvo111);
			gamePlayer11.addSalvo(salvo211); salvoRepository.save(salvo211);
			gamePlayer11.addSalvo(salvo311); salvoRepository.save(salvo311);

			gamePlayer12.addShip(ship112); shipRepository.save(ship112);
			gamePlayer12.addShip(ship212); shipRepository.save(ship212);
			gamePlayer12.addShip(ship312); shipRepository.save(ship312);
			gamePlayer12.addShip(ship412); shipRepository.save(ship412);
			gamePlayer12.addShip(ship512); shipRepository.save(ship512);
			gamePlayer12.addSalvo(salvo112); salvoRepository.save(salvo112);
			gamePlayer12.addSalvo(salvo212); salvoRepository.save(salvo212);
			gamePlayer12.addSalvo(salvo312); salvoRepository.save(salvo312);

			gamePlayer22.addShip(ship122); shipRepository.save(ship122);
			gamePlayer22.addShip(ship222); shipRepository.save(ship222);
			gamePlayer22.addShip(ship322); shipRepository.save(ship322);
			gamePlayer22.addShip(ship422); shipRepository.save(ship422);
			gamePlayer22.addShip(ship522); shipRepository.save(ship522);
			gamePlayer22.addSalvo(salvo122); salvoRepository.save(salvo122);

			gamePlayer23.addShip(ship123); shipRepository.save(ship123);
			gamePlayer23.addShip(ship223); shipRepository.save(ship223);
			gamePlayer23.addShip(ship323); shipRepository.save(ship323);
			gamePlayer23.addShip(ship423); shipRepository.save(ship423);
			gamePlayer23.addShip(ship523); shipRepository.save(ship523);
			gamePlayer23.addSalvo(salvo123); salvoRepository.save(salvo123);

			gamePlayer33.addShip(ship133); shipRepository.save(ship133);
			gamePlayer33.addShip(ship233); shipRepository.save(ship233);
			gamePlayer33.addShip(ship333); shipRepository.save(ship333);
			gamePlayer33.addShip(ship433); shipRepository.save(ship433);
			gamePlayer33.addShip(ship533); shipRepository.save(ship533);
			gamePlayer33.addSalvo(salvo133); salvoRepository.save(salvo133);

			gamePlayer34.addShip(ship134); shipRepository.save(ship134);
			gamePlayer34.addShip(ship234); shipRepository.save(ship234);
			gamePlayer34.addShip(ship334); shipRepository.save(ship334);
			gamePlayer34.addShip(ship434); shipRepository.save(ship434);
			gamePlayer34.addShip(ship534); shipRepository.save(ship534);
			gamePlayer34.addSalvo(salvo134); salvoRepository.save(salvo134);

			gamePlayer44.addShip(ship144); shipRepository.save(ship144);
			gamePlayer44.addShip(ship244); shipRepository.save(ship244);
			gamePlayer44.addShip(ship344); shipRepository.save(ship344);
			gamePlayer44.addShip(ship444); shipRepository.save(ship444);
			gamePlayer44.addShip(ship544); shipRepository.save(ship544);
			gamePlayer44.addSalvo(salvo144); salvoRepository.save(salvo144);

			gamePlayer45.addShip(ship145); shipRepository.save(ship145);
			gamePlayer45.addShip(ship245); shipRepository.save(ship245);
			gamePlayer45.addShip(ship345); shipRepository.save(ship345);
			gamePlayer45.addShip(ship445); shipRepository.save(ship445);
			gamePlayer45.addShip(ship545); shipRepository.save(ship545);
			gamePlayer45.addSalvo(salvo145); salvoRepository.save(salvo145);

			gamePlayer55.addShip(ship155); shipRepository.save(ship155);
			gamePlayer55.addShip(ship255); shipRepository.save(ship255);
			gamePlayer55.addShip(ship355); shipRepository.save(ship355);
			gamePlayer55.addShip(ship455); shipRepository.save(ship455);
			gamePlayer55.addShip(ship555); shipRepository.save(ship555);
			gamePlayer55.addSalvo(salvo155); salvoRepository.save(salvo155);

			gamePlayer56.addShip(ship156); shipRepository.save(ship156);
			gamePlayer56.addShip(ship256); shipRepository.save(ship256);
			gamePlayer56.addShip(ship356); shipRepository.save(ship356);
			gamePlayer56.addShip(ship456); shipRepository.save(ship456);
			gamePlayer56.addShip(ship556); shipRepository.save(ship556);
			gamePlayer56.addSalvo(salvo156); salvoRepository.save(salvo156);

			gamePlayer66.addShip(ship166); shipRepository.save(ship166);
			gamePlayer66.addShip(ship266); shipRepository.save(ship266);
			gamePlayer66.addShip(ship366); shipRepository.save(ship366);
			gamePlayer66.addShip(ship466); shipRepository.save(ship466);
			gamePlayer66.addShip(ship566); shipRepository.save(ship566);
			gamePlayer66.addSalvo(salvo166); salvoRepository.save(salvo166);

			gamePlayer67.addShip(ship167); shipRepository.save(ship167);
			gamePlayer67.addShip(ship267); shipRepository.save(ship267);
			gamePlayer67.addShip(ship367); shipRepository.save(ship367);
			gamePlayer67.addShip(ship467); shipRepository.save(ship467);
			gamePlayer67.addShip(ship567); shipRepository.save(ship567);
			gamePlayer67.addSalvo(salvo167); salvoRepository.save(salvo167);

			gamePlayer77.addShip(ship177); shipRepository.save(ship177);
			gamePlayer77.addShip(ship277); shipRepository.save(ship277);
			gamePlayer77.addShip(ship377); shipRepository.save(ship377);
			gamePlayer77.addShip(ship477); shipRepository.save(ship477);
			gamePlayer77.addShip(ship577); shipRepository.save(ship577);
			gamePlayer77.addSalvo(salvo177); salvoRepository.save(salvo177);

			gamePlayer71.addShip(ship171); shipRepository.save(ship171);
			gamePlayer71.addShip(ship271); shipRepository.save(ship271);
			gamePlayer71.addShip(ship371); shipRepository.save(ship371);
			gamePlayer71.addShip(ship471); shipRepository.save(ship471);
			gamePlayer71.addShip(ship571); shipRepository.save(ship571);
			gamePlayer71.addSalvo(salvo171); salvoRepository.save(salvo171);

			// agrego scores




		};
	}

}




@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getuserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		}).passwordEncoder(passwordEncoder);
	}

	
}


@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/web/games.html").permitAll()
				.antMatchers("/web/games.js").permitAll()
				.antMatchers("/web/games.css").permitAll()
				.antMatchers("/favicon.ico").permitAll()
				.antMatchers("/api/login").permitAll()
				.antMatchers("/api/games").permitAll()
				.antMatchers("/api/players").permitAll()
				.antMatchers("/api/books").permitAll()
				.antMatchers("/**").hasAuthority("USER")
				.and()
				.formLogin();

		http.formLogin()
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/api/login")
				.permitAll();


		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}

