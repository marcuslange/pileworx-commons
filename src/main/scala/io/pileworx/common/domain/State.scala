package io.pileworx.common.domain

trait State

trait Entity[TID] extends State {
  val id:TID
}

case object Uninitialized extends State
case object Removed extends State