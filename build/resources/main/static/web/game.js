var app = new Vue({
			el: "#app",
			data:{
			number : [" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
			letter: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
			player: "",
			rival: "",
			game: "",
			state: "",
			}			
			})
		

// variables globales
var identity = "";
var pathname1 = window.location.href;
var posicion = pathname1.indexOf("=")+1;
if (posicion > 0)
	identity = pathname1.substring(posicion, pathname1.length);
var	indice = parseInt(identity);
// array global
var shipList = new Array();
var salvoList = new Array();







function cargoPag(){

var url = "/api/game_view/"+identity;
//console.log(url);

 fetch(url, {
  headers: new Headers({
  })
})
.then(response => response.json())
.then(dat => {
	dataGamePlayer = dat;
	extraigoDatos();
	dibujoBarcos();
	dibujoSalvos(dataGamePlayer[0].SalvosPlayer, 100);
	if ('SalvosRival' in dataGamePlayer[0])
		dibujoSalvos(dataGamePlayer[0].SalvosRival, 0);
	if (dataGamePlayer[0].GameState.indexOf("Turn") != -1 || dataGamePlayer[0].GameState.indexOf("Game Over") != -1){
		tableHistory(true);
		tableHistory(false);
		
	}
	document.body.style.opacity = "1";
	
	})
	
	
//	app.list = dataGamePlayer;	 

.catch(error => console.error(error))

}



// eventlistners casillas de ships, cambian de color a negro (selecciona), o blanco (para cancelar la seleccion). Agrego casillas de salvos, mismo comportamiento
for (i=0; i<200; i++){
		
		document.getElementsByName("casilla")[i].addEventListener('click', myFunction);
	//	document.getElementsByName("casilla")[i].style.backgroundColor = "none";	
}

function myFunction(){
			if (this.style.backgroundColor=="black")
				myFunction1(this);
			else
				this.style.backgroundColor = "black";
}

function myFunction1(x){
				x.style.backgroundColor = "transparent";
}



function extraigoDatos(){
	app.game = dataGamePlayer[0].Game.GameId;
	app.state = dataGamePlayer[0].GameState;
	
	gamePlayerId = dataGamePlayer[0].Game.GamePlayersInfo[0].GamePlayerId;
  
	//console.log("Game Id "+ dataGamePlayer[0].Game.GameId);
	
	if (indice == gamePlayerId) {
		app.player = dataGamePlayer[0].Game.GamePlayersInfo[0].PlayerInfo;
		if (dataGamePlayer[0].Game.GamePlayersInfo.length > 1)
			app.rival = dataGamePlayer[0].Game.GamePlayersInfo[1].PlayerInfo;
		else
			app.rival = "--";
	}else{
		app.player = dataGamePlayer[0].Game.GamePlayersInfo[1].PlayerInfo;
		gamePlayerId = dataGamePlayer[0].Game.GamePlayersInfo[1].GamePlayerId;
		app.rival = dataGamePlayer[0].Game.GamePlayersInfo[0].PlayerInfo;
	}
	//console.log("Game Player id " + gamePlayerId);
	if (app.state.indexOf("Your") == -1 && app.state.indexOf("Over") == -1){
			//si no detecta la palabra Your (o Game Over) en el estado del game, hay que esperar accion del oponente, settimeout para volver a llamar a cargo pagina
			//agregaSalvoes.style.display = "none";
		if(app.state != "Add Ships")
			setTimeout(cargoPag, 10000);
	}

}


// se dibujan los barcos propios en la 1era tabla. Es la info que viene del server. Los barcos son de color gris
// remuevo los addeventlistener de la cuadricula de barcos (por ahora), para que al clickear la  cuadricula, mo haya cambios. En este punto, los barcos ya fueron cargados al server
function dibujoBarcos(){
	if (dataGamePlayer[0].Ship.length == 0){
		document.getElementById("agregaBarcos").style.innerHTML="Post Ships";
	}else{
	for (i=0; i<100; i++){
					document.getElementsByName("casilla")[i].removeEventListener("click", myFunction);
		}
		document.getElementById("trazo").innerHTML=armoInfo();
		document.getElementById("agregaBarcos").innerHTML="Ships";
		document.getElementById("agregaBarcos").disabled = true;
	}
	dataGamePlayer[0].Ship.forEach(function(element){
		element.ShipLocations.forEach(function(element){
			letra = element.substr(-20,1);
			numero = element.substr(1);
			numCasilla = app.letter.indexOf(letra)*10+parseInt(numero)-1;
			document.getElementsByName("casilla")[numCasilla].style.backgroundImage = "url('casilla1.png')";
		})
	})
	
}



// se dibujan los disparos del jugador en la 2da cuadricula. y los disparos del rival en la cuadricula con los barcos
function dibujoSalvos(disparos,i){
	var turno;
	disparos.forEach(function(element){
		turno = element.Turn;
		element.Locations.forEach(function(element){
			letra = element.substr(-20,1);
			numero = element.substr(1);
			numCasilla = app.letter.indexOf(letra)*10+parseInt(numero)-1+i;
			if (i>0){
				document.getElementsByName("casilla")[numCasilla].innerHTML = '<div class="bullet"><strong>'+turno+'</strong></div>';
				document.getElementsByName("casilla")[numCasilla].style.backgroundImage = "url('bullet.png')";
			}else{
		
				if(document.getElementsByName("casilla")[numCasilla].style.backgroundImage == 'url("casilla1.png")'){
					document.getElementsByName("casilla")[numCasilla].style.backgroundImage = "url('tocado.png')";
					document.getElementsByName("casilla")[numCasilla].innerHTML = '<div class="bullet1"><strong>'+turno+'</strong></div>';
					
					
				}else{
					document.getElementsByName("casilla")[numCasilla].innerHTML = '<div class="bullet"><strong>'+turno+'</strong></div>';
					document.getElementsByName("casilla")[numCasilla].style.backgroundImage = "url('bullet.png')";
				}
			
			}
			// y remuevo el eventListener en esta casilla para que no se acepte volver a tirar en el mismo casillero
			document.getElementsByName("casilla")[numCasilla].removeEventListener("click", myFunction);
		})
	})
	
}

	function logout() {
            fetch("/api/logout", {
                    credentials: 'include',
                    method: 'POST',
                })
				.then(r => console.log(r))
                .then(r => document.location.href="games.html")
                .catch(e => console.log(e));

        }
		
		
		// esta funcion postea los barcos
		 function addShip() {
            var gamePlayerId = indice;
            var url = "/api/games/players/"+gamePlayerId+"/ships";
            fetch(url, {
                credentials: 'include',
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify(shipList)
            }).then(function (response) {
                response.status

                if (response.status == 201) {
                    return response.json().then((data) => {
                     //   console.log("ok");
					//	alert(data.Response)
						document.getElementById("agregaBarcos").innerHTML="Ships";
						//document.location.reload();
						cargoPag();
                    })
                } else {
                    return response.json().then((data) => {
                        alert(data.Response)
                    
                    })
                }
            }, function (error) {
                alert(error.message)

            })
        }
		
		
		
		
		// funcion que detecta las casillas clickeadas, y envia el barco si esta ok 
		// por ahora en caso de barcos con casillas mayores al seleccionado (ejemplo trata de ingresar un barco de 3 y es un patrolboat, toma las 2 primeras casillas
		// la comparacion para haceptar un barco con casillas alienadas pasa por la 1era y la ultima casilla. a la hora de haceptar el barco, corrige el trazo del barco
		function creoBarcos(size, type) {
			let j=0;
			let firstIndex=0;
			let lastIndex=0;
			let k = 0;
			let doAdd = true;
			let accept = false;
			var locaciones = new Array(size);
			for (i=0; i<100; i++){
				if (document.getElementsByName("casilla")[i].style.backgroundColor == "black"){
					j++;
					if (j == 1)
						firstIndex = i;
					if (j == 2)
						lastIndex = i;	
				}
			}
			
			if (lastIndex - firstIndex == (size-1)*10){
			//	console.log("vertical");
				for(i=0;i<size;i++){
					locaciones[i] = firstIndex+i*10;
				}				
			}
			// en caso horizontal, las casillas deben ser consecutivas, y ademas tener la misma decena
			if (lastIndex - firstIndex == size-1 && parseInt(lastIndex/10) == parseInt(firstIndex/10)){
			//	console.log("horizontal");

				for(i=0;i<size;i++){
					locaciones[i] = firstIndex+i;
				}	
			}
			//console.log(locaciones.length);
			if(locaciones[0]==null){
			
				limpieza();
				doAdd = false;
			}
			if (locaciones.length==size && doAdd && shipList.length<5)
				armoJson(type,locaciones);
			else
				alert("Select the first and last cell of the boat, and then press the corresponding button");
				limpieza();
		}
		
		
		
		
		// esta funcion arma el json, una lista de barcos (locations y type)
		function armoJson(type, coord){
				let letra="";
				var ship = new Object();
				var coordenadas = new Array();
					ship.shipType = type;
				//	console.log(type);
					document.getElementById(type).style.display = "none";
					coord.map(function(element){
						// a las casillas verdes (barco ya agegado listo para submit) les quito el eventlistener
					document.getElementsByName("casilla")[element].style.backgroundColor = "blue";	
					document.getElementsByName("casilla")[element].removeEventListener("click", myFunction);					
					// con esto traduzco coordenadas en numeros, a A1 etc. Uso el array de letras en vue	
					//	console.log(parseInt(element/10));
					//	console.log(element%10+1);
						letra = app.letter[parseInt(element/10)];
					//	console.log(letra);
						element = letra+(element%10+1);
					limpieza();
					coordenadas.push(element);
				})
					
					
					ship.shipLocations = coordenadas;
					
				shipList.push(ship);
			//	console.log(shipList);
			
		}
	

// limpio casillas negras	y azules
	function limpieza(){
		for (i=0; i<200; i++){
		
		if(document.getElementsByName("casilla")[i].style.backgroundColor=="black")
			document.getElementsByName("casilla")[i].style.backgroundColor = "transparent";
		
		
	}
		
}



function addSalvo(salvoList1) {
	var gamePlayerId = indice;
	var salvo = new Object();
//	salvo.turn = dataGamePlayer[0].SalvosPlayer.length+1;
//	salvo.salvoLocations = salvoList1;
//	salvo.gamePlayer = gamePlayerId;
	//salvo = {turn: 1, salvoLocations: ['A1','A2','A3','A4','A5']};
	//console.log(salvo);
	
            var url = "/api/games/players/"+gamePlayerId+"/salvoes";
            fetch(url, {
                credentials: 'include',
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(salvoList1)
            }).then(function (response) {
                response.status

                if (response.status == 201) {
                    return response.json().then((data) => {
                    //    console.log("ok");
					//	alert(data.Response)
						//document.location.reload();
						limpieza();
						cargoPag();
                    })
                } else {
                    return response.json().then((data) => {
                        alert(data.Response)
                      //  limpieza();
                    
                    })
                }
            }, function (error) {
                alert(error.message)

            })
        }
	


function creoSalvos() {
	var j = 0;
	var locaciones = new Array();
	var locaciones1 = new Array();
			for (i=100; i<200; i++){
				if (document.getElementsByName("casilla")[i].style.backgroundColor == "black"){
							locaciones[j] = i;
							j++;
				}
			}
			if (locaciones.length <= 5){
					locaciones.map(function(element){				
					// con esto traduzco coordenadas en numeros, a A1 etc. Uso el array de letras en vue. En este caso las casillas arrancan en 100	
						letra = app.letter[parseInt((element-100)/10)];
						element = letra+parseInt((element-100)%10+1);
						locaciones1.push(element);
					})
				addSalvo(locaciones1);
			}else
				alert("Submit 5 shots");
			//console.log(locaciones1);
}			



function tableHistory(condicional){
	var color="";
	if (condicional){
		var patrolBoat = dataGamePlayer[0].PlayerHits.PatrolBoat;
		var battleShip = dataGamePlayer[0].PlayerHits.BattleShip;
		var submarine = dataGamePlayer[0].PlayerHits.Submarine;
		var destroyer = dataGamePlayer[0].PlayerHits.Destroyer;
		var aircraftCarrier = dataGamePlayer[0].PlayerHits.AircraftCarrier;
		var hundidos = dataGamePlayer[0].RivalShipsSunk;
		var tableId = document.getElementById("player1");
		var contentIds = [document.getElementById("ppb"),document.getElementById("pbs"),document.getElementById("psub"), document.getElementById("pdest"), document.getElementById("pair")];
		var color = "#a3b854";
	}else{
		var patrolBoat = dataGamePlayer[0].RivalHits.PatrolBoat;
		var battleShip = dataGamePlayer[0].RivalHits.BattleShip;
		var submarine = dataGamePlayer[0].RivalHits.Submarine;
		var destroyer = dataGamePlayer[0].RivalHits.Destroyer;
		var aircraftCarrier = dataGamePlayer[0].RivalHits.AircraftCarrier;
		var hundidos = dataGamePlayer[0].PlayerShipsSunk;
		var tableId = document.getElementById("rival");
		var contentIds = [document.getElementById("rpb"),document.getElementById("rbs"),document.getElementById("rsub"), document.getElementById("rdest"), document.getElementById("rair")];
		var color = "#ff5338";
	}


	patrolBoat.sort(function(a,b){return a - b;});
	patrolBoat.forEach(function(element, index){
		tableId.childNodes[index].style.backgroundColor = color;
		tableId.childNodes[index].style.borderRadius = "50%";
		tableId.childNodes[index].innerHTML = element;
	
	})
	battleShip.sort(function(a,b){return a - b;});
	battleShip.forEach(function(element, index){
		tableId.childNodes[index+3].style.backgroundColor = color;
		tableId.childNodes[index+3].style.borderRadius = "50%";
		tableId.childNodes[index+3].innerHTML = element;
	})
	submarine.sort(function(a,b){return a - b;});	
	submarine.forEach(function(element, index){
		tableId.childNodes[index+7].style.backgroundColor = color;
		tableId.childNodes[index+7].style.borderRadius = "50%";
		tableId.childNodes[index+7].innerHTML = element;
	})
	destroyer.sort(function(a,b){return a - b;});	
	destroyer.forEach(function(element, index){
		tableId.childNodes[index+11].style.backgroundColor = color;
		tableId.childNodes[index+11].style.borderRadius = "50%";
		tableId.childNodes[index+11].innerHTML = element;
	})
	aircraftCarrier.sort(function(a,b){return a - b;});
	aircraftCarrier.forEach(function(element, index){
		tableId.childNodes[index+16].style.backgroundColor = color;
		tableId.childNodes[index+16].style.borderRadius = "50%";
		tableId.childNodes[index+16].innerHTML = element;
	})	
	if (hundidos.PatrolBoat)
			contentIds[0].innerHTML = "<strong>Sunk!</strong>";
	if (hundidos.BattleShip)
			contentIds[1].innerHTML = "<strong>Sunk!</strong>";
	if (hundidos.Submarine)
			contentIds[2].innerHTML = "<strong>Sunk!</strong>";
	if (hundidos.Destroyer)
			contentIds[3].innerHTML = "<strong>Sunk!</strong>";
	if (hundidos.AircraftCarrier)
			contentIds[4].innerHTML = "<strong>Sunk!</strong>";

}	

function armoInfo(){
	let expresion;
	let expresion1;
	let player1;
	let round;
	let remainingSalvoes;
	let i=0;
	if (dataGamePlayer[0].Game.GamePlayersInfo.length>1 && dataGamePlayer[0].GameState != "Waiting for Opponet s Ships"){
	//	console.log(dataGamePlayer[0].Game.GamePlayersInfo[1].GamePlayerId);
		if (dataGamePlayer[0].Game.GamePlayersInfo[0].GamePlayerId < dataGamePlayer[0].Game.GamePlayersInfo[1].GamePlayerId){
			if (dataGamePlayer[0].Game.GamePlayersInfo[0].PlayerInfo == app.player){
			// player es el 1
				player1 = true;
			
			}else{
				player1 = false;
			}
		}else{
			if (dataGamePlayer[0].Game.GamePlayersInfo[0].PlayerInfo == app.player){
			// player es el 2
				player1 = false;
			}else{
				player1 = true;
			}		
		}
	
		if (player1){
			round = dataGamePlayer[0].SalvosRival.length+1;
			remainingSalvoes = 5 - Object.keys(dataGamePlayer[0].PlayerShipsSunk).length;
			
			if(dataGamePlayer[0].GameState.indexOf("Your") != -1)
				expresion1 = '<div><h3>Shoot '+remainingSalvoes+'!</h3></div>';
			else
				expresion1 = '<div><h3>Waiting</h3><div class="lds-ellipsis"><div></div><div></div><div></div><div></div></div></div>';
			if(dataGamePlayer[0].GameState.indexOf("Game Over") != -1){
				expresion1 = '';
				round = round - 1;
			}
				
			
			expresion = '<div><h5><strong>Player1:</strong> '+ app.player+'</h5></div><div><h5><strong>Player2:</strong> '+app.rival+'</h5></div><div><h3>Round</h3></div><div><h2>'+round+'<h2></div>'+ expresion1;
			
		}else{
			round = dataGamePlayer[0].SalvosPlayer.length+1;
		//	console.log("hundieron "+ Object.keys(dataGamePlayer[0].PlayerShipsSunk).length);
			
			Object.entries(dataGamePlayer[0].PlayerShipsSunk).forEach(function(element){
					if (element[1] < (dataGamePlayer[0].SalvosPlayer.length+1))
											i++;
			})
			remainingSalvoes = 5 - i;
			if(dataGamePlayer[0].GameState.indexOf("Your") != -1)
				expresion1 = '<div><h3>Shoot '+remainingSalvoes+'!</h3></div>';
			else
				expresion1 = '<div><h3>Waiting</h3><div class="lds-ellipsis"><div></div><div></div><div></div><div></div></div></div>';
			if(dataGamePlayer[0].GameState.indexOf("Game Over") != -1){
				expresion1 = '';
				round = round - 1;
			}
			expresion = '<div><h5><strong>Player1:</strong> '+ app.rival +'</h5></div><div><h5><strong>Player2:</strong> '+app.player+'</h5></div><div><h3>Round</h3></div><div><h2>'+round+'<h2></div>'+ expresion1;
		}
	
	
	}else{
		expresion = '<div class="lds-ellipsis"><div></div><div></div><div></div><div></div></div>';
	}
	return (expresion);
	
	
	
}



 function barcosAlAzar() {
	
	 for (i=0; i<200; i++){
		if(document.getElementsByName("casilla")[i].style.backgroundColor=="blue")
			document.getElementsByName("casilla")[i].style.backgroundColor = "transparent";
	}
	
	// aircraft carrier
		
		 // Destroyer
		var aircraft = generateBoat(5);
		var destroyer = new Array();
		do{
			destroyer = generateBoat(4);
			i=0;
			destroyer.forEach(function(element){
				i = i+aircraft.indexOf(element);
			})
		//	console.log("la i vale "+i);
		}while(i!=-4);
		
		
		
		
		
		var battle = new Array();
		do{
			battle = generateBoat(3);
			i=0;
			battle.forEach(function(element){
				i = i+aircraft.indexOf(element);
				i = i+destroyer.indexOf(element);
			})
		//	console.log("la i vale "+i);
		}while(i!=-6);
		
		var submarine = new Array();
		do{
			submarine = generateBoat(3);
			i=0;
			submarine.forEach(function(element){
				i = i+aircraft.indexOf(element);
				i = i+destroyer.indexOf(element);
				i = i+battle.indexOf(element);
			})
		//	console.log("la i vale "+i);
		}while(i!=-9);
		
		var submarine = new Array();
		do{
			submarine = generateBoat(3);
			i=0;
			submarine.forEach(function(element){
				i = i+aircraft.indexOf(element);
				i = i+destroyer.indexOf(element);
				i = i+battle.indexOf(element);
			})
		//	console.log("la i vale "+i);
		}while(i!=-9);
		
		var patrol = new Array();
		do{
			patrol = generateBoat(2);
			i=0;
			patrol.forEach(function(element){
				i = i+aircraft.indexOf(element);
				i = i+destroyer.indexOf(element);
				i = i+battle.indexOf(element);
				i = i+submarine.indexOf(element);
			})
		//	console.log("la i vale "+i);
		}while(i!=-8);
	//	console.log(aircraft);
	//	console.log(destroyer);
	//	console.log(battle);
	//	console.log(submarine);
	//	console.log(patrol);
		 
		 
	 shipList=[{shipType:"PatrolBoat", shipLocations:patrol},{shipType:"Submarine",shipLocations:submarine},{shipType:"BattleShip",shipLocations:battle},{shipType:"Destroyer",shipLocations:destroyer},{shipType:"AircraftCarrier",shipLocations:aircraft}];
	 
	 addShip();
	 
 }
 
  function generateBoat(tamano) {
	   let vert = true;
	 let letter="";
	 let number="";
	 let coordenada = "";
	 let i=0;
	 let j=0;
	 let shipBoat = new Array();
	 
	  if (Math.random()>0.5){
		//	 console.log("vertical");
			 vert = true;
		 }else{
		//	 console.log("horizontal")
			 vert = false;
			 }
		 if (vert){
			i=Math.floor(Math.random() * (11-tamano));
			letter = app.letter[i];
			j=Math.floor(Math.random() * 10+1);
			number = app.number[j];
			coordenada = letter+number;
		//	console.log(coordenada);
			shipBoat[0]=coordenada;
			for(k=1; k<tamano; k++){
				shipBoat[k] = app.letter[i+k]+app.number[j];
			}
		 }else{
			i=Math.floor(Math.random() * 10);
			letter = app.letter[i];
			j=Math.floor((Math.random() * (11-tamano))+1);
			number = app.number[j];
			coordenada = letter+number;
		//	console.log(coordenada);
			shipBoat[0]=coordenada;
			for(k=1; k<tamano; k++){
				shipBoat[k] = app.letter[i]+app.number[j+k];
			}
		 } 
		 return(shipBoat);
  }