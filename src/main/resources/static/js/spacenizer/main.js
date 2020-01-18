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

        //$('#section-user-cards_player-name').text(CLIENT.name);
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
                console.log(jsonResponse.id);
                window.location.href = '/board?id=' + jsonResponse.id;
            }
        }
    });
}

$(document).click(function(e) {
    let $target = $(e.target);
    if (!$target.is(".available-card-clicked")) {
        $('.available-card-clicked').removeClass('available-card-clicked');
        $('.section-main-board').removeClass('available-card-clicked-js');
    }
});

function chooseAvailableCard(el) {
    let currentElement = $(el);
    $('.available-card-clicked').removeClass('available-card-clicked');
    currentElement.addClass('available-card-clicked');
    $('.section-main-board').addClass('available-card-clicked-js');
}