
function clear(type) {
    console.log(type)
    if (type != null) {
        let index = getIndex(type)
        console.log(index)
        $('#semantic_result')
            .find("tr")
            .not(":last")
            .find("td:eq(" + index + ")")
            .get(index).innerHTML = "<td style=\"width: 20%;\"></td>"
    }
}

function insertRow(table) {
    let row_number = table.rows.length - 1
    let row = table.insertRow(row_number);
    let cells = [row.insertCell(), row.insertCell(), row.insertCell()]
    return cells;
}

function getSpanForSemanticResult(value) {
    return "<span text=\"" + value + "\" class=\"badge bg-secondary\">" + value + "</span>"
}

function add(value, type) {
    if (value != null) {
        let table = document.getElementById("semantic_result");
        let index = getIndex(type)
        let hasSpace = checkRow(table, type)
        if (!hasSpace) {
            let cells = insertRow(table);
            cells[index].innerHTML = getSpanForSemanticResult(value)
        } else {
            let row = $("#semantic_result td:eq(" + index + ")")
            row.append(getSpanForSemanticResult(value))
        }

        let target = document.getElementById("payload-" + type)
        target.value = target.value + ";" + value
    }
}

function checkRow(element, type) {
    let columns = element.getElementsByTagName("td")
    return columns[getIndex(type)].innerText === ""
}

function getIndex(type) {
    switch (type) {
        case "topic":
            return 0
        case "sender":
            return 1
        case "metadata":
            return 2
        default:
            return -1
    }
}
