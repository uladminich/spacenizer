function submit() {
    let data = {};
    $('input[type="text"]').each(function() {
        let el = $(this);
        data[el.attr('name')] = el.val() ? el.val().trim() : '';
    });

    $.ajax({
        type: "POST",
        url: '/api/sudoku/solution/get',
        data: data,
        success: function(response) {
            if (response && response.map) {
                let sudokuTableAnswer = response.map;
                for (let i = 0; i < 9; i++) {
                    for (let j = 0; j < 9; j++) {
                        let inputElem = $(`input[name="${i}_${j}"]`);
                        if (!inputElem.val()) {
                            inputElem.val(sudokuTableAnswer[i][j]);
                            inputElem.addClass("sudoku-solved-item");
                        }
                    }
                }
            }
        }
    });
}

function clearInputs() {
    $('input[type="text"]').each(function() {
        let el = $(this);
        el.val("");
        el.removeClass('sudoku-solved-item');
    });
}