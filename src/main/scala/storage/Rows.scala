package storage

object Rows{
  final case class QuoteEventMappingRow(quoteId: String, eventId: String)
  final case class DealQuoteMappingRow(dealId: String, quoteId: String, isLatest: Boolean)
  final case class EventRow(eventId: String, eventType: String, dealId: String)
}
