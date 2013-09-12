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

object Indexing {
  case class  YesIHaveIt(jarName: String)
  case object NoIDont
  case class  SearchFuncs(cond: List[(String, String)])
  case class  Func(start: Int, end: Int, body: Option[String])
  case object DoneIndexing
}

class Indexer(jarPath: String) extends Actor {
  val jarName = jarPath
  var funcMap = new HashMap[String, Set[Func]] with MultiMap[String, Func]

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
      funcMap.addBinding(fqn + "." + node.getName.getFullyQualifiedName, Func(start, end, body))
      super.visit(node)
    }
  }

  override def preStart = {
    val jarFile = new JarFile(jarPath)
    jarFile.entries.filter(_.getName.contains(".java")) foreach { x =>
      extractMethods(x.getName, jarFile.getInputStream(x))
    }
    context.actorSelection("akka://application/user/Manager") ! DoneIndexing
  }

  def receive = {
    case SearchFuncs(funcList) =>
    val s = sender
    val holds = !funcList.exists{case(f, l) => !funcMap.entryExists(f, x => (l.toInt >= x.start) && (l.toInt <= x.end))}
    if (holds) {
      s ! YesIHaveIt(jarName)
    } else {
      s ! NoIDont
    }
  }
}

