/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "/CA1/api/groupmembers/"
var table = document.getElementById("groupTable");

function buildTable(){
    fetch(url+"all")
    .then(res => res.json()) //in flow1, just do it
    .then(data => {
        for (var d of data) {
            row = table.insertRow();
            row.id = d.id;
            row.insertCell(0).innerHTML = d.name;
            row.insertCell(1).innerHTML = d.sId;
            row.insertCell(2).innerHTML = d.color; 
            }
        });

  }
buildTable();

function refresh(){
    table.innerHTML = "";
    buildTable();
}