$(document).ready(function(){
    $.get("/complaint/",{}).done(function(data){
        var json_data = data;
        console.log("aravind : "+json_data);
        show_posts(json_data);
    });
    
});


function show_posts(json_data){
    $(".container").append("<h1 style='text-align:center'>"+"Complaints"+"</h1>")
    for (var i=0; i<json_data.length;i++){
        $(".container").append("<div class='w3-card-4' style='width:100%;background-image: url(https://pixabay.com/static/uploads/photo/2015/03/23/15/59/the-background-686237_960_720.jpg)'>"+"<header class='w3-container w3-light-grey' style='text-align:center'>"+"Location :"+json_data[i]["location"]+"</header>"+"Complaint No : "+json_data[i]["id"]+"<br>"+"User Name : "+"<i style='color:firebrick'>"+json_data[i]["user"]+"</i>"+"<img src='"+json_data[i]["image"]+"' class='w3-right w3-circle w3-margin-right' style='width:100px;height:90px'>"+"<br>"+"CATEGORY : "+json_data[i]["category"]+"<br>"+"DESCRIPTION - "+"<i class='prop'>"+json_data[i]["description"]+"</i>"+"</div>"+"<button class='w3-btn w3-round-large w3-light-blue' style='width:10%'>Assign To</button>"+"<hr>")
    }
}


//$(".container").append("<div><p>"+json_data[i]["user"]+"</p><span class='center'>"+json_data[i]["title"]+"</span></div>")
        //$(".container").append("<div class='container1'>"+json_data[i]["content"]+"</div><br>")


