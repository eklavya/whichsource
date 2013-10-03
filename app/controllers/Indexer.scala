package controllers

import akka.actor._
import scala.collection.mutable.{ HashMap, MultiMap, Set }
import collection.JavaConversions._
import java.util.jar.JarFile
import java.util.jar.JarEntry
import java.io.InputStream
import Indexing._
import java.io.File
import org.eclipse.jdt.core.dom._
import scala.io.Codec
import play.api.templates.Html
import java.io.PrintWriter
import models._

object Indexing {
  case class  GotIt(funcs: Map[String, List[Html]])
  case class  SearchFuncs(cond: List[(String, String)])
  case object DoneIndexing
  case class  NotIt(fl: List[String])
  case class  StillIndexing(total: Int, left: Int)
  var funcMap = new FuncMap
}

class Indexer(jarPath: String, f: File, manager: ActorRef) {
  val jarName = jarPath
  
  def extractMethods(fName: String, is: InputStream) {
    val parser = ASTParser.newParser(AST.JLS4)
    // or use codec "latin1" !!!important
    parser.setSource(io.Source.fromInputStream(is)(Codec.ISO8859).toSeq.toArray)
    parser.setKind(ASTParser.K_COMPILATION_UNIT)
    val cu = parser.createAST(null).asInstanceOf[CompilationUnit]
    val fqn = cu.getPackage.getName + "." + fName.split('/').last.split('.').head
    cu.accept(new MethodVisitor(cu, fqn))
  }
  
  class MethodVisitor(cu: CompilationUnit, fqn: String) extends ASTVisitor {
    override def visit(node: MethodDeclaration) = {
      val start = cu.getLineNumber(node.getStartPosition)
      val end   = start + cu.getLineNumber(node.getLength)
      val body  = Option(node.getBody).map(x => Some(x.toString)).getOrElse(None)
      funcMap.addBinding(fqn + "." + node.getName.getFullyQualifiedName, new Func(fqn + "." + node.getName.getFullyQualifiedName, start, end, body, jarName.split('/').toList.last))
      super.visit(node)
    }
  }

  def index {
    val jarFile = new JarFile(jarPath)
    jarFile.entries.filter(_.getName.contains(".java")) foreach { x =>
      extractMethods(x.getName, jarFile.getInputStream(x))
    }
    val a = new PrintWriter(f)
    try {a.write(jarPath)} finally{a.close()}
    manager ! DoneIndexing
  }
}

