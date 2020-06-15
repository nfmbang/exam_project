/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var scoreboard = $('#table')[0];
var url = "https://nbang.dk/CA1/api/joke"
var joke;
var form = $("#pointForm")[0]

function putJoke(Joke) {
    $("#joke").html(Joke.Joke);
    $("#creator").html(Joke.Creator);
    $("#score").html(Joke.Score / Joke.Votes);

}

function getRandom() {
    fetch(url)
            .then(function (response) {
                return response.json();
            })
            .then(function (myJson) {
                joke = myJson;
                putJoke(myJson);
            });
}


function getScoreboard() {
    scoreboard.innerHTML="";
    fetch(url + "/all").then(function (response) {
        return response.json();
    }).then(function (myJson) {
        let sorted = myJson.sort(function (a, b) {
            return b.Score / b.Votes - a.Score / a.Votes
        })
        DrawBoard(myJson);
    });

}

function jokeID(e){
    fetch(url+"/"+e.target.parentNode.id)
            .then(function (response) {
                return response.json();
            })
            .then(function (myJson) {
                joke = myJson;
                putJoke(myJson);
            });
}

function DrawBoard(Jokes) {
    for (var x of Jokes) {
        let row = scoreboard.insertRow();
        row.id = x.id;
        row.insertCell(0).innerHTML = x.Creator;
        row.insertCell(1).innerHTML = x.Score / x.Votes;
        row.insertCell(2).innerHTML = x.Votes;
        row.insertCell(3).innerHTML = x.id;
    }
}
getRandom();
getScoreboard();

function post() {
    fetch(url + "/vote", {
        method: 'post',
        body: "param1=" + JSON.stringify(joke) + "&param2=" + form.point.value,
        headers: {'Content-type': 'application/x-www-form-urlencoded'}
    });
    getRandom();
    getScoreboard();
}
function dispVote() {
$("#vote").show();
}


