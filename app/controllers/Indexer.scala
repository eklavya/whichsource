package controllers

import akka.actor._
import scala.collection.immutable.HashMap
import collection.JavaConversions._
import java.util.jar.JarFile
import java.util.jar.JarEntry
import java.io.InputStream
import Indexing._
import java.io.File
import org.eclipse.jdt.core.dom._
import java.io.BufferedReader
import java.io.BufferedInputStream
import scala.io.Codec

object Indexing {
  case class DoYouHaveIt(funcName: String, line: Int)
  case class YesIHaveIt(jarName: String, code: String)
  case class SearchFuncs(cond: List[String])
}

class Indexer(jarPath: String) extends Actor {
  val jarName = jarPath
  var funcMap = Map.empty[String, Int]

  def extractMethods(is: InputStream) {
    val parser = ASTParser.newParser(AST.JLS4)
//    parser.setSource(io.Source.fromInputStream(is).toSeq.toArray)
    // or use codec "latin1" !!!important
    parser.setSource(io.Source.fromInputStream(is)(Codec.ISO8859).getLines.foldLeft(Array.empty[Char])((a, b) => a ++ b.toCharArray()))
    parser.setKind(ASTParser.K_COMPILATION_UNIT)

    val cu = parser.createAST(null).asInstanceOf[CompilationUnit]

    cu.accept(new MethodVisitor(cu))
  }
  
  class MethodVisitor(cu: CompilationUnit) extends ASTVisitor {
    override def visit(node: MethodDeclaration) = {
//    println(node.getName + " at line " + cu.getLineNumber(node.getStartPosition))
      funcMap += (node.getName.getFullyQualifiedName -> cu.getLineNumber(node.getStartPosition))
      true
    }
  }
  
  override def preStart = {
//    println("Indexer spawned for " + jarPath)
    val jarFile = new JarFile(jarPath)
    jarFile.entries.filter(_.getName.contains(".java")) foreach { x =>
//        println("Calling extractMethods on " + x.getName)
      extractMethods(jarFile.getInputStream(x))
    }
  }

  def receive = {
    case SearchFuncs(funcList) =>
      println("Searching if I have it.")
      if (funcMap contains funcList.head) {
        val f = funcMap(funcList.head)
        sender ! YesIHaveIt(jarName, f.toString)
      } else {
        sender ! YesIHaveIt("no I don't", "nowhere")
      }
  }
}

