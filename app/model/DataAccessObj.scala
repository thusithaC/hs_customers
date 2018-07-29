package model

import java.sql.{ResultSet, SQLException}

import play.api.Logger
import play.api.db._
import play.api.libs.json._
import play.api.Play.current

import scala.annotation.tailrec

/**
  * Created by thusitha on 7/25/18.
  */
object DataAccessObj {

  val dbname = "customers"

  def getAllCustomerAccounts(): List[CustomerAccount] = {
    val query = "SELECT customerId, forename, surname, accountId FROM customer_info"
    getItemsFromDatsource(query)
  }

  def getAllCustomers(): List[Customer] = {
    val query = "SELECT customerId, forename, surname, accountId FROM customer_info"
    val customerAccounts = getItemsFromDatsource(query)
    val accountsGrouped = customerAccounts.groupBy(_.customerId)

    val customers = for ((customerId, customerAccountList) <- accountsGrouped) yield {
      val accounts = customerAccountList.map(_.account)
      val sample = customerAccountList.head
      Customer(sample.customerId, sample.forename, sample.surname, accounts)
    }
    customers.toList
  }

  def getAllAccountsForCustomer(customerId: String) : List[String] = {

    def getaccountsFromResult(accList:List[String], rs: ResultSet) : List[String] = {
      val hasNext = rs.next()
      if (!hasNext) accList else getaccountsFromResult(rs.getString("accountId")::accList, rs)
    }

    val query = s"SELECT accountId FROM customer_info where customerId = \'${customerId}\'"

    val conn = DB.getConnection(dbname)
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(query)
      getaccountsFromResult(Nil, rs)
    } finally {
      conn.close()
    }
  }

  def getCustomer(customerId: String) : Customer = {
    val query = s"SELECT customerId, forename, surname, accountId FROM customer_info where customerId = \'${customerId}\'"
    val customerAccounts = getItemsFromDatsource(query)
    if (customerAccounts.isEmpty)
      null
    else {
      val accounts = customerAccounts.map(_.account)
      val sample = customerAccounts.head
      Customer(customerId, sample.forename, sample.surname, accounts)
    }
  }

  def insertCustomerAccountJson(json:JsValue) : Boolean= {
    json.validate[CustomerAccount] match {
      case c: JsSuccess[CustomerAccount] => {
        val customerAccount: CustomerAccount = c.get
        val rowsAffected = insertCustomerAccountIntoDb(customerAccount)
        if (rowsAffected > 0){
          Logger.info("successfully entered transaction " + customerAccount )
          true
        }
        else {
          Logger.error("Database Error couldnt enter transaction " + customerAccount)
          false
        }
      }
      case e: JsError => {
        Logger.error("Error parsing transaction " +  json)
        false
      }
    }
  }

  private def insertCustomerAccountIntoDb(customerAccount: CustomerAccount) : Int = {
    val conn = DB.getConnection(dbname)
    try {
      val stmt = conn.createStatement
      val insertString = s"INSERT INTO customer_info (customerId, forename, surname, accountId) VALUES (\'${customerAccount.customerId}\', \'${customerAccount.forename}\', \'${customerAccount.surname}\', \'${customerAccount.account}\')"
      Logger.info(insertString)
      stmt.executeUpdate(insertString)
    } finally {
      conn.close()
    }
  }


  def insertCustomerJson(json:JsValue) : Boolean= {
    json.validate[Customer] match {
      case c: JsSuccess[Customer] => {
        val customer: Customer = c.get

        val customerAccounts = for (account <- customer.accounts) yield{
          CustomerAccount(customer.customerId, customer.forename, customer.surname, account)
        }

        val rowsAffected = insertCustomerAccountsIntoDb(customerAccounts)
        if (rowsAffected > 0){
          Logger.info("successfully entered transaction " + customer )
          true
        }
        else {
          Logger.error("Database Error couldnt enter transaction " + customer)
          false
        }
      }
      case e: JsError => {
        Logger.error("Error parsing transaction " +  json)
        false
      }
    }
  }

  private def insertCustomerAccountsIntoDb(customerAccounts: List[CustomerAccount]) : Int = {

    def insertHelper(inserted:Int, customerAccounts: List[CustomerAccount]) : Int = customerAccounts match {
      case Nil => inserted
      case head::tail => insertHelper(insertCustomerAccountIntoDb(head)+inserted, tail)
    }
    insertHelper(0, customerAccounts)
  }

  private def createCustomerAccount(rs:ResultSet) : Option[CustomerAccount] = {
    val id = rs.getString("customerId")
    if (rs.getString("customerId").trim.isEmpty)
      None
    else
      Some(CustomerAccount(rs.getString("customerId").trim, rs.getString("forename").trim, rs.getString("surname").trim, rs.getString("accountId").trim))
  }

  @tailrec
  private def getItemsFromResult(tList:List[CustomerAccount], rs: ResultSet) : List[CustomerAccount] = {
    val hasNext = rs.next()
    if (!hasNext) tList else {
      val customerAccountOption = createCustomerAccount(rs)
      customerAccountOption match  {
        case None => getItemsFromResult(tList, rs)
        case Some(c) => getItemsFromResult(c::tList, rs)
      }
    }
  }

  private def getItemFromResult(rs: ResultSet) : CustomerAccount = {
    if (rs.first()) createCustomerAccount(rs) match {
      case None => null
      case Some(c) => c
    }
    else
      null
  }


  private def getItemsFromDatsource(query: String) : List[CustomerAccount] = {
    val conn = DB.getConnection(dbname)
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(query)
      getItemsFromResult(Nil, rs)
    } finally {
      conn.close()
    }
  }

  private def getItemFromDatsource(query: String) : CustomerAccount = {
    val conn = DB.getConnection(dbname)
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(query)
      getItemFromResult(rs)
    } finally {
      conn.close()
    }
  }
}
