// lchannels - session programming in Scala
// Copyright (c) 2017, Alceste Scalas and Imperial College London
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
//
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

/** Multiparty protocol classes for role Seller in three-buyer example.
 *  The classes in this package have been automatically generated from the
 *  multiparty game protocol:
 *  https://github.com/alcestes/scribble-java/blob/linear-channels/modules/linmp-scala/src/test/scrib/ThreeBuyer.scr
 *  
 * @author Alceste Scalas <alceste.scalas@imperial.ac.uk> */
package lchannels.examples.threebuyer.protocol.seller

import scala.concurrent.duration.Duration
import lchannels._
import lchannels.examples.threebuyer.protocol.binary
import java.time.ZonedDateTime

// Local type for role Seller:
//    Alice&{Title(String).Alice⊕{QuoteA(Int).Bob⊕{QuoteB(Int).Bob&{OkS(Unit).Bob&{Address(String).Bob⊕{Deliver(ZonedDateTime).end}}, QuitS(Unit).end}}}}

// Input message types for multiparty sessions (autogenerated by Scribble)
case class Title(p: String, cont: MPQuoteA)
sealed abstract class MsgMPOkSOrQuitS
case class OkS(p: Unit, cont: MPAddress) extends MsgMPOkSOrQuitS
case class QuitS(p: Unit) extends MsgMPOkSOrQuitS
case class Address(p: String, cont: MPDeliver)

// Output message types for multiparty sessions (autogenerated by Scribble)
case class QuoteA(p: Int)
case class QuoteB(p: Int)
case class Deliver(p: ZonedDateTime)

// Multiparty session classes (autogenerated by Scribble)
case class MPTitle(alice: In[binary.Title], bob: Out[binary.QuoteB]) {
  def receive(implicit timeout: Duration = Duration.Inf) = {
    alice.receive(timeout) match {
      case m @ binary.Title(p) => {
        Title(p, MPQuoteA(m.cont, bob))
      }
    }
  }
}

case class MPQuoteA(alice: Out[binary.QuoteA], bob: Out[binary.QuoteB]) {
  def send(v: QuoteA) = {
    val cnt = alice ! binary.QuoteA(v.p)
    MPQuoteB(cnt, bob)
  }
}

case class MPQuoteB(alice: Unit, bob: Out[binary.QuoteB]) {
  def send(v: QuoteB) = {
    val cnt = bob !! binary.QuoteB(v.p)_
    MPOkSOrQuitS(alice, cnt)
  }
}

case class MPOkSOrQuitS(alice: Unit, bob: In[binary.OkSOrQuitS]) {
  def receive(implicit timeout: Duration = Duration.Inf) = {
    bob.receive(timeout) match {
      case m @ binary.OkS(p) => {
        OkS(p, MPAddress(alice, m.cont))
      }
      case m @ binary.QuitS(p) => {
        QuitS(p)
      }
    }
  }
}

case class MPAddress(alice: Unit, bob: In[binary.Address]) {
  def receive(implicit timeout: Duration = Duration.Inf) = {
    bob.receive(timeout) match {
      case m @ binary.Address(p) => {
        Address(p, MPDeliver(alice, m.cont))
      }
    }
  }
}

case class MPDeliver(alice: Unit, bob: Out[binary.Deliver]) {
  def send(v: Deliver) = {
    val cnt = bob ! binary.Deliver(v.p)
    ()
  }
}