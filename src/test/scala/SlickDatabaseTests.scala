import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import storage.SlickDatabase

import scala.slick.driver.JdbcDriver

@RunWith(classOf[JUnitRunner])
class SlickDatabaseTests extends FlatSpec with BeforeAndAfterAll with Matchers{

  val dealDatabase = new SlickDatabase{
    val db = scala.slick.jdbc.JdbcBackend.Database.forURL("jdbc:db2://localhost:50000/TEST_DB:user=db2admin;password=password;", driver="com.ibm.db2.jcc.DB2Driver")
  }

  implicit val session = dealDatabase.createSession

  val numQuotesPerEvent = 3

  override def beforeAll(): Unit = {
    dealDatabase.clearAllDealQuoteMappings
    dealDatabase.clearAllQuoteEventMappings
    dealDatabase.clearAllEvents
    val newEventType = "New"
    val resizeEventTypePrefix = "Resize "
    val numEventsPerDeal = 3
    (1 to 10).foreach(index => {
      val eventIdPrefix = s"event_${index}"
      var quote_prefix = s"quote_${index}"
      val deal_id = s"deal_${index}"
      (1 to numEventsPerDeal).foreach(sub => {
        val eventId = eventIdPrefix + s"_${sub}"
        dealDatabase.addEvent(eventId, if(sub == 1) newEventType else resizeEventTypePrefix + s"${sub - 1}", deal_id)
        (1 to numQuotesPerEvent).foreach(evl => {
          val quote_id = quote_prefix + s"_${sub}_${evl}"
          dealDatabase.addQuoteEventMapping(quote_id, eventId)
          dealDatabase.addDealQuoteMapping(deal_id, quote_id, evl % numQuotesPerEvent == 0)
        })
      })
    })
  }

  override def afterAll(): Unit = {
//    dealDatabase.clearAllDealQuoteMappings
//    dealDatabase.clearAllQuoteEventMappings
//    dealDatabase.clearAllEvents
  }

  it should "fetch all quotes related to a given quote by same event id and same deal" in {
    val aQuoteId = "quote_1_2_3"
    val result = dealDatabase.getRelatedQuotes(aQuoteId)
    result.size shouldBe numQuotesPerEvent
    result.map(_.dealId).toSet.size shouldBe 1 //all the quotes have same deal id
    var eventId = dealDatabase.getQuoteEventMapping(aQuoteId).map(_.eventId)
    eventId.isDefined shouldBe true
    result.map(_.quoteId).map(quoteId => {
      dealDatabase.getQuoteEventMapping(quoteId).map(_.eventId) shouldBe eventId //all quotes should have same eventId
    })
  }

  it should "unset all flags for quotes which have same eventid as given quote's event id" in {
    val aQuoteId = "quote_1_2_3"
    val result = dealDatabase.clearRelatedQuotesIsLatestFlag(aQuoteId)
    result shouldBe 1
    val relatedQuotes = dealDatabase.getRelatedQuotes(aQuoteId)
    relatedQuotes.foreach(relatedQuote => {
      relatedQuote.isLatest shouldBe false
    })
  }

}
