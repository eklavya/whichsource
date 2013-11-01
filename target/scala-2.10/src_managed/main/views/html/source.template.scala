
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
object source extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template4[String,List[String],Int,Int,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(jarString: String)(source: List[String])(start: Int)(err: Int):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.65*/("""
        <h2>In """),_display_(Seq[Any](/*2.17*/jarString)),format.raw/*2.26*/(""" at line """),_display_(Seq[Any](/*2.36*/err)),format.raw/*2.39*/("""</h2>

        """),_display_(Seq[Any](/*4.10*/for(i <- 0 until source.length) yield /*4.41*/ {_display_(Seq[Any](format.raw/*4.43*/("""
            <pre><span class="line">"""),_display_(Seq[Any](/*5.38*/(start + i))),format.raw/*5.49*/("""</span><span class="code">"""),_display_(Seq[Any](/*5.76*/if(start + i == err)/*5.96*/{_display_(Seq[Any](format.raw/*5.97*/("""<span class="error">""")))})),_display_(Seq[Any](/*5.119*/Html(source.drop(i).head))),_display_(Seq[Any](/*5.145*/if(start + i == err)/*5.165*/{_display_(Seq[Any](format.raw/*5.166*/("""</span>""")))})),format.raw/*5.174*/("""</span></pre>
        """)))})),format.raw/*6.10*/("""

"""))}
    }
    
    def render(jarString:String,source:List[String],start:Int,err:Int): play.api.templates.HtmlFormat.Appendable = apply(jarString)(source)(start)(err)
    
    def f:((String) => (List[String]) => (Int) => (Int) => play.api.templates.HtmlFormat.Appendable) = (jarString) => (source) => (start) => (err) => apply(jarString)(source)(start)(err)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Tue Oct 22 11:45:56 IST 2013
                    SOURCE: /home/eklavya/Applications/Workspace/whichsource/app/views/source.scala.html
                    HASH: c713d05cd7ef93d83ba052bec04e4179a8a23ac3
                    MATRIX: 578->1|735->64|787->81|817->90|862->100|886->103|937->119|983->150|1022->152|1095->190|1127->201|1189->228|1217->248|1255->249|1317->271|1373->297|1402->317|1441->318|1481->326|1535->349
                    LINES: 19->1|22->1|23->2|23->2|23->2|23->2|25->4|25->4|25->4|26->5|26->5|26->5|26->5|26->5|26->5|26->5|26->5|26->5|26->5|27->6
                    -- GENERATED --
                */
            