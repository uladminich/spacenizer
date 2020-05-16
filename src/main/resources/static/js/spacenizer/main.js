let availableCardClicked = false;

function joinToChat() {
    let value = $('#chat-name').val() ? $('#chat-name').val().trim() : '';
    if (value) {
        if (!CLIENT) {
            return;
        };
        CLIENT.name = value;
        $('#section-header__join-wrapper').addClass('d-none');
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
                if(jsonResponse.id) {
                    window.location.href = '/board?id=' + jsonResponse.id;
                }
            }
        }
    });
}

$(document).click(function(e) {
    let $target = $(e.target);
    if(!isActiveTurn()) {
        return;
    }
    if (!$target.is(".card-available--clicked-js")) {
        if (($target.is(".main-board-zone") || $target.parents(".section-main__board--available-card-clicked-js .main-board-zone").length > 0) && !$target.is('.board-player-zone--player-lose') && $('.card-available--clicked-js').attr('data-card-id')) {
            let currentPlayer = getCurrentPlayer(CLIENT.state.players);
            let targetPlayerId = $target.attr('data-player-id') || $target.parents(".section-main__board--available-card-clicked-js .main-board-zone").attr('data-player-id');
            let action = {};
            action.name = CLIENT.COMMAND_PLAY_CARD;
            action.fromPlayer = currentPlayer.name;
            action.toPlayer = targetPlayerId;
            action.fromCard = $('.card-available--clicked-js').attr('data-card-id');
            action.toCard = '';
            CLIENT.state.action = action;
            CLIENT.sendAction();
        }

        if (!availableCardClicked) {
            $('#section-main__board').removeClass('section-main__board--available-card-clicked-js');
            $('.card-available--clicked-js').removeClass('card-available--clicked-js');
            $('#section-main__user-cards-change-card-button').addClass('d-none');
        } else {
            availableCardClicked = false;
        }
    }
});

function chooseAvailableCard(el, event) {
    event.preventDefault();
    if(!isActiveTurn()) {
        return;
    }
    let currentElement = $(el);
    $('.card-available--clicked-js').removeClass('card-available--clicked-js');
    $('#section-main__board').addClass('section-main__board--available-card-clicked-js');
    currentElement.addClass('card-available--clicked-js');
    availableCardClicked = true;
    let currentPlayer = getCurrentPlayer(CLIENT.state.players);
    if (currentPlayer.changeCardAmount > 0) {
        $('#section-main__user-cards-change-card-button').removeClass('d-none');
    }
    return true;
}

function isActiveTurn() {
    if(CLIENT && CLIENT.state && CLIENT.state.players) {
        let currentPlayer = getCurrentPlayer(CLIENT.state.players);
        return currentPlayer.activeTurn;
    }
    return false;
}

function changeAvailableCard() {
    if(!isActiveTurn()) {
        return;
    }
    event.preventDefault();
    let currentPlayer = getCurrentPlayer(CLIENT.state.players);
    let action = {};
    action.name = CLIENT.COMMAND_CHANGE_CARD;
    action.fromPlayer = currentPlayer.name;
    action.toPlayer = '';
    action.fromCard = $('.card-available--clicked-js').attr('data-card-id');
    action.toCard = '';
    CLIENT.state.action = action;
    CLIENT.sendAction();
}

function skipTurn() {
    if(!isActiveTurn()) {
        return;
    }
    event.preventDefault();
    let currentPlayer = getCurrentPlayer(CLIENT.state.players);
    let action = {};
    action.name = CLIENT.COMMAND_SKIP_TURN;
    action.fromPlayer = currentPlayer.name;
    action.toPlayer = '';
    action.fromCard = '';
    action.toCard = '';
    CLIENT.state.action = action;
    CLIENT.sendAction();
}