package models

import com.typesafe.config.ConfigFactory
import java.io.{File, InputStream, FileWriter, FileNotFoundException}
import java.util.jar.JarFile
import org.eclipse.jdt.core.dom._
import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.io.Codec


object Indexer {

  def index(jarPath: String) {
    val jarFile = new JarFile(jarPath)
    jarFile.entries.filter(_.getName.contains(".java")) foreach { jFile =>
      extractMethods(jFile.getName, jarFile.getInputStream(jFile), jarPath)
    }
  }

  /**
   * Extracting Methods From each .java entry in the JAR.
   *
   * @param jFileName *.java name
   * @param is JAR as inputstream
   * @param jarPath JAR path
   */
  def extractMethods(jFileName: String, is: InputStream, jarPath: String) = {
    val parser = ASTParser.newParser(AST.JLS4)
    // or use codec "latin1" !!!important
    parser.setSource(io.Source.fromInputStream(is)(Codec.ISO8859).toSeq.toArray)
    parser.setKind(ASTParser.K_COMPILATION_UNIT)
    parser.setEnvironment(null, Array(jarPath), null, true)
    parser.setUnitName("")
    parser.setResolveBindings(true)
    val cu = parser.createAST(null).asInstanceOf[CompilationUnit]
    val fqn = cu.getPackage.getName + "." + jFileName.split('/').last.split('.').head
    cu.accept(new MethodVisitor(cu, fqn, jarPath))
  }


  class MethodVisitor(cu: CompilationUnit, fqn: String, jarName: String) extends ASTVisitor {
    override def visit(node: MethodDeclaration) = {
      val start = Start(cu.getLineNumber(node.getStartPosition))
      val end = End(cu.getLineNumber(node.getStartPosition + node.getLength))
      val body = Option(node.getBody)
      val fqn = getFullName(node)
      val fqnWithParamsAndReturn = fqn + "(" + node.parameters().map { x =>
        x.toString.split(" ").head
      }.mkString(",") + "):" + node.getReturnType2

      val fi = FuncInfo(fqnWithParamsAndReturn, start, end, body, jarName.split('/').toList.last)
      Funcs.add(fi)

      super.visit(node)
    }

    def getFullName(node: MethodDeclaration): String = {
      Option(node.resolveBinding()).map { x =>
        if(fqn.equals(s"${x.getDeclaringClass.getPackage.getName}.${x.getDeclaringClass.getName}"))
          s"$fqn.${x.getName}"
        else
          s"$fqn.${x.getDeclaringClass.getName}.${x.getName}"
      }.getOrElse(s"$fqn.${node.getName.getFullyQualifiedName}")
    }
}