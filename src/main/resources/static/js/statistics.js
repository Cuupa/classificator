/**
 * @param {string} type
 * @param {string} position
 * @param {string} datafield
 * @param {string} elementId
 * @param {string} label
 */
function getDiagram(type, position, datafield, elementId, label) {
    const data = JSON.parse(document.getElementById(datafield).value);
    const ctx = document.getElementById(elementId).getContext("2d")

    new Chart(ctx, {
        type: type,
        data: {
            labels: Object.keys(data),
            datasets: [{
                label: label,
                data: Object.values(data),
                backgroundColor: getBackgroundColors(),
                borderColor: getBorderColor(),
                borderWidth: 1
            }]
        },
        options: {
            legend: {
                position: position
            }
        }
    });
}

function getBackgroundColors() {
    return [
        'rgba(255, 99, 132, 0.2)',
        'rgba(54, 162, 235, 0.2)',
        'rgba(255, 206, 86, 0.2)',
        'rgba(75, 192, 192, 0.2)',
        'rgba(153, 102, 255, 0.2)',
        'rgba(255, 159, 64, 0.2)'
    ];
}

function getBorderColor() {
    return [
        'rgba(255, 99, 132, 1)',
        'rgba(54, 162, 235, 1)',
        'rgba(255, 206, 86, 1)',
        'rgba(75, 192, 192, 1)',
        'rgba(153, 102, 255, 1)',
        'rgba(255, 159, 64, 1)'
    ];
}