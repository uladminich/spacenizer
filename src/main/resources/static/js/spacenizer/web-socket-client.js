let CLIENT = {};

CLIENT.COMMAND_START_GAME = 'start';
CLIENT.COMMAND_START_GAME_COMPLETED = 'start_completed';
CLIENT.COMMAND_PLAY_CARD = 'play_card';
CLIENT.COMMAND_CHANGE_CARD = 'change_card';
CLIENT.COMMAND_SKIP_TURN = 'skip_turn';
CLIENT.GAME_STAT_SECTION_ID = 'GLOBAL';

CLIENT.initConnection = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');

    CLIENT.socket = new WebSocket("ws://" + window.location.host + "/board/" + id + '/' + CLIENT.name);

    CLIENT.socket.onmessage = function(event) {
        CLIENT.state = JSON.parse(event.data);

        if (CLIENT.state.action && CLIENT.state.action.name === CLIENT.COMMAND_START_GAME_COMPLETED) {
            // init game stat
            postInitGameAction();
        }

        // init global stat info
        updateGlobalStatInfoSection();
        // init players info
        updatePlayerInfoSection();
        // init board zone
        updatePlayerZones();

        let playerCount = CLIENT.state.players.length;
        $('#game-stat-player-count').text(playerCount);
        if (isCreator(CLIENT.state.players) && !CLIENT.state.action) {
            $('#section-header__start-game-button').removeClass('d-none');
        }
        let currentPlayer = getCurrentPlayer(CLIENT.state.players);
        let skipTurnButton = $('#section-main__user-cards-skip-turn-button');
        if (currentPlayer.availableCards.length == 0 && CLIENT.state.action && !CLIENT.state.finished) {
            skipTurnButton.removeClass('d-none');
        } else if (!skipTurnButton.hasClass('d-none')) {
            skipTurnButton.addClass('d-none');
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

function postInitGameAction() {
    // init game stat
    $('#section-header__start-game-button').addClass('d-none');
    $('#game-stat-red-amount-wrapper').removeClass('hidden');
    $('#game-stat-blue-amount-wrapper').removeClass('hidden');
}

function getCurrentPlayer(players) {
    return players.find((p) => p.name === CLIENT.name);
}

function getActivePlayer(players) {
    return CLIENT.state.players.find((p) => p.activeTurn);
}

function updatePlayerZones() {
    if(!CLIENT.state.action) {
        return;
    }
    let playerAmount = CLIENT.state.players.length;
    let boardElem = $('#section-main__board');
    boardElem.empty();
    if (playerAmount <= 3 ) { // one row required
        boardElem.append('<div class="row row-single-line-js">');
        let rowElem = $(".row-single-line-js");
        for (let i = 0; i < playerAmount; i++) {
            let playerForZone = CLIENT.state.players[i];
            addPlayerZoneHeader(rowElem, playerForZone, i);
            let playerZone = $(".main-board-zone-" + i);

            // player finished game as can't produce enough red resource
            if (!playerForZone.alive) {
                addDeadPlayerZoneDescription(playerZone, playerForZone);
                continue;
            }
            playerZone.append(`<div class="row row-active-cards row-active-cards-${i}">`)
            let rowActiveCards = $('.main-board-zone-' + i + ' .row-active-cards-' + i);

            for(let j = 0; j < playerForZone.activeCards.length; j++) {
                let currentActiveCard = playerForZone.activeCards[j];
                addActiveCardToPlayerZone(rowActiveCards, currentActiveCard);
            }
        }
    } else if (playerAmount <= 6 && playerAmount > 3){
        let playerIndex = 0;
        for (let rowIndex = 0; rowIndex < 2; rowIndex++) {
            boardElem.append(`<div class="row row-multi-line row-multi-line-js-${rowIndex}">`);
            let rowElem = $(".row-multi-line-js-" + rowIndex);
            let rowLimit = getRowLimit(playerAmount, rowIndex);
            for (let i = 0; i < rowLimit; i++) {
                let playerForZone = CLIENT.state.players[playerIndex];
                addPlayerZoneHeader(rowElem, playerForZone, playerIndex);
                let playerZone = $(".main-board-zone-" + playerIndex);

                // player finished game as can't produce enough red resource
                if (!playerForZone.alive) {
                    addDeadPlayerZoneDescription(playerZone, playerForZone);
                    continue;
                }

                playerZone.append(`<div class="row row-active-cards row-active-cards-${playerIndex}">`)
                let rowActiveCards = $('.main-board-zone-' + playerIndex + ' .row-active-cards-' + playerIndex);
                for(let j = 0; j < playerForZone.activeCards.length; j++) {
                    let currentActiveCard = playerForZone.activeCards[j];
                    addActiveCardToPlayerZone(rowActiveCards, currentActiveCard);
                }
                playerIndex++;
            }
        }
    }
}

function getRowLimit(playerAmount, multiRowIndex) {
    if (playerAmount == 4) {
        return 2;
    } else if (playerAmount == 5 && multiRowIndex == 0) {
        return 3;
    } else if (playerAmount == 5 && multiRowIndex == 1) {
        return 2;
    }
    return 3;
}

function addPlayerZoneHeader(rowElem, playerForZone, i) {
    rowElem.append(`<div class="col-sm main-board-zone main-board-zone-${i} ${CLIENT.name === playerForZone.name ? 'board-player-zone--current-player' : '' }"
                        data-player-id="${playerForZone.name}">
                        <div class="row">
                            <div class="col-sm text-center">
                                <b>${playerForZone.name}</b> (${playerForZone.availableCards.length})
                                <img class="${playerForZone.changeCardAmount == 2 ? '' : 'd-none'}" src="/svg/change-card-img.svg" width="30px" height="30px" title="Player can change one card">
                                <img class="${playerForZone.changeCardAmount == 1 || playerForZone.changeCardAmount == 2 ? '' : 'd-none'}" src="/svg/change-card-img.svg" width="30px" height="30px" title="Player can change one card">
                            </div>
                            <div class="col-sm text-center">
                                <span class="card-global badge badge-danger">
                                    Red: ${playerForZone.redAmount} | ${playerForZone.redProduction > 0 ? '+' + playerForZone.redProduction : 0 } | ${playerForZone.redConsumption > 0 ? '-' + playerForZone.redConsumption : 0 }
                                </span>
                            </div>
                            <div class="col-sm text-center">
                                <span class="card-global badge badge-primary">
                                    Blue: ${playerForZone.blueAmount} | ${playerForZone.blueProduction > 0 ? '+' + playerForZone.blueProduction : 0 } | ${playerForZone.blueConsumption > 0 ? '-' + playerForZone.blueConsumption : 0 }
                                </span>
                           </div>
                        </div>
                    </div>`)
}

function addDeadPlayerZoneDescription(playerZone, playerForZone) {
    playerZone.append(`<div class="row">
                            <div class="col-sm">
                                <div> The colony of <b>[${playerForZone.name}]</b> died as couldn't reproduce enough resources... </div>
                            </div>
                        <div>`);
    playerZone.addClass('board-player-zone--player-lose');
}

function addActiveCardToPlayerZone(rowActiveCards, currentActiveCard) {
    rowActiveCards.append(`<div class="col-sm">
                                <div class="card text-center">
                                    <div class="card-body ${currentActiveCard.active ? '' : 'active-card-item--disabled'}"
                                        title="${currentActiveCard.description}"
                                        data-card-id="${currentActiveCard.idUI}">
                                        <h5 class="card-title" style="border-bottom: none;">${currentActiveCard.title}</h5>
                                        <!-- // TODO no data at the moment on UI
                                            <p class="card-text">
                                                <span class="badge badge-danger"> +2 | -2 </span>
                                                <span class="badge badge-primary" style=""> 0 | 0 </span>
                                            </p>
                                         -->
                                    </div>
                                </div>
                           </div>`);
}

function updatePlayerInfoSection() {
    if(!CLIENT.state.action) {
        return;
    }
    let currentPlayer = getCurrentPlayer(CLIENT.state.players);
    $('#section-main__user-cards-title').text(currentPlayer.name + '\'s ');
    $('#section-main__user-cards-list').empty();
    for (let i = 0; i < currentPlayer.availableCards.length; i++) { //no more that 6 card in the row
        let card =  currentPlayer.availableCards[i];
        $('#section-main__user-cards-list')
            .append(`<div class="col-sm" >
                         <div class="card text-center card-available" style="width: 8rem;" title=""
                             data-card-id="${card.idUI}" data-card-global="${card.global}"
                             onclick="chooseAvailableCard(this, event);";>
                             <div class="card-body">
                                 <h5 class="card-title">${card.title}</h5>
                                 <p class="card-text">
                                    ${card.global ? '<span id="section-header__game-globals-cards-default" class="badge badge-warning" title="Играется на зону WORLD." >GLOBAL</span><br>' : '' }
                                    ${card.id == 6 || card.id == 5 || card.id == 8 || card.id == 9 ? '<span id="section-header__game-globals-cards-default" class="badge badge badge-info" title="Повторяющиеся карты не оказывают эффекта.">ONE ACTIVE</span><br>' : '' }
                                    ${card.description}
                                 </p>
                             </div>
                         </div>
                     </div>`)
    }
}

function updateGlobalStatInfoSection() {
    let redAmount = CLIENT.state.redResourceCount;
    let blueAmount = CLIENT.state.blueResourceCount;
    let totalAmount = CLIENT.state.totalResourceCount;
    drawResourceChart(redAmount, blueAmount, totalAmount);
    let activePlayer = getActivePlayer();
    if (activePlayer) {
        $('#game-status-info__active-player-wrapper').removeClass('d-none');
        $('#game-status-info__active-player').text(activePlayer.name);
        $('#section-header__game-globals-wrapper').removeClass('d-none');
    }
    let globalCardListElem = $('#section-header__game-globals-cards-list');
    let activeGlobalCards = CLIENT.state.globalPlayer.activeCards;
    if (activeGlobalCards.length == 0) {
        $('#section-header__game-globals-cards-default').removeClass('d-none');
        return;
    }
    $('#section-header__game-globals-cards-default').addClass('d-none');
    globalCardListElem.empty();
    for (let i = 0; i < CLIENT.state.globalPlayer.activeCards.length; i++) {
        let card = CLIENT.state.globalPlayer.activeCards[i];
        globalCardListElem.append(`<span title="${card.description}" class="badge badge-warning"data-card-id="${card.idUI}">${card.title}</span>`)
    }
    $('#section-header__game-globals-cards-description').text(CLIENT.state.action.description);
}