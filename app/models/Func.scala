package models

import java.io.{ObjectInputStream, ObjectOutputStream, ByteArrayOutputStream}
import java.sql.Blob
import javax.sql.rowset.serial.SerialBlob

import org.eclipse.jdt.core.dom.Block
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._

case class Start(value: Int) extends AnyVal with MappedTo[Int]

case class End(value: Int) extends AnyVal with MappedTo[Int]

case class FuncInfo(name: String, start: Start, end: End, body: Option[Block], jarName: String)


class FuncInfos(tag: Tag) extends Table[FuncInfo](tag, "Functions") {

  implicit val blockColumnType = MappedColumnType.base[Block, Blob]({ block =>
    val b = new ByteArrayOutputStream
    val out = new ObjectOutputStream(b)
    out.writeObject(block)
    out.flush
    new SerialBlob(b.toByteArray)
  }, { b =>
    val in = new ObjectInputStream(b.getBinaryStream)
    in.readObject().asInstanceOf[Block]
  })

  def name = column[String]("Name")

  def jar = column[String]("Jar")

  def start = column[Start]("Start")

  def end = column[End]("End")

  def body = column[Block]("Body", O.Nullable)

  def pk = primaryKey("pk", (name, jar))

  def * = (name, jar, start, end, body.?) <>(FuncInfo.tupled, FuncInfo.unapply)
}

object Funcs extends TableQuery(new FuncInfos(_)) {
  def db = Database.forDataSource(DB.getDataSource())

  def add(f: FuncInfo) = db withDynSession {
    this.insert(f)
  }

  def ofName(name: String): List[FuncInfo] = db withDynSession {
    this.findBy(_.name).apply(name).list
  }

  def ofNameFromJar(name: String, jar: String): Option[FuncInfo] = db withDynSession {
    this.filter(f => f.name === name && f.jar === jar).list.headOption
  }
}