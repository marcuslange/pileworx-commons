package io.pileworx.common.persistence

import com.couchbase.client.protocol.views.Query
import org.reactivecouchbase.ReactiveCouchbaseDriver
import play.api.libs.json.{Reads, Writes}

import scala.concurrent.{ExecutionContext, Future}

case class CouchbaseQuery(
  docName:String,
  viewName:String,
  query:Query)

trait Couchbase extends Persistence {

  lazy val bucket = ReactiveCouchbaseDriver().bucket("default")

  def save[T <: Identifiable[TID], TID](instance:T)(implicit w: Writes[T], ec: ExecutionContext):Future[Outcome] = {
    val stringId = instance.id.asInstanceOf[String]
    for {
      result <- bucket.set[T](stringId, instance)
    } yield {
      Outcome(
        ok = result.isSuccess,
        error = Some(result.getMessage))
    }
  }

  def delete[TID](id:TID)(implicit ec: ExecutionContext): Future[Outcome] = {
    val stringId = id.asInstanceOf[String]
    for {
      result <- bucket.delete(stringId)
    } yield {
      Outcome(
        ok = result.isSuccess,
        error = Some(result.getMessage))
    }
  }

  def find[T <: Identifiable[TID], TID](id:TID)(implicit r: Reads[T], ec: ExecutionContext): Future[Option[T]] =  {
    val stringId = id.asInstanceOf[String]
    bucket.get[T](stringId)
  }

  def findAll[T <: Identifiable[TID], TID](query: AnyRef)(implicit r: Reads[T], ec: ExecutionContext): Future[Traversable[T]] = {
    val viewQuery = query.asInstanceOf[CouchbaseQuery]
    bucket.find(viewQuery.docName, viewQuery.viewName)(viewQuery.query)
  }
}