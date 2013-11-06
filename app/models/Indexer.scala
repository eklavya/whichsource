package models

import akka.actor._
import collection.JavaConversions._
import scala.concurrent._
import ExecutionContext.Implicits.global
import java.util.jar.JarFile
import java.io.{File, InputStream, FileWriter, FileNotFoundException}

import Indexing._
import org.eclipse.jdt.core.dom._
import scala.io.Codec
import play.api.templates.Html
import com.typesafe.config.ConfigFactory
import scala.concurrent._
import scala.Some
import play.libs.Akka

object Indexing {
  case class GotIt(funcs: Map[String, List[Html]])
  case class SearchFuncs(cond: List[(String, String)])
  case class DoneIndexing(jarPath: String)
  case class NotIt(fl: List[String])
  case class Index(jarPath: String)
}

trait IndexerService {

  def jarDir: String
  def jars = new java.io.File(jarDir).listFiles().filter(_.getName().contains(".jar"))
  var jarsToIndex: Int
  def jarListBackup: String
  def cachedJars = new java.io.File(jarListBackup)

  private val indexer = Akka.system.actorOf(Props(new Actor {
    def receive = {
      case DoneIndexing(jarPath) =>
        jarsToIndex -= 1
        indexingFinished(jarPath)
        if (jarsToIndex == 0) {
          persistIndex
        }

      case Index(jarPath: String) =>
        future(index(jarPath))
    }
  }), "indexer")

  def addFunc(fName: String, f: Func): Unit

  def persistIndex: Unit

  def index(jarPath: String) {
    val jarFile = new JarFile(jarPath)
    jarFile.entries.filter(_.getName.contains(".java")) foreach { x =>
      extractMethods(x.getName, jarFile.getInputStream(x), jarPath)
    }
    indexer ! DoneIndexing(jarPath)
  }

  def extractMethods(fName: String, is: InputStream, jarPath: String) {
    val parser = ASTParser.newParser(AST.JLS4)
    // or use codec "latin1" !!!important
    parser.setSource(io.Source.fromInputStream(is)(Codec.ISO8859).toSeq.toArray)
    parser.setKind(ASTParser.K_COMPILATION_UNIT)
    parser.setEnvironment(null, Array(jarPath), null, true)
    parser.setUnitName("indexer")
    parser.setResolveBindings(true)
    val cu = parser.createAST(null).asInstanceOf[CompilationUnit]
    val fqn = cu.getPackage.getName + "." + fName.split('/').last.split('.').head
    cu.accept(new MethodVisitor(cu, fqn, jarPath))
  }


  class MethodVisitor(cu: CompilationUnit, fqn: String, jarName: String) extends ASTVisitor {
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
              body = body.replaceAll(k + """\(""", "<a href='/func/" + v + "&" + jarName.split('/').toList.last + "'>" + k + "</a>(")
          }
          Some(body)

        case None => None
      }

    }

    class MethodInvocationVisitor(invokeMap: scala.collection.mutable.Map[String, String]) extends ASTVisitor {
      override def visit(node: MethodInvocation) = {
        val name = node.getName.getFullyQualifiedName
        invokeMap += (name -> Option(node.resolveMethodBinding()).map { x =>
          x.getDeclaringClass.getPackage.getName + "." + x.getDeclaringClass.getName + "." + name
        }.getOrElse(""))
        super.visit(node)
      }
    }
  }


  def indexingFinished(jar: String) {
    val fw = new FileWriter(new File(jarListBackup), true)
    try {
      fw.append(jar + "\n")
      fw.flush()
    } finally {
      fw.close()
    }
  }

}

object MapIndexer extends IndexerService {
  val jarDir        = ConfigFactory.load.getString("repoPath")
  val jarListBackup = ConfigFactory.load.getString("jarListBackup")
  var jarsToIndex   = jars.length

  def addFunc(fName: String, f: Func) {
    Functions.getFunc(fName, f.jarName) match {
      case Some(x) => if (x.isEmpty) Functions.add(fName, f)
      case None => Functions.add(fName, f)
    }
  }

  def persistIndex = Functions.store

  def init {
    Functions.load
    try {
      val files = io.Source.fromFile(cachedJars).getLines
      jars foreach { x: java.io.File =>
        if (!files.contains(x.getName)) {
          future((index(x.getPath)))
        } else {
          jarsToIndex -= 1
        }
      }
    } catch {
      case e: FileNotFoundException =>
        jars foreach(x => future((index(x.getPath))))
    }
  }
}