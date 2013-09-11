package test

import java.util.jar.JarFile

object DataProvider {

  def getFuncs = io.Source.fromFile("/home/eklavya/funcs").getLines().toList map { x =>
    val a = x.split(';')
    (a.head, a.last)
  }

  def getTrace = io.Source.fromFile("/home/eklavya/trace").getLines().toList

  def getJar   = new JarFile("/home/eklavya/Downloads/jars/hibernate-3.2.0.ga-sources.jar")

}
