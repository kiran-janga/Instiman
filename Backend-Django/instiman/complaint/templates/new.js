$(document).ready(function(){
    $.get("/complaint/",{}).done(function(data){
        var json_data = data;
        console.log("aravind : "+json_data);
        show_posts(json_data);
    });
    
});


function show_posts(json_data){
    for (var i=0; i<json_data.length;i++){
        $(".container").append("<div>"+"<b>"+"USER : "+"</b>"+json_data[i]["user"]+"<br>"+"<br>"+"<b'>"+"ID : "+"</b>"+json_data[i]["id"]+"<br>"+"<b>"+"IMAGE : "+"</b>"+json_data[i]["image"]+"<br>"+"<b>"+"CATEGORY : "+"</b>"+json_data[i]["category"]+"<br>"+"<b>"+"LOCATION : "+"</b>"+json_data[i]["location"]+"<br>"+"<h4>"+"DESCRIPTION : "+"</h4>"+json_data[i]["description"]+"</div><br>")
    }
}


//$(".container").append("<div><p>"+json_data[i]["user"]+"</p><span class='center'>"+json_data[i]["title"]+"</span></div>")
        //$(".container").append("<div class='container1'>"+json_data[i]["content"]+"</div><br>")


