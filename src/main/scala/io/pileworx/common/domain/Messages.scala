package io.pileworx.common.domain

trait Message

case class Error(message:String) extends Message
case class Acknowledge(id:String) extends Message