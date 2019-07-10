var app = new Vue({
			el: "#app",
			data:{
			detail : "",
			list: "",
			leader: ""
			},
		methods: {
				joinGame:function(id){
           
            body = "gameId="+id;
                fetch("/api/game/"+id+"/players", {
                    method: "POST",
                    body: body,
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    credentials: "same-origin"
                }).then(function (response) {
                        response.status

                        if (response.status == 201) {
							return response.json().then((data) => { document.location.href = 'game.html?gp=' + data.GamePlayerId })
                        } else if(response.status == 403){
                           return response.json().then((data) => { alert(data.Response) 
							if (data.Response == "Game is full"){
								information();
							}
						   })
							
                        }else{
                           return response.json().then((data) => { alert("Login or SignUp first") })}
                    }, function (error) {
                    error.message 
                
					})

					}
				}
			})




function information(){
fetch("/api/games", {
  headers: new Headers({
  })
})
.then(response => response.json())
.then(dat => {
	dataGamePlayer = dat;
	//console.log(dataGamePlayer);
	
	//console.log(dataGamePlayer.CurrentPlayer);
	if (dataGamePlayer.CurrentPlayer  == "None"){
		document.getElementById("loginstatus").innerHTML='<input  id="username1"  type="email" placeholder="username"><input  id="password1" type="password" placeholder="password"><button onclick="login()" class="btn-success">LogIn</button><button onclick="signup()" class="btn-success">SignUp</button>';
	}else{
		logedState();
	}
	
	armoChart();
	armoLeader();
	app.list = armoTabla(dataGamePlayer);
	document.body.style.opacity = "1";
})
.catch(error => console.error(error))

}



var players = new Array();
var leaders = new Array();
var dataGamePlayer;
var userLoged = "none";

function armoTabla(objeto){
	var jugador;
	objeto.Games.map(function(cadena) {
		
		if (cadena.GamePlayersInfo.length == 0){
			cadena.GamePlayersInfo = "none";
			cadena.login = true;
		}else {
			jugador = "";
			for (i=0; i<cadena.GamePlayersInfo.length; i++){
				if (cadena.GamePlayersInfo[i].PlayerInfo == userLoged){
					cadena.Link= 'game.html?gp=' + cadena.GamePlayersInfo[i].GamePlayerId;
				}
				if (cadena.GamePlayersInfo[i].PlayerInfo.indexOf("@")==-1)
					jugador += cadena.GamePlayersInfo[i].PlayerInfo + "; ";
				else
					jugador += cadena.GamePlayersInfo[i].PlayerInfo.substring(0,cadena.GamePlayersInfo[i].PlayerInfo.indexOf("@"))+"; ";
			}
		//	console.log(cadena.GamePlayersInfo.length);
			if (cadena.GamePlayersInfo.length<2)
				cadena.login = true;
			else
				cadena.login = false;
			
			cadena.GamePlayersInfo  = jugador;	
			
		}
		//console.log(cadena.login);		
	})
	
	return objeto.Games;
}


function armoChart(){
var i = 0;
	dataGamePlayer.Games.forEach(function(element){	
		if(element.GamePlayersInfo.length>0){
			
			if ('Score' in element.GamePlayersInfo[0]){
				players[i] = new Object();
				players[i].player1  = element.GamePlayersInfo[0].PlayerInfo + " " + element.GamePlayersInfo[0].Score; 	
				players[i].player2 = element.GamePlayersInfo[1].PlayerInfo + " " + element.GamePlayersInfo[1].Score;	
				i++;
			}
			
			}
		})
		app.detail = players
}


function armoLeader(){
	
	var i = 0;
	var acumulado= 0;
	var won = 0;
	var lost = 0;
	var tie = 0;
	dataGamePlayer.LeaderBoard.forEach(function(element, index){
		leaders[index] = new Object();
		if(element.Scores.length>0){
			element.Scores.forEach(function(element){
				
				switch (element) {
					case 1:
						won++;
					break;
					case 0.5:
						tie	++;
					break;
					case 0:
						lost++;
					break;
				}
			
				acumulado += element;	
			})
		}
		leaders[index].player = element.UserName;
		leaders[index].score = acumulado;
		leaders[index].won = won;
		leaders[index].tie = tie;
		leaders[index].lost = lost;
		leaders.sort(function (a, b) {
		if (a.score < b.score) {
			return 1;
			}
		if (a.score > b.score) {
			return -1;
			}

		return 0;
		});
		acumulado = 0;
		won = 0;
		tie = 0;
		lost = 0;
	})
	app.leader = leaders;
	leaders =[];
}


    function login() { 
        //    console.log(username1.value);
		//	   console.log(password1.value);
            if (username1.value == "" || password1.value == "") {
                alert("please fill the form")
            } else {
				var body = "username=" + username1.value + "&password=" + password1.value
				// console.log(body);
                fetch("/api/login", {
                    method: "POST",
					body: body,
                    headers: {
				
                        'Content-Type': 'application/x-www-form-urlencoded'
                       
                    },
					credentials: "same-origin"
                }).then(function (response) {
                        response.status

                        if (response.status == 200) {
                        //   console.log("ok");
						   information();
                        } else  {
                            alert("wrong password or username")
                        }
                    },              
  
                            function (error) {
                                error.message 

                    });
            }
	}
        
		
	function logedState(){
	userLoged = dataGamePlayer.CurrentPlayer;
	//console.log("hola");
		document.getElementById("loginstatus").innerHTML="<h4><strong>"+dataGamePlayer.CurrentPlayer+"</strong></h4><button onclick='logout()' class='paper-btn btn-primary'>Logout</button>";
	}
	
	
	function logout() {
            fetch("/api/logout", {
                    credentials: 'include',
                    method: 'POST',
                })
				//.then(r => console.log(r))
                .then(r => information())
                .catch(e => console.log(e));
        }
		
		
		function signup() {
            if (username1.value == "" || password1.value == "") {
                alert("please fill the form")
            } else {
                var body = "username=" + username1.value + "&password=" + password1.value;
                fetch("/api/players", {
                    method: "POST",
                    body: body,
                    headers: {
                 
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    credentials: "same-origin"
                }).then(function (response) {
                    response.status
                    if (response.status == 201) {
                        alert("account created");
						login();
                    } else if(response.status == 403){
                        alert("user added already")
                    }
                }, function (error) {
                    error.message 
                })

            }
        }
		
		
		
		function createGame(){
            fetch("/api/games", {
                    method: "POST",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    credentials: "same-origin"
                }).then(function (response) {
                        response.status

                        if (response.status == 201) {
                            return response.json().then((data) => { document.location.href = 'game.html?gp=' + data.GamePlayerId })
                        } else if(response.status == 403){
                        alert("No user Logged-In")
						}
                    }, function (error) {
                    error.message
                })
        }
		
		