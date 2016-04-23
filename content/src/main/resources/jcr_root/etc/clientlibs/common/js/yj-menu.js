function tree_menu(rootId) {
	$(document).ready(function(){
		$(rootId).treeview({
			animated:"normal",
			persist: "cookie"
		});
	});
			
	

	
	$(rootId).find("li").click(function() {
		   $(this).css("backgroundColor", "red"); 
	});
}

function add_node(newNode, currenNode) {
	$(currenNode).click(function(){
		var branches = newNode.appendTo(currenNode);
		$(currenNode).treeview({
			add: branches
		});
	});
}