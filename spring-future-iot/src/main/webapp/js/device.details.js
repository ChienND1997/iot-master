function selectDevice() {
    var selected = document.getElementById('type_code').value;
    var path = "/device/" + selected;
    console.log(path);
    window.location.href = path;
}

var stompClient = null;
function disconnect(){
    if(stompClient != null){
        stompClient.disconnect();
    }
    console.log("Disconnected stomp client...");
}
function connectCbnd(empId, username) {
    var headerName = "${_csrf.headerName}";
    var token = "${_csrf.token}";
    var headers = {};
    headers[headerName] = token;
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/cbnd/'+ empId + username, function (sensor) {
            var sensor = JSON.parse(sensor.body);
            var mac = sensor.macAddress;
            document.getElementById('cbnd-value').innerHTML = sensor.temperatureValue + "°C" + " - " + sensor.humidityValue + "%";
            document.getElementById('cbnd-value-table-' + mac).innerHTML = sensor.temperatureValue + "°C" + " - " + sensor.humidityValue + "%";
            document.getElementById('cbnd-update-table-' + mac).innerHTML = sensor.updateTime;
        });
    });
}

function deleteDevice(path) {
    if (confirm("Hành động này không thể hoàn tác, bạn có muốn xóa !")) {
        window.location.href = path;
    }
}
function getTemperatureById(macAddress) {
    $.ajax({
        type: 'GET',
        url: '/api/cbnd/get-'+ macAddress,
        data: { get_param: 'value' },
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function (sensor) {
            document.getElementById('cbnd-value').innerHTML = sensor.temperatureValue + "°C" + " - " + sensor.humidityValue + "%";
            document.getElementById('cbnd-mac').innerHTML = "Mac: " +  sensor.macAddress;
            document.getElementById('cbnd-create-date').innerHTML = "Ngày Tạo: " + sensor.createDate;
            document.getElementById('cbnd-des').innerHTML = "Mô Tả: " + sensor.description;
        }
    });


}