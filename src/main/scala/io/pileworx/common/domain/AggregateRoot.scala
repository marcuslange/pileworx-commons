package io.pileworx.common.domain

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, SnapshotMetadata, SnapshotOffer}

trait AggregateRoot extends PersistentActor with ActorLogging {

  override def persistenceId:String

  protected var state:State = _

  protected def updateState(evt: Event): Unit

  protected def afterEventPersisted(evt: Event): Unit = {
    updateAndRespond(evt)
    publish(evt)
  }

  private def updateAndRespond(evt: Event): Unit = {
    updateState(evt)
    respond(evt)
  }

  protected def respond(response: AnyRef): Unit = {
    sender() ! response
    context.parent ! Acknowledge(persistenceId)
  }

  private def publish(event: Event) = context.system.eventStream.publish(event)

  override val receiveRecover: Receive = {
    case evt: Event =>
      updateState(evt)
    case SnapshotOffer(metadata, state: State) =>
      restoreFromSnapshot(metadata, state)
      log.debug("Recovering Aggregate from snapshot.")
  }

  protected def restoreFromSnapshot(metadata: SnapshotMetadata, state: State)
}