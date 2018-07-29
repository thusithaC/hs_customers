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

  def customerAccounts = Action {
    Logger.info("Request received: customerAccounts ")
    val customerAccounts = DataAccessObj.getAllCustomerAccounts()
    if(!customerAccounts.isEmpty) Ok(Json.toJson(customerAccounts))
    else {
      Logger.error("Error processing request : customerAccounts")
      NotFound
    }
  }

  def customers = Action {
    Logger.info("Request received: customers ")
    val customers = DataAccessObj.getAllCustomers()
    if(!customers.isEmpty) Ok(Json.toJson(customers))
    else {
      Logger.error("Error processing request : customers")
      NotFound
    }
  }

  def accountsForCustomer(customerId: String) = Action {
    Logger.info("Request received: accountsForCustomer ")
    val accounts = DataAccessObj.getAllAccountsForCustomer(customerId)
    if(!accounts.isEmpty) Ok(Json.toJson(accounts))
    else {
      Logger.error("Error processing request : accountsForCustomer")
      NotFound
    }
  }

  def customer(customerId: String) = Action {
    Logger.info("Request received: customerById ")
    val customer = DataAccessObj.getCustomer(customerId)
    if(null != customer) Ok(Json.toJson(customer))
    else {
      Logger.error("Error processing request : customerById")
      NotFound
    }
  }

  def createNewCustomerAccount = Action { request =>
    Logger.info("Request to create createNewCustomerAccount received + " + request.body.asText)
    val json = request.body.asJson.get
    if (DataAccessObj.insertCustomerAccountJson(json)) Ok
    else {
      Logger.error("Error processing request : createNewCustomerAccount")
      BadRequest
    }
  }

  def createNewCustomer = Action { request =>
    Logger.info("Request to create createNewCustomer received + " + request.body.asText)
    val json = request.body.asJson.get
    if (DataAccessObj.insertCustomerJson(json)) Ok
    else {
      Logger.error("Error processing request : createNewCustomer")
      BadRequest
    }
  }

}
