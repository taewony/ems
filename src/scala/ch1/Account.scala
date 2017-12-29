package frdomain.ch1

import org.joda.time.DateTime
import scala.util.Try

object Types {
  type Amount = BigDecimal
}
import Types._

trait Account {
  def no: String
  def name: String
  def openedOn: DateTime
  def closedOn: Option[DateTime]
}

case class CheckingAccount(
  no: String, 
  name: String, 
  openedOn: DateTime, 
  closedOn: Option[DateTime]) extends Account

case class SavingsAccount(
  no: String, 
  name: String, 
  openedOn: DateTime, 
  rateOfInterest: BigDecimal, //
  minBalance: BigDecimal,
  closedOn: Option[DateTime]) extends Account

trait AccountService {
  def debit(a: Account, amount:Amount): Try[Account] = ???
  def credit(a: Account, amount:Amount): Try[Account] = ???
  def transfer(from: Account, to: Account, amount: Amount): Option[Amount]
}

case class Balance(amount:Amount = 0)

case class AccountBalanced (no:String, name:String,
  openedOn:DateTime, closedOn: Option[DateTime], balance: Balance = Balance()) extends Account 

case class LoanAccount(
  no: String, 
  name: String, 
  openedOn: DateTime, 
  rateOfInterest: Option[BigDecimal],
  emi: Option[BigDecimal],
  status: AccountStatus = Applied, 
  closedOn: Option[DateTime]) extends Account

sealed trait AccountStatus
case object Applied extends AccountStatus
case object Approved extends AccountStatus

case class Credentials(name: String, address: String, age: Int)

import scalaz._
import Scalaz._

trait LoanProcessing {
  def applyForLoan[A <: LoanAccount](name: String, dateOfApplication: DateTime): Kleisli[Option, Credentials, A]
  def approve[A <: LoanAccount](zone: String): Kleisli[Option, A, A]
  def finalizeEMI[A <: LoanAccount](tenure: Int): Kleisli[Option, A, A]

  def loanProcessing(name: String, dateOfApplication: DateTime, zone: String, tenure: Int) = 
    applyForLoan(name, dateOfApplication) >=> approve[LoanAccount](zone) >=> finalizeEMI(tenure)
}

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello")
  }
}