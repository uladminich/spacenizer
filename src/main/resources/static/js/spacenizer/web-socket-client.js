let CLIENT = {};

CLIENT.initConnection = function () {
    console.log("initConnection start ");

    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');

    CLIENT.socket = new WebSocket("ws://localhost:8080/board/" + id + '/' + CLIENT.name);

    CLIENT.socket.onmessage = function(event) {
        CLIENT.state = JSON.parse(event.data);
        if (CLIENT.state.action === 'start_completed') {

            // init game stat
            $('#start-game-button').addClass('hidden');
            $('#game-stat-red-amount-wrapper').removeClass('hidden');
            $('#game-stat-red-count').text(CLIENT.state.redResourceCount);

            // init players info
            $('#user-cards-resource-info').removeClass('hidden');
            let currentPlayer = getCurrentPlayer(CLIENT.state.players);
            for (let i = 0; i < currentPlayer.availableCards.length; i++) {
                let card =  currentPlayer.availableCards[i];
                $('#section-user-cards').append(`<span title="${card.description}" class="card-available">${card.name}</span>`)
            }

            // init board zone
            for (let i = 0; i < CLIENT.state.players.length; i++) {
                let zone = $('#main-board-zone-' + i);
                let playerForZone = CLIENT.state.players[i];
                zone.attr('data-player-id', playerForZone.name);

                zone.append(`<div>
                      [<span>${playerForZone.name}</span>] |
                      КР:
                      З - <span title="Запас">${playerForZone.redAmount}</span>;
                      П - <span title="Производство">${playerForZone.redProduction}</span>;
                      Р - <span title="Расход">${playerForZone.redConsumption}</span>;
                </div>`);

                for(let i = 0; i < playerForZone.activeCards.length; i++) {
                    zone.append(`<div class="active-card-item" title="${playerForZone.activeCards[i].description}">S</div>`)
                }
            }

        }

        let playerCount = CLIENT.state.players.length;
        $('#game-stat-player-count').text(playerCount);
        if (isCreator(CLIENT.state.players)) {
            $('#start-game-button').removeClass('hidden');
        }

    };

    CLIENT.socket.onopen = function(event) {
        console.log("onopen");

    };

    CLIENT.socket.onclose = function(event) {
        if (!event.wasClean) {
            console.log('[close] Соединение прервано, код=${event.code} причина=${event.reason}`');
        }
    };

    console.log("initConnection end ");
}

CLIENT.disconnect = function() {
    CLIENT.socket.close();
}

CLIENT.startGame = function() {
    CLIENT.state.action = 'start';
    CLIENT.socket.send(JSON.stringify(CLIENT.state));
}
function isCreator(players) {
    return players.some(el => el.name == CLIENT.name && el.creator);
}

function getCurrentPlayer(players) {
    return players.find((p) => p.name === CLIENT.name);
}


