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
    println("Request received: customerAccounts ")
    val customerAccounts = DataAccessObj.getAllCustomerAccounts()
    if(!customerAccounts.isEmpty) Ok(Json.toJson(customerAccounts)) else NotFound
  }

  def customers = Action {
    println("Request received: customers ")
    val customers = DataAccessObj.getAllCustomers()
    if(!customers.isEmpty) Ok(Json.toJson(customers)) else NotFound
  }

  def accountsForCustomer(customerId: String) = Action {
    println("Request received: accountsForCustomer ")
    val accounts = DataAccessObj.getAllAccountsForCustomer(customerId)
    if(!accounts.isEmpty) Ok(Json.toJson(accounts)) else NotFound
  }

  def customer(customerId: String) = Action {
    println("Request received: customer ")
    val customer = DataAccessObj.getCustomer(customerId)
    if(null != customer) Ok(Json.toJson(customer)) else NotFound
  }

  def createNewCustomerAccount = Action { request =>
    println("Request to create createNewCustomerAccount received + " + request.body.asText)
    val json = request.body.asJson.get
    if (DataAccessObj.insertCustomerAccountJson(json)) Ok else BadRequest
  }

  def createNewCustomer = Action { request =>
    println("Request to create createNewCustomer received + " + request.body.asText)
    val json = request.body.asJson.get
    if (DataAccessObj.insertCustomerJson(json)) Ok else BadRequest
  }

}
