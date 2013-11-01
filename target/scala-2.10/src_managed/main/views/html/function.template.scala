
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
object function extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template2[List[Html],Html,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(resList: List[Html], h: Html):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.32*/("""
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

</head>

<body>

<div id="top">
    <div>
        <div class="insidediv">WHICH <a id="sourcered">SOURCE</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src=""""),_display_(Seq[Any](/*22.175*/routes/*22.181*/.Assets.at("images/search.png"))),format.raw/*22.212*/(""""></input>
        belong to
        <button id='Mbutton' type="button" class="more" onclick="moreclick(this)">more...</button>
    </div>

    <img class="logotop" src=""""),_display_(Seq[Any](/*27.32*/routes/*27.38*/.Assets.at("images/logo.png"))),format.raw/*27.67*/("""">

    <div class="findmethod">
        <div class="insidediv">FIND <a id="sourcered">Methods</a> that use </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src=""""),_display_(Seq[Any](/*30.179*/routes/*30.185*/.Assets.at("images/search.png"))),format.raw/*30.216*/(""""></input>
        ...
    </div>

    <div class="findmethod">
        <div class="insidediv">Question <a id="sourcered">#3</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src=""""),_display_(Seq[Any](/*35.174*/routes/*35.180*/.Assets.at("images/search.png"))),format.raw/*35.211*/(""""></input>
        goes...
    </div>
    <div class="findmethod">
        <div class="insidediv">Question <a id="sourcered">#4</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src=""""),_display_(Seq[Any](/*39.174*/routes/*39.180*/.Assets.at("images/search.png"))),format.raw/*39.211*/(""""></input>
        goes...
    </div>
    <div class="findmethod">
        <div class="insidediv">Question <a id="sourcered">#5</a> does </div><input type="text" class="inputbox" name="FirstName" value="..." ><img class = "searchicon"src=""""),_display_(Seq[Any](/*43.174*/routes/*43.180*/.Assets.at("images/search.png"))),format.raw/*43.211*/(""""></input>
        goes...
        <button id='lbutton' type="button" class="more" onclick="lessclick(this)">less...</button>
    </div>


</div>


<div>
    <div id ="directory"> """),_display_(Seq[Any](/*53.28*/h)),format.raw/*53.29*/("""
        <!--<ul class="selectiontabs">-->
        <!--<INPUT TYPE="image" class="list" SRC=""""),_display_(Seq[Any](/*55.52*/routes/*55.58*/.Assets.at("images/list.png"))),format.raw/*55.87*/("""" onclick="alert('...')" />-->
        <!--<INPUT TYPE="image" class="folder" SRC=""""),_display_(Seq[Any](/*56.54*/routes/*56.60*/.Assets.at("images/folder.png"))),format.raw/*56.91*/("""" onclick="alert('...')">-->
        <!--</ul>-->

        <div>
        </div>

        <div>
        </div>

    </div>
    <div id ="details">
        """),_display_(Seq[Any](/*67.10*/defining(Html(resList.map(x => "<div class='body'>" + x + "</div>").mkString(" ")))/*67.93*/ { sources =>_display_(Seq[Any](format.raw/*67.106*/("""
        <div id="source" class="play-error-page"> """),_display_(Seq[Any](/*68.52*/sources)),format.raw/*68.59*/(""" </div>
        """)))})),format.raw/*69.10*/("""
    </div>
</div>

</body>
</html>"""))}
    }
    
    def render(resList:List[Html],h:Html): play.api.templates.HtmlFormat.Appendable = apply(resList,h)
    
    def f:((List[Html],Html) => play.api.templates.HtmlFormat.Appendable) = (resList,h) => apply(resList,h)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Fri Nov 01 12:33:38 IST 2013
                    SOURCE: /home/eklavya/Applications/Workspace/whichsource/app/views/function.scala.html
                    HASH: 2a8e8634f6c5a7b08dc2ea867b5eadf0914c651d
                    MATRIX: 568->1|692->31|836->140|850->146|911->186|998->238|1012->244|1067->278|1159->335|1173->341|1226->373|1283->394|1298->400|1365->445|1454->498|1469->504|1538->551|2144->1120|2160->1126|2214->1157|2421->1328|2436->1334|2487->1363|2735->1574|2751->1580|2805->1611|3079->1848|3095->1854|3149->1885|3426->2125|3442->2131|3496->2162|3773->2402|3789->2408|3843->2439|4060->2620|4083->2621|4213->2715|4228->2721|4279->2750|4399->2834|4414->2840|4467->2871|4658->3026|4750->3109|4802->3122|4890->3174|4919->3181|4968->3198
                    LINES: 19->1|22->1|28->7|28->7|28->7|29->8|29->8|29->8|30->9|30->9|30->9|31->10|31->10|31->10|32->11|32->11|32->11|43->22|43->22|43->22|48->27|48->27|48->27|51->30|51->30|51->30|56->35|56->35|56->35|60->39|60->39|60->39|64->43|64->43|64->43|74->53|74->53|76->55|76->55|76->55|77->56|77->56|77->56|88->67|88->67|88->67|89->68|89->68|90->69
                    -- GENERATED --
                */
            