package io.pileworx.common.persistence

import play.api.libs.json.{Reads, Writes}
import scala.concurrent.{ExecutionContext, Future}

case class Outcome(
  ok:Boolean,
  error:Option[String])

trait Identifiable[T] {
  val id: T
  def idAsString: String
}

trait Persistence {
  def table: String
  def save[T <: Identifiable[TID], TID](instance:T)(implicit w: Writes[T], ec: ExecutionContext):Future[Outcome]
  def delete[TID](id:TID)(implicit ec: ExecutionContext): Future[Outcome]
  def find[T <: Identifiable[TID], TID](id:TID)(implicit r: Reads[T], ec: ExecutionContext): Future[Option[T]]
  def findAll[T <: Identifiable[TID], TID](query: AnyRef)(implicit r: Reads[T], ec: ExecutionContext): Future[Traversable[T]]
}