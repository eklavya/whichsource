package models

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
  case class GotIt(funcs: Map[String, List[Html]])
  case class SearchFuncs(cond: List[(String, String)])
  case object DoneIndexing
  case class NotIt(fl: List[String])
  case class StillIndexing(total: Int, left: Int)
}

class Indexer(jarPath: String, f: File, manager: ActorRef) {
  val jarName = jarPath

  def extractMethods(fName: String, is: InputStream) {
    val parser = ASTParser.newParser(AST.JLS4)
    // or use codec "latin1" !!!important
    parser.setSource(io.Source.fromInputStream(is)(Codec.ISO8859).toSeq.toArray)
    parser.setKind(ASTParser.K_COMPILATION_UNIT)
    parser.setEnvironment(null, Array(jarPath), null, true)
    parser.setUnitName("indexer")
    parser.setResolveBindings(true)
    val cu = parser.createAST(null).asInstanceOf[CompilationUnit]
    val fqn = cu.getPackage.getName + "." + fName.split('/').last.split('.').head
    cu.accept(new MethodVisitor(cu, fqn))
  }

  def addFunc(fName: String, f: Func) {
    Functions.add(fName, f)
  }

  class MethodVisitor(cu: CompilationUnit, fqn: String) extends ASTVisitor {
    override def visit(node: MethodDeclaration) = {
      val start = cu.getLineNumber(node.getStartPosition)
      val end = start + cu.getLineNumber(node.getLength)
      val body = processBody(node)
      addFunc(fqn + "." + node.getName.getFullyQualifiedName, new Func(fqn + "." + node.getName.getFullyQualifiedName, start, end, body, jarName.split('/').toList.last))
      super.visit(node)
    }

    def processBody(node: MethodDeclaration) = {
      Option(node.getBody) match {
        case Some(x) =>
          val invokeMap = scala.collection.mutable.Map.empty[String, String]
          Option(node.getBody).map(_.accept(new MethodInvocationVisitor(invokeMap)))
          var body = node.toString
          invokeMap foreach {
            case (k, v) =>
              // println(k + " -> " + v)
              // val K = k + "("
              body = body.replaceAll(k + """\(""", "<a href='/func/" + v + "&" + jarName.split('/').toList.last + "'>" + k + "</a>(")
          }
          // invokeMap foreach { case (k, v) => println(k + " -> " + v) }
          // println(body)
          Some(body)
        case None => None
      }

    }

    class MethodInvocationVisitor(invokeMap: scala.collection.mutable.Map[String, String]) extends ASTVisitor {
      override def visit(node: MethodInvocation) = {
        val name = node.getName.getFullyQualifiedName
        // println("visiting " + name)
        invokeMap += (name -> Option(node.resolveMethodBinding()).map { x =>
          // println(x.getDeclaringClass.getPackage + "." + x.getDeclaringClass.getName + "." + name)
          x.getDeclaringClass.getPackage.getName + "." + x.getDeclaringClass.getName + "." + name
        }.getOrElse(""))
        super.visit(node)
      }
    }
  }

  def index {
    val jarFile = new JarFile(jarPath)
    jarFile.entries.filter(_.getName.contains(".java")) foreach { x =>
      extractMethods(x.getName, jarFile.getInputStream(x))
    }
    val a = new PrintWriter(f)
    try { a.write(jarPath) } finally { a.close() }
    manager ! DoneIndexing
  }
}

