package test

import java.util.jar.JarFile
import models.{Func, FuncMap}

object DataProvider {

	def getFuncs = io.Source.fromFile("/home/eklavya/funcs").getLines().toList map { x =>
		val a = x.split(';')
		(a.head, a.last)
	}

	def getTrace = io.Source.fromFile("/home/eklavya/trace").getLines().toList

	def getJar   = new JarFile("test/hibernate-3.2.0.ga-sources.jar")

	object TestFunctions {
		var funcMap: FuncMap = _

		def load = {
			funcMap = new FuncMap
		}

		def add(fqn: String, f: Func) = {
			funcMap.addBinding(fqn, f)
		}

		def holds(conds: List[(String, String)]) = {
			!conds.exists{case(f, l) => !funcMap.entryExists(f, x => (l.toInt >= x.start) && (l.toInt <= x.end))}
		}

		def findFunc(f: String, l: Int) = {
			funcMap(f).filter(x => (l >= x.start) && (l <= x.end)).toList
		}

		def getFunc(f: String, jarName: String) = {
			funcMap.get(f).map(x => x.filter(_.jarName == jarName).head)
		}

		def store {
			funcMap.store
		}
	}

	def constructMap {
		TestFunctions.load
		TestFunctions.add("test.FunctionMap.func", new Func("test.FunctionMap.func", 1, 10, Some("body"), "someJar"))
	}

}
