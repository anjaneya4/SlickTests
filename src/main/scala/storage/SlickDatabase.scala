package storage

import storage.Rows.{DealQuoteMappingRow, EventRow, QuoteEventMappingRow}

import scala.slick.driver.JdbcDriver
import scala.slick.jdbc.JdbcBackend

trait SlickDatabase extends Tables with DealDatabase{

  type DBSession = JdbcBackend.Session

  val profile: scala.slick.driver.JdbcProfile = JdbcDriver.profile

  import profile.simple._

  def db: JdbcBackend.DatabaseDef
  def createSession = db.createSession()
  def close(s: JdbcBackend.Session) = s.close()
  def commit(s: JdbcBackend.Session) = s.conn.commit()

  def addEvent(eventId: String, eventType: String, dealId: String)(implicit session: DBSession): Unit ={
    events.insert(EventRow(eventId, eventType, dealId))
  }
  def getEvent(eventId: String)(implicit session: DBSession): Option[EventRow] = {
    events.filter(_.eventId === eventId).firstOption
  }
  def clearEvent(eventId: String)(implicit session: DBSession): Unit = {
    events.filter(_.eventId === eventId).delete
  }

  def clearAllEvents(implicit session: DBSession): Unit = {
    events.delete
  }

  def addDealQuoteMapping(dealId: String, quoteId: String, isLatest: Boolean)(implicit session: DBSession): Unit = {
    dealQuoteMappings.insert(DealQuoteMappingRow(dealId, quoteId, isLatest))
  }
  def getDealQuoteMapping(dealId: String)(implicit session: DBSession): Option[DealQuoteMappingRow] = {
    dealQuoteMappings.filter(_.dealId === dealId).firstOption
  }
  def clearDealQuoteMapping(dealId: String)(implicit session: DBSession): Unit = {
    dealQuoteMappings.filter(_.dealId === dealId).delete
  }

  def clearAllDealQuoteMappings(implicit session: DBSession): Unit = {
    dealQuoteMappings.delete
  }

  def addQuoteEventMapping(dealId: String, eventId: String)(implicit session: DBSession): Unit = {
    quoteEventMappings.insert(QuoteEventMappingRow(dealId, eventId))
  }
  def getQuoteEventMapping(quoteId: String)(implicit session: DBSession): Option[QuoteEventMappingRow] = {
    quoteEventMappings.filter(_.quoteId === quoteId).firstOption
  }
  def clearQuoteEventMapping(quoteId: String)(implicit session: DBSession): Unit = {
    quoteEventMappings.filter(_.quoteId === quoteId).delete
  }
  def clearAllQuoteEventMappings(implicit session: DBSession): Unit = {
    quoteEventMappings.delete
  }

  def clearRelatedQuotesIsLatestFlag(quoteId: String)(implicit session: DBSession): Int = {
    val eventId = quoteEventMappings.filter(_.quoteId === quoteId).map(_.eventId)
    val relatedQuoteIds = quoteEventMappings.filter(_.eventId in eventId).map(_.quoteId)
    dealQuoteMappings.filter(dq => (dq.quoteId in relatedQuoteIds) && (dq.isLatest === true)).map(_.isLatest).update(false)
  }

  def getRelatedQuotes(quoteId: String)(implicit session: DBSession): List[DealQuoteMappingRow] = {
    val eventId = quoteEventMappings.filter(_.quoteId === quoteId).map(_.eventId)
    val relatedQuoteIds = quoteEventMappings.filter(_.eventId in eventId).map(_.quoteId)
    val result = dealQuoteMappings.filter(_.quoteId in relatedQuoteIds).list
    result
  }
}
