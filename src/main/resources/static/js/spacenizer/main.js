function joinToChat() {
    let value = $('#chat-name').val() ? $('#chat-name').val().trim() : '';
    if (value) {
        if (!CLIENT) {
            return;
        };
        CLIENT.name = value;
        $('#join-wrapper').addClass('hidden');
        if (!CLIENT.socket) {
            CLIENT.initConnection();
        }
    }
}

function createBoard() {
    let login = $('#board-form-login').val() ? $('#board-form-login').val().trim() : '';
    let password = $('#board-name-password').val() ? $('#board-name-password').val().trim() : '';

    let data = {
        'login' : login,
        'pass' : password
    };
    $.ajax({
        type: "POST",
        url: '/api/v1/spacenizer/board',
        data: data,
        success: function(response) {
            if (response) {
                let jsonResponse = JSON.parse(response);
                window.location.href = '/board?id=' + jsonResponse.id;
            }
        }
    });
}

$(document).click(function(e) {
    let $target = $(e.target);
    if(!isActiveTurn()) {
        return;
    }
    if (!$target.is(".available-card-clicked")) {

        if ($target.is(".main-board-player-zone") && !$target.is('.board-player-zone--player-lose')) {
            let currentPlayer = getCurrentPlayer(CLIENT.state.players);
            let targetPlayerId = $target.attr('data-player-id');

            let action = {};
            action.name = CLIENT.COMMAND_PLAY_CARD;
            action.fromPlayer = currentPlayer.name;
            action.toPlayer = targetPlayerId;
            action.fromCard = $('.available-card-clicked').attr('data-card-id');
            action.toCard = '';
            CLIENT.state.action = action;
            CLIENT.sendAction();

        }

        $('.available-card-clicked').removeClass('available-card-clicked');
        $('.section-main-board').removeClass('available-card-clicked-js');
    }
});

function chooseAvailableCard(el) {
    if(!isActiveTurn()) {
        return;
    }
    let currentElement = $(el);
    $('.available-card-clicked').removeClass('available-card-clicked');
    currentElement.addClass('available-card-clicked');
    $('.section-main-board').addClass('available-card-clicked-js');
}

function isActiveTurn() {
    if(CLIENT && CLIENT.state && CLIENT.state.players) {
        let currentPlayer = getCurrentPlayer(CLIENT.state.players);
        return currentPlayer.activeTurn;
    }
    return false;
}