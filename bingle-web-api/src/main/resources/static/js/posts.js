$(document).ready(function(){
    localStorage.removeItem("writing");
    var pID = "-999";
    $('#submitReply').click(function(){
        if (sessionStorage.getItem("logged in") == "false")
        {
            $("#loginModal").modal("toggle");
            return;
        }
        var content = $('#reply-input').val();
        var threadid = $('.thread')[0].id;
        var offset = $("#"+pID+"_container").css("margin-left");
        console.log(offset);
        console.log(pID);
        if (content == "")
        {
            return;
        }
        $.post("/posts/" + threadid +"/new", 
        {
            'content' : content,
            'parent': pID

        }, function(data, status){
            if (data == null)
            {
                alert("Oops something went wrong! Please refresh");
                return;
            }
            
                var html_string = '<div class="content container" style="margin-left : '+ (Number(offset.replace("px", ""))+40).toString() +'px; " id="'+ data.id +'_container">' + 
                    '<div class="row">' +

                    '<div class="col-sm-6 post" id="' + data.id + '">' +
                        '<div class="row thread">' + 

                        '<div class="col-sm-1 post-vote">' +
                            '<div class="vote-btn post-upvote">' +
                                '<i class="fa fa-angle-up" aria-hidden="true"></i>' +
                            '</div>' +
                            '<label class="votes">' + data.voteCount + '</label>' +
                            '<div class="vote-btn post-downvote">' +
                                '<i class="fa fa-angle-down" aria-hidden="true"></i>' +
                            '</div>' +
                        '</div><div class="col-sm-2 post-user">';

                        if (data.avatar) {
                            html_string += `<img src="` + data.avatar + `" style="height:60px"/>`;
                        } else {
                            html_string +=  `<i class="fa fa-user-circle" aria-hidden="true"></i>`;
                        }

                        html_string += `<label class="post-username">` + data.username + `</label>`;
                        
                        if (data.admin) {
                            html_string += `<label class="post-bp">Instructor</label>`;
                        } else {
                            html_string += `<label class="post-bp">Student</label>`;
                        }

                        html_string += `</div>` +
                        
                        '<div class="col-sm-7 post-content">' +
                            '<p>' + data.content + '</p>' +
                            '<label>By '+ data.name +' at ' + data.createdAt + '</label>' +
                        '</div>' +

                        '<div class="col-sm-2 text-center">' +
                            '<button class="btn btn-dark btn-sm btn-reply" data-toggle="modal" data-target="#reply-post-modal">' +
                               'Reply' +
                            '</button>' +
                        '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>'+
                '</div>';

                $('#'+pID+"_container").after(html_string);

                MathJax.Hub.Config(
                    {"HTML-CSS": { preferredFont: "TeX", availableFonts: ["STIX","TeX"], linebreaks: { automatic: true }, EqnChunk: (MathJax.Hub.Browser.isMobile ? 10 : 50) },
                     tex2jax: { inlineMath: [ ["$", "$"], ["\\\\(","\\\\)"] ], displayMath: [ ["$$","$$"], ["\\[", "\\]"] ], processEscapes: true, ignoreClass: "tex2jax_ignore|dno" },
                     TeX: {  noUndefined: { attributes: { mathcolor: "red", mathbackground: "#FFEEEE", mathsize: "90%" } }, Macros: { href: "{}" } },
                     messageStyle: "none", skipStartupTypeset: true });
                     MathJax.Hub.queue.pending = 0;
                MathJax.Hub.Queue(["Typeset", MathJax.Hub]); 

            $('.btn-reply').click(function(){
                pID = $(this).parent().parent().parent()[0].id;
        
                //console.log(pID);
            });


            $('.post-upvote').off("click");
            $('.post-upvote').on("click", function(){
                if (sessionStorage.getItem("logged in") != "false"){
        
                    if ($(this).css("color") != "rgb(255, 165, 0)" && $(this).parent().children(".post-downvote").css("color") != "rgb(0, 0, 255)")
                    {     
                        var voteNum = $(this).parent().find(".votes");
                        $(this).css("color", "orange");
                        voteNum.html(parseInt(voteNum.html()) + 1);    
                        $.post("/posts/interact", {
                            'id': $(this).parent().parent().parent()[0].id,
                            'voteCount': "1"
                        })
                    }
                    else if ($(this).parent().children(".post-downvote").css("color") == "rgb(0, 0, 255)")
                    {
                        var voteNum = $(this).parent().find(".votes");
                        voteNum.html(parseInt(voteNum.html()) + 2);
                        $(this).parent().children().css("color", "");
                        $(this).css("color", "orange");
                        $.post("/posts/interact", {
                            'id':  $(this).parent().parent().parent()[0].id,
                            'voteCount': "2"
                        })
                    }
                    else {
                        var voteNum = $(this).parent().find(".votes");
                        $(this).parent().children().css("color", "")
                        voteNum.html(parseInt(voteNum.html()) - 1);
                        $.post("/posts/interact", {
                            'id':  $(this).parent().parent().parent()[0].id,
                            'voteCount': "-3"
                        })
                    }
                }
                else {
                    $("#loginModal").modal("toggle");
                }
            });
            $('.post-downvote').off("click");

            $('.post-downvote').on("click", function(){
                if (sessionStorage.getItem("logged in") != "false"){
                    if ($(this).css("color") != "rgb(0, 0, 255)" && $(this).parent().children(".post-upvote").css("color") != "rgb(255, 165, 0)")
                    {  
                        var voteNum = $(this).parent().find(".votes");
                        voteNum.html(parseInt(voteNum.html()) - 1);
                        $(this).css("color", "blue");
                        $.post("/posts/interact", {
                            'id':  $(this).parent().parent().parent()[0].id,
                            'voteCount': "-1"
                        })
                    }
                    else if ($(this).parent().children(".post-upvote").css("color") == "rgb(255, 165, 0)")
                    {
                        var voteNum = $(this).parent().find(".votes");
                        voteNum.html(parseInt(voteNum.html()) - 2);
                        $(this).parent().children().css("color", "");
                        $(this).css("color", "blue");
                        $.post("/posts/interact", {
                            'id':  $(this).parent().parent().parent()[0].id,
                            'voteCount': "-2"
                        })
                        
                    }
                    else {
                        var voteNum = $(this).parent().find(".votes");
                        $(this).parent().children().css("color", "")
                        voteNum.html(parseInt(voteNum.html()) + 1);
                        $.post("/posts/interact", {
                            'id':  $(this).parent().parent().parent()[0].id,
                            'voteCount': "3"
                        })
                    }
                }
                else {
                    $("#loginModal").modal("toggle");
                }
            });
            $('#reply-input').val("");
            localStorage.removeItem("writing");

        });

        //console.log(pID);
    })

    $('.btn-reply').click(function(){
        pID = $(this).parent().parent().parent()[0].id;

        //console.log(pID );
    })

    $('#btn-new-post').click(function(){
        if (sessionStorage.getItem("logged in") == "false")
        {
            $("#loginModal").modal("toggle");
            return;
        }

        var content = $('#wmd-input').val();
        var threadid = $('.thread')[0].id;

        $.post("/posts/" + threadid +"/new", 
        {
            'content' : content,
            'parent': "-1"

        }, function(data, status){
            var html_string = '<div class="content container" style="margin-left : 40px; " id="'+ data.id +'_container">' + 
                    '<div class="row">' +

                    '<div class="col-sm-6 post" id="' + data.id + '">' +
                        
                        '<div class="row thread">' + 
                        '<div class="col-sm-1 post-vote">' +
                            '<div class="vote-btn post-upvote">' +
                                '<i class="fa fa-angle-up" aria-hidden="true"></i>' +
                            '</div>' +
                            '<label class="votes">' + data.voteCount + '</label>' +
                            '<div class="vote-btn post-downvote">' +
                                '<i class="fa fa-angle-down" aria-hidden="true"></i>' +
                            '</div>' +
                        '</div><div class="col-sm-2 post-user">';

                        if (data.avatar) {
                            html_string += `<img src="` + data.avatar + `" style="height:60px"/>`;
                        } else {
                            html_string +=  `<i class="fa fa-user-circle" aria-hidden="true"></i>`;
                        }

                        html_string += `<label class="post-username">` + data.username + `</label>`;
                        
                        if (data.admin) {
                            html_string += `<label class="post-bp">Instructor</label>`;
                        } else {
                            html_string += `<label class="post-bp">Student</label>`;
                        }

                        html_string += `</div>` +

                        

                        '<div class="col-sm-7 post-content">' +
                            '<p>' + data.content + '</p>' +
                            '<label>By '+ data.name +' at ' + data.createdAt + '</label>' +
                        '</div>' +

                        '<div class="col-sm-2 text-center">' +
                            '<button class="btn btn-dark btn-sm btn-reply" data-toggle="modal" data-target="#reply-post-modal">' +
                               'Reply' +
                            '</button>' +
                        '</div>' +
                    '</div>' +
                    '</div>' +
                    '</div>'+

                '</div>';
                $('.posts').append(html_string);

                MathJax.Hub.Config(
                    {"HTML-CSS": { preferredFont: "TeX", availableFonts: ["STIX","TeX"], linebreaks: { automatic: true }, EqnChunk: (MathJax.Hub.Browser.isMobile ? 10 : 50) },
                     tex2jax: { inlineMath: [ ["$", "$"], ["\\\\(","\\\\)"] ], displayMath: [ ["$$","$$"], ["\\[", "\\]"] ], processEscapes: true, ignoreClass: "tex2jax_ignore|dno" },
                     TeX: {  noUndefined: { attributes: { mathcolor: "red", mathbackground: "#FFEEEE", mathsize: "90%" } }, Macros: { href: "{}" } },
                     messageStyle: "none", skipStartupTypeset: true });
                     MathJax.Hub.queue.pending = 0;
                     MathJax.Hub.Queue(["Typeset", MathJax.Hub]); 

            $('.btn-reply').click(function(){
                pID = $(this).parent().parent().parent()[0].id;
        
                //console.log(pID);
            })
            $('.post-upvote').off("click");
            $('.post-upvote').on("click", function(){
                if (sessionStorage.getItem("logged in") != "false"){
        
                    if ($(this).css("color") != "rgb(255, 165, 0)" && $(this).parent().children(".post-downvote").css("color") != "rgb(0, 0, 255)")
                    {     
                        var voteNum = $(this).parent().find(".votes");
                        $(this).css("color", "orange");
                        voteNum.html(parseInt(voteNum.html()) + 1);    
                        $.post("/posts/interact", {
                            'id': $(this).parent().parent().parent()[0].id,
                            'voteCount': "1"
                        })
                    }
                    else if ($(this).parent().children(".post-downvote").css("color") == "rgb(0, 0, 255)")
                    {
                        var voteNum = $(this).parent().find(".votes");
                        voteNum.html(parseInt(voteNum.html()) + 2);
                        $(this).parent().children().css("color", "");
                        $(this).css("color", "orange");
                        $.post("/posts/interact", {
                            'id':  $(this).parent().parent().parent()[0].id,
                            'voteCount': "2"
                        })
                    }
                    else {
                        var voteNum = $(this).parent().find(".votes");
                        $(this).parent().children().css("color", "")
                        voteNum.html(parseInt(voteNum.html()) - 1);
                        $.post("/posts/interact", {
                            'id':  $(this).parent().parent().parent()[0].id,
                            'voteCount': "-3"
                        })
                    }
                }
                else {
                    $("#loginModal").modal("toggle");
                }
            });
            $('.post-downvote').off("click");

            $('.post-downvote').on("click", function(){
                if (sessionStorage.getItem("logged in") != "false"){
                    if ($(this).css("color") != "rgb(0, 0, 255)" && $(this).parent().children(".post-upvote").css("color") != "rgb(255, 165, 0)")
                    {  
                        var voteNum = $(this).parent().find(".votes");
                        voteNum.html(parseInt(voteNum.html()) - 1);
                        $(this).css("color", "blue");
                        $.post("/posts/interact", {
                            'id':  $(this).parent().parent().parent()[0].id,
                            'voteCount': "-1"
                        })
                    }
                    else if ($(this).parent().children(".post-upvote").css("color") == "rgb(255, 165, 0)")
                    {
                        var voteNum = $(this).parent().find(".votes");
                        voteNum.html(parseInt(voteNum.html()) - 2);
                        $(this).parent().children().css("color", "");
                        $(this).css("color", "blue");
                        $.post("/posts/interact", {
                            'id':  $(this).parent().parent().parent()[0].id,
                            'voteCount': "-2"
                        })
                        
                    }
                    else {
                        var voteNum = $(this).parent().find(".votes");
                        $(this).parent().children().css("color", "")
                        voteNum.html(parseInt(voteNum.html()) + 1);
                        $.post("/posts/interact", {
                            'id':  $(this).parent().parent().parent()[0].id,
                            'voteCount': "3"
                        })
                    }
                }
                else {
                    $("#loginModal").modal("toggle");
                }
            });

            $('#wmd-input').val("");
            localStorage.removeItem("writing");
        });
    });
    
    $('.post-upvote').on("click", function(){
        if (sessionStorage.getItem("logged in") != "false"){

            if ($(this).css("color") != "rgb(255, 165, 0)" && $(this).parent().children(".post-downvote").css("color") != "rgb(0, 0, 255)")
            {     
                var voteNum = $(this).parent().find(".votes");
                $(this).css("color", "orange");
                voteNum.html(parseInt(voteNum.html()) + 1);    
                $.post("/posts/interact", {
                    'id': $(this).parent().parent().parent()[0].id,
                    'voteCount': "1"
                })
            }
            else if ($(this).parent().children(".post-downvote").css("color") == "rgb(0, 0, 255)")
            {
                var voteNum = $(this).parent().find(".votes");
                voteNum.html(parseInt(voteNum.html()) + 2);
                $(this).parent().children().css("color", "");
                $(this).css("color", "orange");
                $.post("/posts/interact", {
                    'id':  $(this).parent().parent().parent()[0].id,
                    'voteCount': "2"
                })
            }
            else {
                var voteNum = $(this).parent().find(".votes");
                $(this).parent().children().css("color", "")
                voteNum.html(parseInt(voteNum.html()) - 1);
                $.post("/posts/interact", {
                    'id':  $(this).parent().parent().parent()[0].id,
                    'voteCount': "-3"
                })
            }
        }
        else {
            $("#loginModal").modal("toggle");
        }
    });
    
    $('.post-downvote').on("click", function(){
        if (sessionStorage.getItem("logged in") != "false"){
            if ($(this).css("color") != "rgb(0, 0, 255)" && $(this).parent().children(".post-upvote").css("color") != "rgb(255, 165, 0)")
            {  
                var voteNum = $(this).parent().find(".votes");
                voteNum.html(parseInt(voteNum.html()) - 1);
                $(this).css("color", "blue");
                $.post("/posts/interact", {
                    'id':  $(this).parent().parent().parent()[0].id,
                    'voteCount': "-1"
                })
            }
            else if ($(this).parent().children(".post-upvote").css("color") == "rgb(255, 165, 0)")
            {
                var voteNum = $(this).parent().find(".votes");
                voteNum.html(parseInt(voteNum.html()) - 2);
                $(this).parent().children().css("color", "");
                $(this).css("color", "blue");
                $.post("/posts/interact", {
                    'id':  $(this).parent().parent().parent()[0].id,
                    'voteCount': "-2"
                })
                
            }
            else {
                var voteNum = $(this).parent().find(".votes");
                $(this).parent().children().css("color", "")
                voteNum.html(parseInt(voteNum.html()) + 1);
                $.post("/posts/interact", {
                    'id':  $(this).parent().parent().parent()[0].id,
                    'voteCount': "3"
                })
            }
        }
        else {
            $("#loginModal").modal("toggle");
        }
    });

        togglemathjax = function(enabled) {
            if (enabled) {
                if (!latexenabledonce)
                {
                    MathJax.Hub.Config(
        {"HTML-CSS": { preferredFont: "TeX", availableFonts: ["STIX","TeX"], linebreaks: { automatic: true }, EqnChunk: (MathJax.Hub.Browser.isMobile ? 10 : 50) },
         tex2jax: { inlineMath: [ ["$", "$"], ["\\\\(","\\\\)"] ], displayMath: [ ["$$","$$"], ["\\[", "\\]"] ], processEscapes: true, ignoreClass: "tex2jax_ignore|dno" },
         TeX: {  noUndefined: { attributes: { mathcolor: "red", mathbackground: "#FFEEEE", mathsize: "90%" } }, Macros: { href: "{}" } },
         messageStyle: "none", skipStartupTypeset: true });
                    mjpd1.mathjaxEditing.prepareWmdForMathJax(editor, '', [["$", "$"]]);
                    latexenabledonce = true;
                    if (editor.refreshPreview !== undefined)
                        editor.refreshPreview();
                }
                else {
                    MathJax.Hub.queue.pending = 0;
                    MathJax.Hub.Queue(["Typeset", MathJax.Hub]); 
                   // MathJax.Hub.Queue(["Typeset", MathJax.Hub, "post-content"]);

                }
            }
            else {
                MathJax.Hub.queue.pending = 1; 
                if (editor.refreshPreview !== undefined)
                    editor.refreshPreview();
                else {
                   MathJax.Hub.Config({ skipStartupTypeset: true });
                }
            }
        }
        
        toggledarkmode = function(enabled){
            $('body').toggleClass('dark-mode',enabled);
        }
        
        if (localStorage.getItem("writing") !== null) {
            $('#wmd-input').val(localStorage.getItem("writing"));
        }

        if (localStorage.getItem("writing-reply") !== null) {
            $('#wmd-input-reply').val(localStorage.getItem("writing-reply"));
        }
        openFile = function(e) {         
          readFile(e.target.files[0]);
        }
        
        readFile = function(file){ // https://stackoverflow.com/a/26298948/1422096
          if (!file) {
            return;
          }
          var reader = new FileReader();
          reader.onload = function(e) {
            var contents = e.target.result;
            $('#wmd-input').val(contents);        // display file content
            editor.refreshPreview();
          };
          reader.readAsText(file);
        }
        
       // document.getElementById('openFileInput').addEventListener('change', openFile, false);
        
        $('body').on('drag dragstart dragend dragover dragenter dragleave drop', function(e) {
            e.preventDefault();
            e.stopPropagation();
          })
          .on('drop', function(e) {
            readFile(e.originalEvent.dataTransfer.files[0]);
        });
        
        $('#wmd-input').on('input', function() {
            localStorage.setItem("writing", $('#wmd-input').val());
        });
        
        $('#wmd-reply-input').on('input', function() {
            localStorage.setItem("writing-reply", $('#wmd-input-reply').val());
        });

        $('#wmd-input').focus();
        $('#wmd-preview').show();
        $('#wmd-reply-preview').show();

        $('#helpicon').click(function() {
            $('#help').show();
        });
        
        $('#closeicon, #wmd-input, #wmd-preview').click(function() {
            $('#help').hide();
        });
        
        // $(document).on('keydown', function(e) {
        //     if (e.keyCode == 80 && (e.ctrlKey || e.metaKey)) {    // CTRL + P 
        //         if (mode != 1) {
        //             mode = 1;
        //             $('#wmd-input').hide();
        //             $('#wmd-preview').show();
        //             $('body').removeClass('fixedheight');
        //             $('html').removeClass('fixedheight');
        //             toggledarkmode(false);
        //             e.preventDefault();
        //             window.print();
        //             toggledarkmode(darkmodeenabled);
        //             return false;
        //         }
        //         //var doc = new jsPDF();
        //         //var specialElementHandlers = {'#editor': function (element, renderer) { return true; } };
        //         //doc.fromHTML($('#wmd-preview').html(), 15, 15, { 'width': 170, 'elementHandlers': specialElementHandlers });
        //         //doc.save('file.pdf');
        //         /*var restorepage = $('body').html();
        //         var printcontent = $('#wmd-preview').clone();
        //         $('body').empty().html(printcontent);
        //         window.print();
        //         $('body').html(restorepage);
        //         e.preventDefault();
        //         return false;*/
        //     }
        //     else if (e.keyCode == 83 && (e.ctrlKey || e.metaKey)) {    // CTRL + S
        //         var blob = new Blob([$('#wmd-input').val()], {type: 'text'});     // https://stackoverflow.com/a/33542499/1422096
        //         if (window.navigator.msSaveOrOpenBlob) {
        //             window.navigator.msSaveBlob(blob, 'newfile.md');
        //         }
        //         else {
        //             var elem = window.document.createElement('a');
        //             elem.href = window.URL.createObjectURL(blob);
        //             elem.download = 'newfile.md';        
        //             document.body.appendChild(elem);
        //             elem.click();        
        //             document.body.removeChild(elem);
        //         }
        //         e.preventDefault();
        //         return false;
        //     }
        //     else if (e.keyCode == 68 && (e.ctrlKey || e.metaKey) && !e.shiftKey) {    // CTRL + D
        //         mode += 1; if (mode == 3) mode = 0;
        //         if (mode == 1) {
        //             $('#wmd-input').hide();
        //             $('#wmd-preview').show();
        //             $('body').removeClass('fixedheight');
        //             $('html').removeClass('fixedheight');
        //         }
        //         else if (mode == 2) {
        //             $('#wmd-preview').hide();            
        //             $('#wmd-input').show().css('float', 'none').css('width', '100%').focus();
        //             $('body').addClass('fixedheight');
        //             $('html').addClass('fixedheight');
        //         }
        //         else {
        //             $('#wmd-input').show().css('float', 'left').css('width', '50%').focus();
        //             $('#wmd-preview').show();
        //         }
        //         e.preventDefault();
        //         return false;
        //     }        
        //     else if (e.keyCode == 72 && (e.ctrlKey || e.metaKey) && e.shiftKey) {    // CTRL + H
        //         $('#help').show();
        //         e.preventDefault();
        //         return false;
        //     }
        //     else if (e.keyCode == 68 && (e.ctrlKey || e.metaKey) && e.shiftKey) {    // CTRL + SHIFT + D
        //       darkmodeenabled = !darkmodeenabled;
        //       localStorage.setItem("darkmode", darkmodeenabled ? "1" : "0");
        //       toggledarkmode(darkmodeenabled);
        //       e.preventDefault();
        //       return false;
        //     }    
        //     else if (e.keyCode == 82 && (e.ctrlKey || e.metaKey) && e.shiftKey) {    // CTRL + SHIFT + R
        //         $('html').toggleClass('texroman');
        //         e.preventDefault();
        //         return false;
        //     }    
        //     else if (e.keyCode == 76 && (e.ctrlKey || e.metaKey) && e.shiftKey) {    // CTRL + SHIFT + L 
        //         latexenabled = !latexenabled;
        //         localStorage.setItem("latex", latexenabled ? "1" : "0");
        //         togglemathjax(latexenabled);
        //         e.preventDefault();
        //         return false;
        //     }
        //     else if (e.keyCode == 79 && (e.ctrlKey || e.metaKey) && e.shiftKey) {    // CTRL + SHIFT + O
        //         $('#openFileInput').click();
        //         e.preventDefault();
        //         return false;
        //     } 
        //     else if (e.keyCode == 27)  { // ESC
        //         $('#help').hide();
        //     }
        // });
        
        var mode = 0;
        var latexenabledonce = false;
        var latexenabled = localStorage.getItem("latex") !== "0";
        var darkmodeenabled = localStorage.getItem("darkmode") == "1";
        var converter = Markdown.getSanitizingConverter();
        Markdown.Extra.init(converter);
        var editor = new Markdown.Editor(converter, '');
        var mjpd1 = new mjpd();
        togglemathjax(latexenabled);
        toggledarkmode(darkmodeenabled);
        editor.run();

});

