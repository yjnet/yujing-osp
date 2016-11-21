<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Parsys: jQuery UI Draggable + Sortable</title>
  <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
  <style>
  ul { list-style-type: none; margin: 0; padding: 0; margin-bottom: 10px; }
  # sortable li { margin: 5px; padding: 5px;  }
  .ui-dialog .ui-dialog-content {
  	padding: 0px 0px;
  }
  </style>
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
  <script>
  $( function() {
  	var origin = 'sortable';
  	
    $( "#sortable" ).sortable({
      	revert: true,
      	receive: function( event, ui) {
        },
        stop: function( event, ui) {
			drag(event, ui );
        }
      });
    
    $( "#sortable" ).droppable( {
    	 drop: function (event, ui) {
           if (origin === 'draggable') {
               ui.draggable.html('<li class="ui-widget-content ui-corner-tr">'
				+ '<span class="ui-widget-header">' + ui.helper.text() + '</span>'
				+ '<a href="images/high_tatras.jpg" title="Edit this component" class="ui-icon ui-icon-pencil">Edit</a>'
				+ '<a href="link/to/trash/script/when/we/have/js/off" title="Delete this component" class="ui-icon ui-icon-trash" onclick="return removeItem(this);">Delete</a>'
				+ '<div style="color: green; width: 100%;">Look at this new item</div>'
				+ '</li>');
               ui.draggable.attr("style", "margin: 0px 0px;");
               origin = 'sortable';
           }
           /* var item = ui.item.data("sortItem");
                if (item) {
                    //identify parents
                    var originalParent = ui.item.data("parentList");
                    var newParent = ui.item.parent().data("sortList");
                    //figure out its new position
                    var position = ko.utils.arrayIndexOf(ui.item.parent().children(), ui.item[0]);
                    if (position >= 0) {
                        originalParent.remove(item);
                        newParent.splice(position, 0, item);
                    }
                }
                */
           else {
          //  drag(event, ui );
          }
       }
    } ) ;
   
     function drag( event, ui) {
     	 if(ui.item.hasClass("number"))
              ui.sender.sortable("cancel");
     	//alert("position[ " + ui.item.index() + "]: " + ui.item.html());
    }

    
    $( "#sitekick > li" ).draggable({
    	 start: function()
			{
					origin = 'draggable';
			},
      connectToSortable: "#sortable",
      helper: "clone",
      appendTo: 'body',
      revert: 'invalid',
      scroll: false
    });
    
    $( "#sitekick" ).dialog(
    );
   
  } );
  
  function removeItem(item) {
	var comp = $(item);
	while (comp != null && (comp.parent().attr("id") == null || comp.parent().attr("id") !== "sortable")){
		comp = comp.parent();
	}
	
	if (comp != null)
	{
		alert("get: " + comp.html());
		comp.remove();
		return false;
	}
	
	return true;
  }
  </script>
  
  <link rel="stylesheet" href="http://code.jquery.com/ui/1.12.0/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="http://code.jquery.com/resources/demos/style.css">
  
  <style>
		.sidekick {
				position: fixed;
		}
		.sidekick .ui-dialog-titlebar-close {
				display: none;
		}
		
		li.ui-draggable, li.ui-droppable {
		    margin: 2px 0px 2px 0px;
		    padding: 2px;
		}
		
	.dragHover {
	    margin: 0em 0 0 1em;
	    padding: 0 0 2em 0;
	    border:1px gray dotted;
	    background-color:#ffeedd;
	}

	.ui-accordion-content-active {
	    background:#fff;
	    height:auto;
	}
	
	#sidekick { float: left; width: 65%; min-height: 12em; }
  .gallery.custom-state-active { background: #eee; }
  #sidekick li { float: left; width: 96px; padding: 0.4em; margin: 0 0.4em 0.4em 0; text-align: center; }
  #sidekick li h5 { margin: 0 0 0.4em; cursor: move; }
  #sidekick li a { float: right; }
  #sidekick li a.ui-icon-zoomin { float: left; }
  #sidekick li img { width: 100%; cursor: move; }
  	</style>
</head>
<body>

<div>
	<form>
		
			
<ul id="sitekick" title="sitekick">
  <li class="ui-state-highlight">Drag me down 1</li>
  <li class="ui-state-highlight">Drag me down 2</li>
  <li class="ui-state-highlight">Drag me down 3</li>
</ul>
 
 
<ul id="sortable" class="place-holder parsys-default-drop-zone ui-state-highlight" style="border-style: dashed; border-width: 1px; border-color: rgb(160,160,255);"
	data-resource-path="default-drop-zone">
  Drop components here
</ul>

<div class="ui-widget ui-helper-clearfix">
 
</div>
 </form>
 </div>
</body>
</html>