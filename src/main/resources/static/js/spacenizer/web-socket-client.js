let CLIENT = {};

CLIENT.initConnection = function () {
    console.log("initConnection start ");

    CLIENT.socket = new WebSocket("ws://localhost:8080/chat");

    CLIENT.socket.onmessage = function(event) {
      $('#chat-wrapper').append( '<span>' + event.data + '</span><br>');
    };

    CLIENT.socket.onopen = function(e) {
    console.log("onopen ");
      CLIENT.socket.send("Пользователь " + CLIENT.name + " подключился.");
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
    CLIENT.socket.send(CLIENT.name + ": " + value);
}

CLIENT.disconnect = function() {
    CLIENT.socket.send("Пользователь " + CLIENT.name + " отключился.");
    CLIENT.socket.close();
}


