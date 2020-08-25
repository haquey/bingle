(function() {
  "use strict";

  window.onload = function() {

    var images = ['../assets/DSC_4187.JPG', '../assets/DSC_4188.JPG', '../assets/DSC_4202.JPG', '../assets/DSC_4204.JPG', '../assets/DSC_4205.JPG', 
    	'../assets/DSC_4206.JPG', '../assets/DSC_4207.JPG', '../assets/DSC_4208.JPG', '../assets/DSC_4214.JPG', '../assets/DSC_4215.JPG', 
    	'../assets/DSC_4220.JPG', '../assets/DSC_4241.JPG', '../assets/DSC_4242.JPG', '../assets/DSC_4243.JPG', '../assets/DSC_4244.JPG', 
    	'../assets/DSC_4245.JPG', '../assets/DSC_4246.JPG', '../assets/DSC_4247.JPG' ]
    	var randomImage = Math.floor(Math.random() * 18); // 0 to length of images array - 1
    	$(".homepage").css("background-image", "url('" + images[randomImage] + "')");


    document.querySelector('#crawlForm').addEventListener('submit', function(e){
      e.preventDefault();
      var loading = true;
      var url = document.querySelector("#domain").value;
      var depth = document.querySelector("#depth").value;
      var page = document.querySelector("#page").value;
      var option = document.querySelector('input[name="option"]:checked').value;
      console.log(option);

      $.post( "/crawl", {"url": url, "depth": depth, "page": page, "mode": option}, function(data) {

        loading = false;
        $("#load").hide();
        $("#finish").show();
      });

      $("#load").show();
    });






  };
}());