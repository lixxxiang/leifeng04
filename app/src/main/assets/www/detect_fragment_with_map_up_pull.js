function showAddressInfo(content){
    $("#addressInfo").text(content);
}



function Info(){
    var val=$('#a').val();
    console.log(val);
    cordova.exec(success, fail, "CallBack", "search", [val]);
            var success = function () {
            };
            var fail = function () {
            };
}