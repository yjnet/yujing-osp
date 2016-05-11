<div>
	<div><span>Title: </span><input id="title" type="textinput" value="" /></div>
	<div><span>Name: </span><input id="name" type="textinput" value="" /></div>
	<hr />
	
	<ul id="templates"></ul>
	
	<script>
		$(document).ready(function() {
			$.ajax(function() {
				url : "/apps.prop.json?statement=/jcr:root/apps/*/templates/*",
			    type: "GET",
			    success: function(data, textStatus, jqXHR)
					    {
					    	for(var x=0; x<data.length; x++){
		
			                    var name = data[x].name;
			                    var sourceType = data[x]['jcr:path'];
			                    var description = data[x]['jcr:description'];
		
			                    var template ="<li><p value=\""+ sourceType + "\">" + name = "</p><p>"+ description +"</p>";
			                    
			                    $("#templates").append("<li>"+template+"</li>");
			                }
					    },
			    error: function (jqXHR, textStatus, errorThrown)
					    {
			    			alert('error: ' + textStatus);
				    	}
			})
		});
	
	</script>

</div>