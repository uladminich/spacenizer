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