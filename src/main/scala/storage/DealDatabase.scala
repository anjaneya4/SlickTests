package storage

trait DealDatabase {
  type DBSession
  import Rows._

  def createSession: DBSession
  def close(s: DBSession): Unit

  def commit(s: DBSession): Unit

  def addEvent(eventId: String, eventType: String, dealId: String)(implicit session: DBSession): Unit
  def getEvent(eventId: String)(implicit session: DBSession): Option[EventRow]
  def clearEvent(eventId: String)(implicit session: DBSession): Unit
  def clearAllEvents(implicit session: DBSession): Unit

  def addDealQuoteMapping(dealId: String, quoteId: String, isLatest: Boolean)(implicit session: DBSession): Unit
  def getDealQuoteMapping(dealId: String)(implicit session: DBSession): Option[DealQuoteMappingRow]
  def clearDealQuoteMapping(dealId: String)(implicit session: DBSession): Unit
  def clearAllDealQuoteMappings(implicit session: DBSession): Unit

  def addQuoteEventMapping(dealId: String, eventId: String)(implicit session: DBSession): Unit
  def getQuoteEventMapping(dealId: String)(implicit session: DBSession): Option[QuoteEventMappingRow]
  def clearQuoteEventMapping(dealId: String)(implicit session: DBSession): Unit
  def clearAllQuoteEventMappings(implicit session: DBSession): Unit

  //related by event id
  def clearRelatedQuotesIsLatestFlag(quoteId: String)(implicit session: DBSession): Int
  def getRelatedQuotes(quoteId: String)(implicit session: DBSession): List[DealQuoteMappingRow]

}
