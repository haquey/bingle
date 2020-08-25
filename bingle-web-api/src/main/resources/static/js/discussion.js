$(document).ready(function(){
    $("#nav-site #nav-discussion").addClass("active");

$('#disc-submitInfo').click(function(){
    var post_title = $('#title-input').val();
    var post_text = $('#thread-input').val();
    var post_tags = $('#tags-input').val();

    $.post("/threads/new", {
        'title' : post_title,
        'text' : post_text,
        'tags' : post_tags,
    }, function(data, status){
        if (data == null)
        {
            alert("Oops something went wrong! Please refresh");
            return;
        }
        var tags = "";

        data.tags.forEach(function(tag){
            tags += '<label>'+ tag +'</label>';
        })

        $('#threads').append(
           ' <div class="row thread" id='+ data.id +'>' + 
            '<div class="col-sm-1 thread-vote"> ' +
               ' <div class="vote-btn upvote">' +
                    '<i class="fa fa-angle-up" aria-hidden="true"></i>' +
                ' </div>' +
                '<label class="votes">' + data.voteCount + '</label>' +
                '<div class="vote-btn downvote">' +
                    '<i class="fa fa-angle-down" aria-hidden="true"></i>' +
                '</div>' +
            '</div>' +
            '<div class="col-sm-5 thread-title">' +
                 '<a href="discussion/posts/'+ data.id +'">' + data.title +'</a>' +

                '<label>' + data.name + ' at ' + data.createdAt + '</label>' +
            '</div>' +
            
            '<div class="col-sm-4 thread-tags">' +
                '<div class="row">'+
                    '<div class="col-sm-12" style="margin-top: 7px;">' +
                        '<i class="fa fa-tags" aria-hidden="true"></i>' +
                        '<label>Tags</label>' +
                    '</div>' +
                '</div>' +

                '<div class="row">' +
                    '<div class="col-sm-12 tag-holder">' +
                        tags +
                    '</div>' +
                '</div>' +
            '</div>' +

            '<div class="col-sm-2 thread-stats">' +
                '<div class="stat">' +
                    '<i class="fa fa-eye" aria-hidden="true"></i>' +
                    '<label>' + data.viewCount + '</label>' +
                '</div>' +
                '<div class="stat">' +
                    '<i class="fa fa-comments" aria-hidden="true"></i>' +
                    '<label>' + data.postCount + '</label>' +
                '</div>' +
            '</div>' +

        '</div>'

        
        );
        $('.upvote').off("click");

        $('.upvote').on("click", function(){
            if (sessionStorage.getItem("logged in") != "false"){
        
                if ($(this).css("color") != "rgb(255, 165, 0)" && $(this).parent().children(".downvote").css("color") != "rgb(0, 0, 255)")
                {     
                    var voteNum = $(this).parent().find(".votes");
                    $(this).css("color", "orange");
                    voteNum.html(parseInt(voteNum.html()) + 1);    
                    $.post("/threads/interact", {
                        'id': $(this).parent().parent()[0].id,
                        'voteCount': "1"
                    })
                }
                else if ($(this).parent().children(".downvote").css("color") == "rgb(0, 0, 255)")
                {
                    var voteNum = $(this).parent().find(".votes");
                    voteNum.html(parseInt(voteNum.html()) + 2);
                    $(this).parent().children().css("color", "");
                    $(this).css("color", "orange");
                    $.post("/threads/interact", {
                        'id':  $(this).parent().parent()[0].id,
                        'voteCount': "2"
                    })
                }
                else {
                    var voteNum = $(this).parent().find(".votes");
                    $(this).parent().children().css("color", "")
                    voteNum.html(parseInt(voteNum.html()) - 1);
                    $.post("/threads/interact", {
                        'id':  $(this).parent().parent()[0].id,
                        'voteCount': "-3"
                    })
                }
            }
            else {
                $("#loginModal").modal("toggle");
            }
        });
        
        $('.downvote').off("click");

        $('.downvote').on("click", function(){
            if (sessionStorage.getItem("logged in") != "false"){
                if ($(this).css("color") != "rgb(0, 0, 255)" && $(this).parent().children(".upvote").css("color") != "rgb(255, 165, 0)")
                {  
                    var voteNum = $(this).parent().find(".votes");
                    voteNum.html(parseInt(voteNum.html()) - 1);
                    $(this).css("color", "blue");
                    $.post("/threads/interact", {
                        'id':  $(this).parent().parent()[0].id,
                        'voteCount': "-1"
                    })
                }
                else if ($(this).parent().children(".upvote").css("color") == "rgb(255, 165, 0)")
                {
                    var voteNum = $(this).parent().find(".votes");
                    voteNum.html(parseInt(voteNum.html()) - 2);
                    $(this).parent().children().css("color", "");
                    $(this).css("color", "blue");
                    $.post("/threads/interact", {
                        'id':  $(this).parent().parent()[0].id,
                        'voteCount': "-2"
                    })
                    
                }
                else {
                    var voteNum = $(this).parent().find(".votes");
                    $(this).parent().children().css("color", "")
                    voteNum.html(parseInt(voteNum.html()) + 1);
                    $.post("/threads/interact", {
                        'id': $(this).parent().parent()[0].id,
                        'voteCount': "3"
                    })
                }
            }
            else {
                $("#loginModal").modal("toggle");
            }
        });
        $('#thread-input').val("");
        $('#title-input').val("");
    });
});


$('.upvote').on("click", function(){
    if (sessionStorage.getItem("logged in") != "false"){

        if ($(this).css("color") != "rgb(255, 165, 0)" && $(this).parent().children(".downvote").css("color") != "rgb(0, 0, 255)")
        {     
            var voteNum = $(this).parent().find(".votes");
            $(this).css("color", "orange");
            voteNum.html(parseInt(voteNum.html()) + 1);    
            $.post("/threads/interact", {
                'id': $(this).parent().parent()[0].id,
                'voteCount': "1"
            })
        }
        else if ($(this).parent().children(".downvote").css("color") == "rgb(0, 0, 255)")
        {
            var voteNum = $(this).parent().find(".votes");
            voteNum.html(parseInt(voteNum.html()) + 2);
            $(this).parent().children().css("color", "");
            $(this).css("color", "orange");
            $.post("/threads/interact", {
                'id': $(this).parent().parent()[0].id,
                'voteCount': "2"
            })
        }
        else {
            var voteNum = $(this).parent().find(".votes");
            $(this).parent().children().css("color", "")
            voteNum.html(parseInt(voteNum.html()) - 1);
            $.post("/threads/interact", {
                'id':  $(this).parent().parent()[0].id,
                'voteCount': "-3"
            })
        }
    }
    else {
        $("#loginModal").modal("toggle");
    }
});

$('.downvote').on("click", function(){
    if (sessionStorage.getItem("logged in") != "false"){
        if ($(this).css("color") != "rgb(0, 0, 255)" && $(this).parent().children(".upvote").css("color") != "rgb(255, 165, 0)")
        {  
            var voteNum = $(this).parent().find(".votes");
            voteNum.html(parseInt(voteNum.html()) - 1);
            $(this).css("color", "blue");
            $.post("/threads/interact", {
                'id': $(this).parent().parent()[0].id,
                'voteCount': "-1"
            })
        }
        else if ($(this).parent().children(".upvote").css("color") == "rgb(255, 165, 0)")
        {
            var voteNum = $(this).parent().find(".votes");
            voteNum.html(parseInt(voteNum.html()) - 2);
            $(this).parent().children().css("color", "");
            $(this).css("color", "blue");
            $.post("/threads/interact", {
                'id':  $(this).parent().parent()[0].id,
                'voteCount': "-2"
            })
            
        }
        else {
            var voteNum = $(this).parent().find(".votes");
            $(this).parent().children().css("color", "")
            voteNum.html(parseInt(voteNum.html()) + 1);
            $.post("/threads/interact", {
                'id':  $(this).parent().parent()[0].id,
                'voteCount': "3"
            })
        }
    }
    else {
        $("#loginModal").modal("toggle");
    }
});

$('#search-btn').on("click", function(){

    var query = $('.search')[0].value;

    $.get("/threads/search", {
        "query" : query
    }, function(data, status){
        
        $(".thread").each(function(index){
            $(this).remove();
        })

        data.forEach(function(data){

            var tags = "";

            data.tags.forEach(function(tag){
                tags += '<label>'+ tag +'</label>';
            })


            $('#threads').append(
                ' <div class="row thread" id='+ data.id +'>' + 
                '<div class="col-sm-1 thread-vote"> ' +
                   ' <div class="vote-btn upvote">' +
                        '<i class="fa fa-angle-up" aria-hidden="true"></i>' +
                    ' </div>' +
                    '<label class="votes">' + data.voteCount + '</label>' +
                    '<div class="vote-btn downvote">' +
                        '<i class="fa fa-angle-down" aria-hidden="true"></i>' +
                    '</div>' +
                '</div>' +
                '<div class="col-sm-5 thread-title">' +
                     '<a href="discussion/posts/'+ data.id +'">' + data.title +'</a>' +
    
                    '<label>' + data.name + ' at ' + data.createdAt + '</label>' +
                '</div>' +
                
                '<div class="col-sm-4 thread-tags">' +
                    '<div class="row">'+
                        '<div class="col-sm-12" style="margin-top: 7px;">' +
                            '<i class="fa fa-tags" aria-hidden="true"></i>' +
                            '<label>Tags</label>' +
                        '</div>' +
                    '</div>' +
    
                    '<div class="row">' +
                        '<div class="col-sm-12 tag-holder">' +
                            tags +
                        '</div>' +
                    '</div>' +
                '</div>' +
    
                '<div class="col-sm-2 thread-stats">' +
                    '<div class="stat">' +
                        '<i class="fa fa-eye" aria-hidden="true"></i>' +
                        '<label>' + data.viewCount + '</label>' +
                    '</div>' +
                    '<div class="stat">' +
                        '<i class="fa fa-comments" aria-hidden="true"></i>' +
                        '<label>' + data.postCount + '</label>' +
                    '</div>' +
                '</div>' +
    
            '</div>'
     
             
            );
            $('.upvote').off("click");

            $('.upvote').on("click", function(){
                if (sessionStorage.getItem("logged in") != "false"){
            
                    if ($(this).css("color") != "rgb(255, 165, 0)" && $(this).parent().children(".downvote").css("color") != "rgb(0, 0, 255)")
                    {     
                        var voteNum = $(this).parent().find(".votes");
                        $(this).css("color", "orange");
                        voteNum.html(parseInt(voteNum.html()) + 1);    
                        $.post("/threads/interact", {
                            'id': $(this).parent().parent()[0].id,
                            'voteCount': "1"
                        })
                    }
                    else if ($(this).parent().children(".downvote").css("color") == "rgb(0, 0, 255)")
                    {
                        var voteNum = $(this).parent().find(".votes");
                        voteNum.html(parseInt(voteNum.html()) + 2);
                        $(this).parent().children().css("color", "");
                        $(this).css("color", "orange");
                        $.post("/threads/interact", {
                            'id':  $(this).parent().parent()[0].id,
                            'voteCount': "2"
                        })
                    }
                    else {
                        var voteNum = $(this).parent().find(".votes");
                        $(this).parent().children().css("color", "")
                        voteNum.html(parseInt(voteNum.html()) - 1);
                        $.post("/threads/interact", {
                            'id':  $(this).parent().parent()[0].id,
                            'voteCount': "-3"
                        })
                    }
                }
                else {
                    $("#loginModal").modal("toggle");
                }
            });
            
            $('.downvote').off("click");

            $('.downvote').on("click", function(){
                if (sessionStorage.getItem("logged in") != "false"){
                    if ($(this).css("color") != "rgb(0, 0, 255)" && $(this).parent().children(".upvote").css("color") != "rgb(255, 165, 0)")
                    {  
                        var voteNum = $(this).parent().find(".votes");
                        voteNum.html(parseInt(voteNum.html()) - 1);
                        $(this).css("color", "blue");
                        $.post("/threads/interact", {
                            'id':  $(this).parent().parent()[0].id,
                            'voteCount': "-1"
                        })
                    }
                    else if ($(this).parent().children(".upvote").css("color") == "rgb(255, 165, 0)")
                    {
                        var voteNum = $(this).parent().find(".votes");
                        voteNum.html(parseInt(voteNum.html()) - 2);
                        $(this).parent().children().css("color", "");
                        $(this).css("color", "blue");
                        $.post("/threads/interact", {
                            'id':  $(this).parent().parent()[0].id,
                            'voteCount': "-2"
                        })
                        
                    }
                    else {
                        var voteNum = $(this).parent().find(".votes");
                        $(this).parent().children().css("color", "")
                        voteNum.html(parseInt(voteNum.html()) + 1);
                        $.post("/threads/interact", {
                            'id': $(this).parent().parent()[0].id,
                            'voteCount': "3"
                        })
                    }
                }
                else {
                    $("#loginModal").modal("toggle");
                }
            });
        });
    })

});

});
