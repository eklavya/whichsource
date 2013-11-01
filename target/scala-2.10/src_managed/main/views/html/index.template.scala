
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
object index extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template1[String,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(message: String):play.api.templates.HtmlFormat.Appendable = {
        _display_ {import helper._


Seq[Any](format.raw/*1.19*/("""
"""),format.raw/*3.1*/("""<!--"""),_display_(Seq[Any](/*3.6*/main("WhichSource")/*3.25*/ {_display_(Seq[Any](format.raw/*3.27*/("""-->

	<!--<div id="traceDiv">-->
		<!--"""),_display_(Seq[Any](/*6.8*/form(action = routes.Application.sourceFinder())/*6.56*/ {_display_(Seq[Any](format.raw/*6.58*/("""-->
			<!--<textarea name="trace" id="trace" placeholder="Paste your stacktrace here."></textarea>-->
			<!--<input name="submit" id="submit" type="submit" value="Post" />-->
		<!--""")))})),format.raw/*9.8*/("""-->
	<!--</div>-->
<!--""")))})),format.raw/*11.6*/("""-->

<html>
<title>Which Source
</title>
<head>
    <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*17.50*/routes/*17.56*/.Assets.at("stylesheets/stylesheet.css"))),format.raw/*17.96*/("""">
    <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*18.50*/routes/*18.56*/.Assets.at("stylesheets/main.css"))),format.raw/*18.90*/("""">
    <link rel="shortcut icon" type="image/png" href=""""),_display_(Seq[Any](/*19.55*/routes/*19.61*/.Assets.at("images/favicon.png"))),format.raw/*19.93*/("""">
    <script src=""""),_display_(Seq[Any](/*20.19*/routes/*20.25*/.Assets.at("javascripts/jquery-1.9.0.min.js"))),format.raw/*20.70*/("""" type="text/javascript"></script>
    <script src=""""),_display_(Seq[Any](/*21.19*/routes/*21.25*/.Assets.at("javascripts/script_whichsource.js"))),format.raw/*21.72*/("""" type="text/javascript"></script>
    <link href='http://fonts.googleapis.com/css?family=Inconsolata:400' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Inconsolata:400' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Quintessential' rel='stylesheet' type='text/css'>
</head>

<body>

<div id="top">
    <div>
        <div class="insidediv">WHICH <a id="sourcered">SOURCE</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src=""""),_display_(Seq[Any](/*31.175*/routes/*31.181*/.Assets.at("images/search.png"))),format.raw/*31.212*/(""""></input>
        belong to
        <button id='Mbutton' type="button" class="more" onclick="moreclick(this)">more...</button>
    </div>

    <img class="logotop" src=""""),_display_(Seq[Any](/*36.32*/routes/*36.38*/.Assets.at("images/logo.png"))),format.raw/*36.67*/("""">

    <div class="findmethod">
        <div class="insidediv">FIND <a id="sourcered">Methods</a> that use </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src=""""),_display_(Seq[Any](/*39.179*/routes/*39.185*/.Assets.at("images/search.png"))),format.raw/*39.216*/(""""></input>
        ...
    </div>

    <div class="findmethod">
        <div class="insidediv">Question <a id="sourcered">#3</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src=""""),_display_(Seq[Any](/*44.174*/routes/*44.180*/.Assets.at("images/search.png"))),format.raw/*44.211*/(""""></input>
        goes...
    </div>
    <div class="findmethod">
        <div class="insidediv">Question <a id="sourcered">#4</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src=""""),_display_(Seq[Any](/*48.174*/routes/*48.180*/.Assets.at("images/search.png"))),format.raw/*48.211*/(""""></input>
        goes...
    </div>
    <div class="findmethod">
        <div class="insidediv">Question <a id="sourcered">#5</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src=""""),_display_(Seq[Any](/*52.174*/routes/*52.180*/.Assets.at("images/search.png"))),format.raw/*52.211*/(""""></input>
        goes...
        <button id='lbutton' type="button" class="more" onclick="lessclick(this)">less...</button>
    </div>


</div>


<div>
    <div id ="directory"> Paste a new trace.
        <ul class="selectiontabs">
            <!--<INPUT TYPE="image" class="list" SRC=""""),_display_(Seq[Any](/*64.56*/routes/*64.62*/.Assets.at("images/list.png"))),format.raw/*64.91*/("""" onclick="alert('...')" />-->
            <!--<INPUT TYPE="image" class="folder" SRC=""""),_display_(Seq[Any](/*65.58*/routes/*65.64*/.Assets.at("images/folder.png"))),format.raw/*65.95*/("""" onclick="alert('...')">-->
        </ul>

        <div>
        </div>

        <div>
        </div>

    </div>
    <div id ="details">
        """),_display_(Seq[Any](/*76.10*/form(action = routes.Application.sourceFinder())/*76.58*/ {_display_(Seq[Any](format.raw/*76.60*/("""
            <textarea name="trace" id="trace" placeholder="Paste your stacktrace here."></textarea>
            <input name="submit" id="submit" type="submit" value="Post" />
        """)))})),format.raw/*79.10*/("""
    </div>
</div>


</body>
</html>"""))}
    }
    
    def render(message:String): play.api.templates.HtmlFormat.Appendable = apply(message)
    
    def f:((String) => play.api.templates.HtmlFormat.Appendable) = (message) => apply(message)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Fri Nov 01 11:27:23 IST 2013
                    SOURCE: /home/eklavya/Applications/Workspace/whichsource/app/views/index.scala.html
                    HASH: 949e3ef61c93f3610e7b449dd157dabd8f9f4a41
                    MATRIX: 556->1|683->18|710->36|749->41|776->60|815->62|889->102|945->150|984->152|1196->334|1251->358|1384->455|1399->461|1461->501|1549->553|1564->559|1620->593|1713->650|1728->656|1782->688|1839->709|1854->715|1921->760|2010->813|2025->819|2094->866|2699->1434|2715->1440|2769->1471|2976->1642|2991->1648|3042->1677|3290->1888|3306->1894|3360->1925|3634->2162|3650->2168|3704->2199|3981->2439|3997->2445|4051->2476|4328->2716|4344->2722|4398->2753|4723->3042|4738->3048|4789->3077|4913->3165|4928->3171|4981->3202|5165->3350|5222->3398|5262->3400|5479->3585
                    LINES: 19->1|23->1|24->3|24->3|24->3|24->3|27->6|27->6|27->6|30->9|32->11|38->17|38->17|38->17|39->18|39->18|39->18|40->19|40->19|40->19|41->20|41->20|41->20|42->21|42->21|42->21|52->31|52->31|52->31|57->36|57->36|57->36|60->39|60->39|60->39|65->44|65->44|65->44|69->48|69->48|69->48|73->52|73->52|73->52|85->64|85->64|85->64|86->65|86->65|86->65|97->76|97->76|97->76|100->79
                    -- GENERATED --
                */
            