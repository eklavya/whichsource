// @SOURCE:/home/eklavya/Applications/Workspace/whichsource/conf/routes
// @HASH:98a4f5f803c154d71a12c4f56a74477ede454075
// @DATE:Tue Oct 22 11:45:55 IST 2013


import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString

object Routes extends Router.Routes {

private var _prefix = "/"

def setPrefix(prefix: String) {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_Application_index0 = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
        

// @LINE:9
private[this] lazy val controllers_Assets_at1 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
        

// @LINE:10
private[this] lazy val controllers_Application_sourceFinder2 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("trace"))))
        

// @LINE:11
private[this] lazy val controllers_Application_getFunc3 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("func/"),DynamicPart("f", """[^/]+""",true))))
        
def documentation = List(("""GET""", prefix,"""controllers.Application.index"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """trace""","""controllers.Application.sourceFinder"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """func/$f<[^/]+>""","""controllers.Application.getFunc(f:String)""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]] 
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_Application_index0(params) => {
   call { 
        invokeHandler(controllers.Application.index, HandlerDef(this, "controllers.Application", "index", Nil,"GET", """ Home page""", Routes.prefix + """"""))
   }
}
        

// @LINE:9
case controllers_Assets_at1(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
   }
}
        

// @LINE:10
case controllers_Application_sourceFinder2(params) => {
   call { 
        invokeHandler(controllers.Application.sourceFinder, HandlerDef(this, "controllers.Application", "sourceFinder", Nil,"POST", """""", Routes.prefix + """trace"""))
   }
}
        

// @LINE:11
case controllers_Application_getFunc3(params) => {
   call(params.fromPath[String]("f", None)) { (f) =>
        invokeHandler(controllers.Application.getFunc(f), HandlerDef(this, "controllers.Application", "getFunc", Seq(classOf[String]),"GET", """""", Routes.prefix + """func/$f<[^/]+>"""))
   }
}
        
}

}
     