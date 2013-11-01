// @SOURCE:/home/eklavya/Applications/Workspace/whichsource/conf/routes
// @HASH:98a4f5f803c154d71a12c4f56a74477ede454075
// @DATE:Tue Oct 22 11:45:55 IST 2013

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString


// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:6
package controllers {

// @LINE:9
class ReverseAssets {
    

// @LINE:9
def at(file:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                
    
}
                          

// @LINE:11
// @LINE:10
// @LINE:6
class ReverseApplication {
    

// @LINE:11
def getFunc(f:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "func/" + implicitly[PathBindable[String]].unbind("f", dynamicString(f)))
}
                                                

// @LINE:6
def index(): Call = {
   Call("GET", _prefix)
}
                                                

// @LINE:10
def sourceFinder(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "trace")
}
                                                
    
}
                          
}
                  


// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:6
package controllers.javascript {

// @LINE:9
class ReverseAssets {
    

// @LINE:9
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        
    
}
              

// @LINE:11
// @LINE:10
// @LINE:6
class ReverseApplication {
    

// @LINE:11
def getFunc : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.getFunc",
   """
      function(f) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "func/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("f", encodeURIComponent(f))})
      }
   """
)
                        

// @LINE:6
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        

// @LINE:10
def sourceFinder : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.sourceFinder",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "trace"})
      }
   """
)
                        
    
}
              
}
        


// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:6
package controllers.ref {


// @LINE:9
class ReverseAssets {
    

// @LINE:9
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      
    
}
                          

// @LINE:11
// @LINE:10
// @LINE:6
class ReverseApplication {
    

// @LINE:11
def getFunc(f:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.getFunc(f), HandlerDef(this, "controllers.Application", "getFunc", Seq(classOf[String]), "GET", """""", _prefix + """func/$f<[^/]+>""")
)
                      

// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      

// @LINE:10
def sourceFinder(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.sourceFinder(), HandlerDef(this, "controllers.Application", "sourceFinder", Seq(), "POST", """""", _prefix + """trace""")
)
                      
    
}
                          
}
        
    