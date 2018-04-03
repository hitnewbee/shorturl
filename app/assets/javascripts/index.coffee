$ ->
  $.get "/links", (Links) ->
    $.each Links, (index, links) ->
      $("#Links").append $("<li>").text links.keyword


