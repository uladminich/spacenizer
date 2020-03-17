let CLIENT = {};

CLIENT.COMMAND_START_GAME = 'start';
CLIENT.COMMAND_START_GAME_COMPLETED = 'start_completed';
CLIENT.COMMAND_PLAY_CARD = 'play_card';

CLIENT.initConnection = function () {
    console.log("initConnection start ");

    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');

    CLIENT.socket = new WebSocket("ws://localhost:8080/board/" + id + '/' + CLIENT.name);

    CLIENT.socket.onmessage = function(event) {
        CLIENT.state = JSON.parse(event.data);
        if (CLIENT.state.action && CLIENT.state.action.name === CLIENT.COMMAND_START_GAME_COMPLETED) {

            // init game stat
            $('#start-game-button').addClass('hidden');
            $('#game-stat-red-amount-wrapper').removeClass('hidden');
            $('#game-stat-blue-amount-wrapper').removeClass('hidden');
        }

        $('#game-stat-red-count').text(CLIENT.state.redResourceCount);
        $('#game-stat-blue-count').text(CLIENT.state.blueResourceCount);
        let activePlayer = getActivePlayer();
        if (activePlayer) {
            $('#game-stat-active-player-wrapper').removeClass('hidden');
            $('#game-stat-active-player').text(activePlayer.name);
        }

        // init players info
        updatePlayerInfoSection();
        // init board zone
        updatePlayerZones();
        let availCards = $('.card-available');
        for (let i = 0; i < availCards.length; i++) {
            availCards[i].addEventListener("blur", function( event ) {
                 $(event.target).removeClass('available-card-clicked');
            }, true);
        }

        let playerCount = CLIENT.state.players.length;
        $('#game-stat-player-count').text(playerCount);
        if (isCreator(CLIENT.state.players) && !CLIENT.state.action) {
            $('#start-game-button').removeClass('hidden'); // TODO fix
        }

        if(CLIENT.state.finished) {
            CLIENT.disconnect();
            setTimeout(() => { alert('Выиграл ' + CLIENT.state.winner)}, 1000);
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

CLIENT.sendAction = function() {
    CLIENT.socket.send(JSON.stringify(CLIENT.state));
}

CLIENT.startGame = function() {
    CLIENT.state.action = {};
    CLIENT.state.action.name = CLIENT.COMMAND_START_GAME;
    CLIENT.sendAction();
}

function isCreator(players) {
    return players.some(el => el.name == CLIENT.name && el.creator);
}

function getCurrentPlayer(players) {
    return players.find((p) => p.name === CLIENT.name);
}

function getActivePlayer(players) {
    return CLIENT.state.players.find((p) => p.activeTurn);
}

function updatePlayerZones() {
    for (let i = 0; i < CLIENT.state.players.length; i++) {
        let zone = $('#main-board-zone-' + i);
        zone.empty();
        let playerForZone = CLIENT.state.players[i];
        zone.attr('data-player-id', playerForZone.name);
        if (CLIENT.name === playerForZone.name) {
            zone.addClass('board-player-zone--current-player');
        }
        zone.append(`<div>
              [<span>${playerForZone.name}</span>] |
                  <span style="color: red; font-weight: bold;"> КР:
                      З - <span title="Запас КР">${playerForZone.redAmount}</span>;
                      П - <span title="Производство КР">${playerForZone.redProduction}</span>;
                      Р - <span title="Расход КР">${playerForZone.redConsumption}</span>
                  </span>
                  ;
                  <span style="color: blue; font-weight: bold;"> СР:
                      З - <span title="Запас СР">${playerForZone.blueAmount}</span>;
                      П - <span title="Производство СР">${playerForZone.blueProduction}</span>;
                      Р - <span title="Расход СР">${playerForZone.blueConsumption}</span>
                  </span>
        </div>`);
        if (!playerForZone.alive) {
            // player finished game as can't produce enough red resource
            zone.append(`<div> Колония игрока <b>[${playerForZone.name}]</b> вымерла из-за нехватки ресурсов...</div>`);
            zone.addClass('board-player-zone--player-lose')
            continue;
        }
        for(let i = 0; i < playerForZone.activeCards.length; i++) {
            let currentActiveCard = playerForZone.activeCards[i];
            zone.append(`<div class="active-card-item" title="${currentActiveCard.description}" data-card-id="${currentActiveCard.idUI}">S</div>`)
        }
    }
}

function updatePlayerInfoSection() {
    let currentPlayer = getCurrentPlayer(CLIENT.state.players);
    $('#section-user-cards').empty();
    for (let i = 0; i < currentPlayer.availableCards.length; i++) {
        let card =  currentPlayer.availableCards[i];
        $('#section-user-cards').append(`<span title="${card.description}" class="card-available" data-card-id="${card.idUI}" onclick="chooseAvailableCard(this);">${card.title}</span>`)
    }
}