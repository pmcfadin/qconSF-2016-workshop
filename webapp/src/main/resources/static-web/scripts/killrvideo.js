$("#querySelectorMenu li a").click(function(){
    $("#querySelector:first-child").text($(this).text());
    $("#querySelector:first-child").val($(this).value);
    alert("blah");
    //Stops the link doing what it would normally
    return false;
});