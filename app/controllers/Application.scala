package controllers

import model.{DataAccessObj, Customer}
import play.api._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import play.api.db._
import play.api.libs.json._
import model.Customer._



object Application extends Controller {

  def index = Action {
    Ok(views.html.index(null))
  }

  def customers = Action {
    println("Request received: transactions ")
    val customers = DataAccessObj.getAllCustomers()
    if(!customers.isEmpty) Ok(Json.toJson(customers)) else NotFound
  }

  def transactionByAccount(accountId : String) = Action {
    println("Request received: transactionByAccount " + accountId)
    if (null != accountId) {
      val transactions = DataAccessObj.getTransactionsForAccount(accountId)
      Ok(Json.toJson(transactions))
    }
    else {
      NotFound
    }
  }

  def allAccounts() = Action {
    println("Request received: allAccounts ")
    val accounts = DataAccessObj.getAllAccounts()
    if(!accounts.isEmpty) Ok(Json.toJson(accounts)) else NotFound
  }


  def transactionById(transactionId : String) = Action {
    println("Request received: transactionById " + transactionId)
    if (null != transactionId) {
      val transaction = DataAccessObj.getTransactionsForId(transactionId)
      if (null != transaction) Ok(Json.toJson(transaction)) else NotFound
    }
    else {
      NotFound
    }
  }

  def createNew = Action { request =>
    println("Request to create object received + " + request.body.asText)
    val json = request.body.asJson.get
    println("Request body " + json)
    if (DataAccessObj.insertTransactionJson(json)) Ok else BadRequest
  }

}
