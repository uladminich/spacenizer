let myChart;
let ctx = document.getElementById('myChart').getContext('2d');

function drawResourceChart(redResource, blueResource, totalResourceAmount) {
    // draw chart only if game started
    if(totalResourceAmount <= 0) {
        return;
    }
    let mined = totalResourceAmount - redResource - blueResource;
    let redLabel = `Red res: ${redResource}`;
    let blueLabel = `Blue res: ${blueResource}`;
    let minedLabel = `Mined: ${mined}`;
    if (myChart) { //TODO rewrite with update
        myChart.destroy();
    }

    myChart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: [redLabel, blueLabel, minedLabel],
            datasets: [
                {
                    data: [redResource,blueResource, mined],
                    backgroundColor: [
                        'red',
                        'blue',
                        'grey'
                    ],
                    borderColor: [
                        'black',
                        'black',
                        'black'
                    ],
                    borderWidth: 1
                }
            ]
        },
        options: {
            legend: {
                display: true,
                position: 'right',
                labels: {
                    fontSize: 16
                }
            },
            animation: {
                duration: 0
            }
        }
    });
}
