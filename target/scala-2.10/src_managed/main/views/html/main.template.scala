
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import play.api.i18n._
import play.api.mvc._
import play.api.data._
import views.html._
/**/
object main extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template2[String,Html,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(title: String)(content: Html):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.32*/("""

<!DOCTYPE html>

<html>
    <head>
        <title>"""),_display_(Seq[Any](/*7.17*/title)),format.raw/*7.22*/("""</title>
        <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*8.54*/routes/*8.60*/.Assets.at("stylesheets/main.css"))),format.raw/*8.94*/("""">
        <link rel="shortcut icon" type="image/png" href=""""),_display_(Seq[Any](/*9.59*/routes/*9.65*/.Assets.at("images/favicon.png"))),format.raw/*9.97*/("""">
        <script src=""""),_display_(Seq[Any](/*10.23*/routes/*10.29*/.Assets.at("javascripts/jquery-1.9.0.min.js"))),format.raw/*10.74*/("""" type="text/javascript"></script>
        <link href='http://fonts.googleapis.com/css?family=Inconsolata:400' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Inconsolata:400' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Quintessential' rel='stylesheet' type='text/css'>
    </head>
    <body>
    <div id="mainDiv">
        <a class="mainHeading" href="/">WhichSource</a>

        """),_display_(Seq[Any](/*19.10*/content)),format.raw/*19.17*/("""

    </div>
    </body>
</html>

<html>
<title>Which Source
</title>
<head>
    <link rel="stylesheet" type="text/css" href="stylesheet.css">
    <script src='script_whichsource.js'></script>
</head>

<body>

<div id="top">
    <div>
        <div class="insidediv">WHICH <a id="sourcered">SOURCE</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src="search.png"></input>
        belong to
        <button id='Mbutton' type="button" class="more" onclick="moreclick(this)">more...</button>
    </div>

    <img class="logotop" src="logo.png">

    <div class="findmethod">
        <div class="insidediv">FIND <a id="sourcered">Methods</a> that use </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src="search.png"></input>
        ...
    </div>

    <div class="findmethod">
        <div class="insidediv">Question <a id="sourcered">#3</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src="search.png"></input>
        goes...
    </div>
    <div class="findmethod">
        <div class="insidediv">Question <a id="sourcered">#4</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src="search.png"></input>
        goes...
    </div>
    <div class="findmethod">
        <div class="insidediv">Question <a id="sourcered">#5</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src="search.png"></input>
        goes...
        <button id='lbutton' type="button" class="more" onclick="lessclick(this)">less...</button>
    </div>


</div>


<div>
    <div id ="directory"> Found in 10 places
        <ul class="selectiontabs">
            <INPUT TYPE="image" class="list" SRC="list.png" onclick="alert('...')" />
            <INPUT TYPE="image" class="folder" SRC="folder.png" onclick="alert('...')">
        </ul>

        <div>
        </div>

        <div>
        </div>

    </div>
    <div id ="details"> <p class="detailstext">Details go here...</p>
    </div>
</div>


</body>
</html>"""))}
    }
    
    def render(title:String,content:Html): play.api.templates.HtmlFormat.Appendable = apply(title)(content)
    
    def f:((String) => (Html) => play.api.templates.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Fri Nov 01 11:17:52 IST 2013
                    SOURCE: /home/eklavya/Applications/Workspace/whichsource/app/views/main.scala.html
                    HASH: 85861101434bb780c5384e6a700bc4ccf7244d73
                    MATRIX: 560->1|684->31|772->84|798->89|895->151|909->157|964->191|1060->252|1074->258|1127->290|1188->315|1203->321|1270->366|1782->842|1811->849
                    LINES: 19->1|22->1|28->7|28->7|29->8|29->8|29->8|30->9|30->9|30->9|31->10|31->10|31->10|40->19|40->19
                    -- GENERATED --
                */
            