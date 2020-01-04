function joinToChat() {
    let value = $('#chat-name').val() ? $('#chat-name').val().trim() : '';
    if (value) {
        if (!CLIENT) {
            return;
        };
        CLIENT.name = "[" + value + "]";
        $('#chat-wrapper').removeClass('hidden');
        $('#chat-editor').removeClass('hidden');
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
                console.log(jsonResponse.id);
                window.location.href = '/alpha?id=' + jsonResponse.id;
            }
        }
    });
}