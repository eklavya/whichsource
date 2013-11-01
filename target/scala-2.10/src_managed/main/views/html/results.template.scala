
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
object results extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template2[List[scala.Tuple2[String, List[Html]]],Html,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(resList: List[(String, List[Html])], h: Html):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.48*/("""
<!DOCTYPE html>
<html>
<title>Which Source
</title>
<head>
    <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*7.50*/routes/*7.56*/.Assets.at("stylesheets/stylesheet.css"))),format.raw/*7.96*/("""">
    <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*8.50*/routes/*8.56*/.Assets.at("stylesheets/main.css"))),format.raw/*8.90*/("""">
    <link rel="shortcut icon" type="image/png" href=""""),_display_(Seq[Any](/*9.55*/routes/*9.61*/.Assets.at("images/favicon.png"))),format.raw/*9.93*/("""">
    <script src=""""),_display_(Seq[Any](/*10.19*/routes/*10.25*/.Assets.at("javascripts/jquery-1.9.0.min.js"))),format.raw/*10.70*/("""" type="text/javascript"></script>
    <script src=""""),_display_(Seq[Any](/*11.19*/routes/*11.25*/.Assets.at("javascripts/script_whichsource.js"))),format.raw/*11.72*/("""" type="text/javascript"></script>
    <link href='http://fonts.googleapis.com/css?family=Inconsolata:400' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Inconsolata:400' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Quintessential' rel='stylesheet' type='text/css'>
    <script type="text/javascript">
        function showSource(num) """),format.raw/*16.34*/("""{"""),format.raw/*16.35*/("""
        var bodies = document.getElementsByClassName("body" );
        for (var i = 0; i < bodies.length; i++) """),format.raw/*18.49*/("""{"""),format.raw/*18.50*/("""
        bodies.item(i).style.display = "none";
        """),format.raw/*20.9*/("""}"""),format.raw/*20.10*/("""
        document.getElementById("source-code" + num ).style.display = "block";
        """),format.raw/*22.9*/("""}"""),format.raw/*22.10*/("""
    </script>

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
    <div id ="directory"> """),_display_(Seq[Any](/*62.28*/h)),format.raw/*62.29*/("""
        <!--<ul class="selectiontabs">-->
            <!--<INPUT TYPE="image" class="list" SRC=""""),_display_(Seq[Any](/*64.56*/routes/*64.62*/.Assets.at("images/list.png"))),format.raw/*64.91*/("""" onclick="alert('...')" />-->
            <!--<INPUT TYPE="image" class="folder" SRC=""""),_display_(Seq[Any](/*65.58*/routes/*65.64*/.Assets.at("images/folder.png"))),format.raw/*65.95*/("""" onclick="alert('...')">-->
        <!--</ul>-->

        <div>
        </div>

        <div>
        </div>

    </div>
    <div id ="details">
        """),_display_(Seq[Any](/*76.10*/defining(Html(resList.zipWithIndex.map(x => "<div class='body' id='source-code" + x._2 + "'>" + x._1._2.mkString(" ") + "</div>").mkString(" ")))/*76.155*/ { sources =>_display_(Seq[Any](format.raw/*76.168*/("""
        <div id="source" class="play-error-page"> """),_display_(Seq[Any](/*77.52*/sources)),format.raw/*77.59*/(""" </div>
        """)))})),format.raw/*78.10*/("""
    </div>
</div>
<script>
    var f = function() """),format.raw/*82.24*/("""{"""),format.raw/*82.25*/("""
    var bodies = document.getElementsByClassName("body" );
    for (var i = 0; i < bodies.length; i++) """),format.raw/*84.45*/("""{"""),format.raw/*84.46*/("""
    bodies.item(i).style.display = "none";
    """),format.raw/*86.5*/("""}"""),format.raw/*86.6*/("""
    """),format.raw/*87.5*/("""}"""),format.raw/*87.6*/(""";
    f();
</script>

</body>
</html>"""))}
    }
    
    def render(resList:List[scala.Tuple2[String, List[Html]]],h:Html): play.api.templates.HtmlFormat.Appendable = apply(resList,h)
    
    def f:((List[scala.Tuple2[String, List[Html]]],Html) => play.api.templates.HtmlFormat.Appendable) = (resList,h) => apply(resList,h)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Fri Nov 01 12:10:36 IST 2013
                    SOURCE: /home/eklavya/Applications/Workspace/whichsource/app/views/results.scala.html
                    HASH: e770c9754f5251d85db9083d2b2681c7877e6980
                    MATRIX: 595->1|735->47|879->156|893->162|954->202|1041->254|1055->260|1110->294|1202->351|1216->357|1269->389|1326->410|1341->416|1408->461|1497->514|1512->520|1581->567|2030->988|2059->989|2199->1101|2228->1102|2311->1158|2340->1159|2455->1247|2484->1248|2753->1480|2769->1486|2823->1517|3030->1688|3045->1694|3096->1723|3344->1934|3360->1940|3414->1971|3688->2208|3704->2214|3758->2245|4035->2485|4051->2491|4105->2522|4382->2762|4398->2768|4452->2799|4669->2980|4692->2981|4826->3079|4841->3085|4892->3114|5016->3202|5031->3208|5084->3239|5275->3394|5430->3539|5482->3552|5570->3604|5599->3611|5648->3628|5727->3679|5756->3680|5888->3784|5917->3785|5992->3833|6020->3834|6052->3839|6080->3840
                    LINES: 19->1|22->1|28->7|28->7|28->7|29->8|29->8|29->8|30->9|30->9|30->9|31->10|31->10|31->10|32->11|32->11|32->11|37->16|37->16|39->18|39->18|41->20|41->20|43->22|43->22|52->31|52->31|52->31|57->36|57->36|57->36|60->39|60->39|60->39|65->44|65->44|65->44|69->48|69->48|69->48|73->52|73->52|73->52|83->62|83->62|85->64|85->64|85->64|86->65|86->65|86->65|97->76|97->76|97->76|98->77|98->77|99->78|103->82|103->82|105->84|105->84|107->86|107->86|108->87|108->87
                    -- GENERATED --
                */
            