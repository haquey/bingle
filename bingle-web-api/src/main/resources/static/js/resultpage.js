(function() {
  "use strict";

  window.onload = function() {

    var acc = document.getElementsByClassName("accordion");
    for (var i = 0; i < acc.length; i++) {
      acc[i].addEventListener("click", function() {
        this.classList.toggle("active");
        var panel = this.nextElementSibling;
        if ($(panel).attr('id') === 'file-panel' || $(panel).attr('id') === 'disc-panel' || $(panel).attr('id') === 'market-panel') {
          $(panel).slideToggle();
        }
      });
    }

    var url = new URL(window.location.href);
    var queryString = url.searchParams.get("query");
    var option = url.searchParams.get("option");

    document.querySelector("#queryString").value = queryString;

    document.querySelector("#" + option).checked = true;

    $.getJSON( "search/"+queryString + "/" + option, function(data) {
      if (data.length != 0) {
        data.forEach(insert_result);
      } else {
        $('#file-panel').remove();
      }
      $('#file-results').html(data.length);

    });

    $.getJSON( "/threads/search", {"query": queryString}, function(data) {
        if (data.length != 0) {
          data.forEach(insert_threads);
        } else {
          $('#disc-panel').remove();
        }
        $('#thread-results').html(data.length);
    });

    $.getJSON( "/marketplace/search/" + queryString, function(data) {
      if (data.length != 0) {
        data.forEach(insert_listing);
      } else {
        $('#market-panel').remove();
      }
      $('#marketplace-results').html(data.length);
    });

    function escapeHTML(html) {
      return html.replace(/<(?!\/?b>|\/?B>)[^>]+>/g, '');
    }

function insert_result(resultEntry) {
            var elmt = document.createElement('div');
            elmt.className = "row file";
            var url = encodeURI(resultEntry.path);
            var tags = resultEntry.tags.replace( /;/g , ', ');
            resultEntry.snippet = escapeHTML(resultEntry.snippet);
            var res = `
                <div class="col-sm-9 file-title">
                    <a href=${url}>${resultEntry.title}</a>
                    <label>Last Modified: ${resultEntry.lastModified}</label>
                    <label>Uploaded By: ${resultEntry.name}</label>
                    <label>${resultEntry.snippet}</label>
                   
                </div>

                <div class="col-sm-3 thread-tags">
                    <i class="fa fa-tags" aria-hidden="false"></i>
                    <label>Tags</label>
                    <div class="col-sm-12 tag-holder">`;

            tags.split(",").forEach(function(tag) {
        
                res += `<label>${tag}</label>`;

            });
            res += '</div></div>';
            elmt.innerHTML = res;
            document.querySelector("#file-panel").append(elmt); 
        }

  function insert_threads(resultEntry) {
    var elmt = document.createElement('div');
    elmt.className = "row file";
    var url = "/discussion/posts/" + resultEntry.id;
    var res = `<div class="col-sm-6 file-title">
                  <a href=${url}>${resultEntry.title}</a>
                  <label>Created At: ${resultEntry.createdAt}</label>
                  <label>Uploaded By: ${resultEntry.name} </label>
                  <label></label>
                </div>

                <div class="col-sm-3 thread-title">
                  <label><b>Discussion Post</b></label>  
                  <label><b>Points:</b> ${resultEntry.voteCount}</label>
                  <label><b>View:</b> ${resultEntry.viewCount}</label>
                </div>

                <div class="col-sm-3 thread-tags">
                    <i class="fa fa-tags" aria-hidden="false"></i>
                    <label>Tags</label>
                    <div class="col-sm-12 tag-holder">`;

    if (resultEntry.tags != null) {
      var tags = resultEntry.tags;
      tags.forEach(function(tag) {
        res += `<label>${tag}</label>`;
      });
    }
    
    res += '</div></div>';
    elmt.innerHTML = res;

    document.querySelector("#disc-panel").append(elmt);
  }

    function insert_listing(resultEntry) {

      var elmt = document.createElement('div');
      elmt.className = "row file";
      var url = "/discussion/posts/" + resultEntry.id;
      //var tags = resultEntry.tags.replace( /;/g , ', ')
      var res = `<div class="col-sm-6 file-title">
              <label style="font-weight:900;">${resultEntry.title}</label>
              <label>Created At: ${resultEntry.createdAt}</label>
              <label>Uploaded By: ${resultEntry.username} </label>
              <label>${resultEntry.description}</label>
            </div>

          <div class="col-sm-3 thread-title">
            <label style="font-weight:900"><b>Marketplace Listing</b></label>  
            <label>Price: <b>${resultEntry.price}</b></label>
            <label>Phone: <b>${resultEntry.phoneNo}</b></label>
            <label>Email: <b>${resultEntry.email}</b></label>
          </div>
          
          <div class="col-sm-3 thread-tags">
                    <i class="fa fa-tags" aria-hidden="false"></i>
                    <label>Tags</label>
                    <div class="col-sm-12 tag-holder">`;
      
          if (resultEntry.tags != null) {
            var tags = resultEntry.tags;
            tags.forEach(function(tag) {
              res += `<label>${tag}</label>`;
            });
          }
          
      res += '</div></div>';
      elmt.innerHTML = res;

      elmt.addEventListener('click', function(){
        viewListing(resultEntry.id);
        console.log("here");
      });

      document.querySelector("#market-panel").append(elmt);
    }


    function viewListing(data_id) {
      $.ajax({
          type: "POST",
          url: '/marketplace/' + data_id,
          contentType: "application/json",
          success: function (data) {
              console.log(data);
              $('#show-listing-title').html(data.title);
              $('#show-listing-viewcount').html(data.viewCount);

              if (data.category === 'ebook') {
                  $('#show-listing-category').html('eBooks');
              } else if (data.category === 'textbook') {
                  $('#show-listing-category').html('Textbooks');
              } else if (data.category === 'coursenote') {
                  $('#show-listing-category').html('Course Notes');
              } else if (data.category === 'coursematerial') {
                  $('#show-listing-category').html('Past Course Material');
              } else if (data.category === 'utencil') {
                  $('#show-listing-category').html('Utencils');
              } else if (data.category === 'misc') {
                  $('#show-listing-category').html('Miscellaneous');
              } else if (data.category === 'electronic') {
                  $('#show-listing-category').html('Electronics');
              } else if (data.category === 'clothing') {
                  $('#show-listing-category').html('Clothing Apparel');
              }

              
              $('#show-listing-user').html(data.username);
              $('#show-listing-desc').html(data.description);
              $('#show-listing-price').html('$' + data.price);
              $('#show-listing-phone').html(data.phoneNo);
              $('#show-listing-email').html(data.email);
              if (data.image != null) {
                  $('.img-holder').html('<img src="' + data.image + '" />');
              } else {
                  $('.img-holder').html('<i class="fas fa-book"></i>');
              }

              $('#view-listing').modal('show');
          },
          error: function (data) {
              console.log('An error occurred.');
              console.log(data);
          },
      });

  }

    document.querySelector('#queryForm').addEventListener('submit', function(e){
      e.preventDefault();
      var queryString = document.querySelector("#queryString").value;
      var option = document.querySelector('input[name="option"]:checked').value;
      window.location = '/resultpage?query='+ queryString.replace( /\\/g , ' ') + "&option=" + option;

    });






  };
}());