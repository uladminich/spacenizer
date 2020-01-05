let CLIENT = {};

CLIENT.initConnection = function () {
    console.log("initConnection start ");

    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');

    CLIENT.socket = new WebSocket("ws://localhost:8080/board/" + id + '/' + CLIENT.name.substring(1, CLIENT.name.length - 1));

    CLIENT.socket.onmessage = function(event) {
        CLIENT.state = JSON.parse(event.data);
        $('#chat-wrapper').append( '<span>' + CLIENT.name + ': ' +  CLIENT.state.lastMessage + '</span><br>');
    };

    CLIENT.socket.onopen = function(e) {
        console.log("onopen ");
    };

    CLIENT.socket.onclose = function(event) {
        if (!event.wasClean) {
            console.log('[close] Соединение прервано, код=${event.code} причина=${event.reason}`');
        }
    };

    console.log("initConnection end ");
}

CLIENT.sendMessage = function() {
    let value = $("#chat-new-message").val() ? $("#chat-new-message").val().trim() : '';
    CLIENT.state.lastMessage = value
    CLIENT.socket.send(JSON.stringify(CLIENT.state));
}

CLIENT.disconnect = function() {
    CLIENT.socket.send("Пользователь " + CLIENT.name + " отключился.");
    CLIENT.socket.close();
}


