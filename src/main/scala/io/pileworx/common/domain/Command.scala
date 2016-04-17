package io.pileworx.common.domain

trait Command

case object Remove extends Command
case object GetState extends Command
case object KillAggregate extends Command