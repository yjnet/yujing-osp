<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage=""%>
<%@ page session="false" %>
<%@include file="/libs/foundation/global.jsp" %>
<link rel="stylesheet" href="/etc/clientlibs/common/css/jquery-ui.css" />
  <script src="/etc/clientlibs/common/js/jquery-1.10.2.js"></script>
  <script src="/etc/clientlibs/common/js/jquery-ui-1.11.4.js"></script>
  <script src="/etc/clientlibs/yujing-osp/ui/dialog/dialog.js"></script>
  
  <style>
    body { font-size: 62.5%; }
    label, input { display:block; }
    input.text { margin-bottom:12px; width:95%; padding: .4em; }
    fieldset { padding:0; border:0; margin-top:25px; }
    h1 { font-size: 1.2em; margin: .6em 0; }
    div#users-contain { width: 350px; margin: 20px 0; }
    div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
    div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
    .ui-dialog .ui-state-error { padding: .3em; }
    .validateTips { border: 1px solid transparent; padding: 0.3em; }
  </style>
  
 
<div id="dialog-form" title="Create new user">
  <p class="validateTips">All form fields are required.</p>
 <script>function try() {alert(navT.path);}</script>
  <form method="POST" action="/content/new-node" enctype="multipart/form-data">
   <span>Title: </span><input type="text" name="title" value="" class="text ui-widget-content ui-corner-all"/>
   <span>Text: </span><input type="text" name="text" value="" class="text ui-widget-content ui-corner-all"/>
   <span>Submit: </span><input type="submit" onclick="try()" tabindex="-1" style="position:absolute; top:-1000px">
</form>
</div>
 
 
<div id="users-contain" class="ui-widget">
  <h1>Existing Users:</h1>
  <table id="users" class="ui-widget ui-widget-content">
    <thead>
      <tr class="ui-widget-header ">
        <th>Name</th>
        <th>Email</th>
        <th>Password</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>John Doe</td>
        <td>john.doe@example.com</td>
        <td>johndoe1</td>
      </tr>
    </tbody>
  </table>
</div>
<button id="create-user">Create new user</button>
 <script>
 mydialog();
  </script>
 