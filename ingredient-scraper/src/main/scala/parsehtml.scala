import data.{abbreviations, seleniumDomains, RawLists}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.openqa.selenium.By
import org.openqa.selenium.remote.{DesiredCapabilities, RemoteWebDriver}
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}

import scala.collection.JavaConverters._

object parsehtml {

  def loadHTMLWithJsoup(u: String): Document = Jsoup.connect(u).timeout(10000).get

  def loadHTMLWithSelenium(u: String, locatorName: String): Document = {
    val driver = new RemoteWebDriver(new java.net.URL("http://172.18.0.2:4444/wd/hub"), DesiredCapabilities.firefox)
    driver.get(u)
    new WebDriverWait(driver, 15).until(
      ExpectedConditions.presenceOfElementLocated(By.className(locatorName))
    )
    val doc = Jsoup.parse(driver.getPageSource)
    driver.quit()
    doc
  }

  def loadHTML(u: String): Document = seleniumDomains.find(d => u contains d.domainName) match {
    case Some(domain) => loadHTMLWithSelenium(u, domain.locatorName)
    case None => loadHTMLWithJsoup(u)
  }

  // TODO: clean HTML based on domain
  def cleanHTML(doc: Document): Document = {
    doc.select("[ng-cloak]").remove()
    doc
  }

  def prepareHTML: String => Document = loadHTML _ andThen cleanHTML

  def replaceAbbreviations(s: String): String =
    abbreviations.foldLeft(s)((a, b) => a.replaceAllLiterally(b._1, b._2))

  def formatText = ((s: String) => s.replace(".", "")) andThen replaceAbbreviations

  def getChildrenText(e: Element): List[String] = e
    .children
    .asScala.toList
    .map(_.text)
    .map(formatText)

  def suspectList(e: Element): Boolean =
    if (e.children.size < 3) false
    else e.children.asScala.toList
      .map { child => (child.tagName, child.className) }
      .groupBy(identity).mapValues(_.size).values
      .max >= e.children.size / 2.toFloat

  def getUnorderedLists(d: Document): RawLists = d
    .getElementsByTag("ul")
    .asScala.toList
    .map(getChildrenText)

  def inferLists(d: Document): RawLists = d
    .getAllElements
    .asScala.toList
    .filter(suspectList)
    .map(getChildrenText)
}
