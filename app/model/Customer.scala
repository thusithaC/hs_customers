package model

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Created by thusitha on 7/25/18.
  * customerId,forename,surname
  */

case class Customer(customerId : String,
                    forename : String,
                    surname : String,
                    accounts: List[String])

case class CustomerAccount(customerId : String,
                          forename : String,
                          surname : String,
                          account: String)

object Customer {
  implicit val customerReads: Reads[Customer] = (
    (JsPath \ "customerId").read[String] and
      (JsPath \ "forename").read[String] and
      (JsPath \ "surname").read[String] and
      (JsPath \ "accounts").read[List[String]]
    )(Customer.apply _)

  implicit val customerWrites: Writes[Customer] = (

    (JsPath \ "customerId").write[String] and
      (JsPath \ "forename").write[String]and
      (JsPath \ "surname").write[String] and
      (JsPath \ "accounts").write[List[String]]
    )(unlift(Customer.unapply))
}


object CustomerAccount {
  implicit val customerReads: Reads[CustomerAccount] = (
    (JsPath \ "customerId").read[String] and
      (JsPath \ "forename").read[String] and
      (JsPath \ "surname").read[String] and
      (JsPath \ "account").read[String]
    )(CustomerAccount.apply _)

  implicit val customerWrites: Writes[CustomerAccount] = (

    (JsPath \ "customerId").write[String] and
      (JsPath \ "forename").write[String]and
      (JsPath \ "surname").write[String] and
      (JsPath \ "account").write[String]
    )(unlift(CustomerAccount.unapply))
}





