package storage

import storage.Rows._

import scala.slick.lifted.ProvenShape

trait Tables{

  val profile: scala.slick.driver.JdbcProfile
  import profile.simple._

  class QuoteEventMapping(tag: Tag) extends Table[QuoteEventMappingRow](tag, "QUOTE_EVENT_REF") {
    def quoteId = column[String]("QUOTE_ID", O.PrimaryKey)
    def eventId = column[String]("EVENT_ID", O.NotNull)
    def * : ProvenShape[QuoteEventMappingRow] = (quoteId, eventId) <> (QuoteEventMappingRow.tupled, QuoteEventMappingRow.unapply)
  }

  class DealQuoteMapping(tag: Tag) extends Table[DealQuoteMappingRow](tag, "DEAL_QUOTE_REF") {
    def dealId = column[String]("DEAL_ID", O.PrimaryKey)
    def quoteId = column[String]("QUOTE_ID", O.NotNull)
    def isLatest = column[Boolean]("IS_LATEST", O.NotNull)
    def * : ProvenShape[DealQuoteMappingRow] = (dealId, quoteId, isLatest) <> (DealQuoteMappingRow.tupled, DealQuoteMappingRow.unapply)
  }

  class Event(tag: Tag) extends Table[EventRow](tag, "EVENT") {
    def eventId = column[String]("EVENT_ID", O.PrimaryKey)
    def eventType = column[String]("EVENT_TYPE", O.NotNull)
    def dealId = column[String]("DEAL_ID", O.NotNull)
    def * : ProvenShape[EventRow] = (eventId, eventType, dealId) <> (EventRow.tupled, EventRow.unapply)
  }

  val quoteEventMappings = TableQuery[QuoteEventMapping]
  val dealQuoteMappings = TableQuery[DealQuoteMapping]
  val events = TableQuery[Event]
}
