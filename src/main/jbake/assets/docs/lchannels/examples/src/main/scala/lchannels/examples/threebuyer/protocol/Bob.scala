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

/** Multiparty protocol classes for role Bob in three-buyer example.
 *  The classes in this package have been automatically generated from the
 *  multiparty game protocol:
 *  https://github.com/alcestes/scribble-java/blob/linear-channels/modules/linmp-scala/src/test/scrib/ThreeBuyer.scr
 *  
 * @author Alceste Scalas <alceste.scalas@imperial.ac.uk> */
package lchannels.examples.threebuyer.protocol.bob

import scala.concurrent.duration.Duration
import lchannels._
import lchannels.examples.threebuyer.protocol.binary
import java.time.ZonedDateTime

// Local type for role Bob (in main protocol):
//    Seller&{QuoteB(Int).Alice&{ShareA(Int).Alice⊕{OkA(Unit).Seller⊕{OkS(Unit).Seller⊕{Address(String).Seller&{Deliver(ZonedDateTime).end}}}, QuitA(Unit).Seller⊕{QuitS(Unit).end}}}}

// Input message types for multiparty sessions (autogenerated by Scribble)
case class PlayBob(p: MPQuoteB)
case class QuoteB(p: Int, cont: MPShareA)
case class ShareA(p: Int, cont: MPOkAOrQuitA)
case class Deliver(p: ZonedDateTime)

// Output message types for multiparty sessions (autogenerated by Scribble)
case class OkA(p: Unit)
case class QuitA(p: Unit)
case class OkS(p: Unit)
case class Address(p: String)
case class QuitS(p: Unit)

// Multiparty session classes (autogenerated by Scribble)
case class MPPlayBob(c: In[binary.PlayBob]) {
  def receive(implicit timeout: Duration = Duration.Inf) = {
    c.receive(timeout) match {
      case m @ binary.PlayBob(p) => {
        PlayBob(p)
      }
    }
  }
}

case class MPQuoteB(alice: In[binary.ShareA], seller: In[binary.QuoteB]) {
  def receive(implicit timeout: Duration = Duration.Inf) = {
    seller.receive(timeout) match {
      case m @ binary.QuoteB(p) => {
        QuoteB(p, MPShareA(alice, m.cont))
      }
    }
  }
}

case class MPShareA(alice: In[binary.ShareA], seller: Out[binary.OkSOrQuitS]) {
  def receive(implicit timeout: Duration = Duration.Inf) = {
    alice.receive(timeout) match {
      case m @ binary.ShareA(p) => {
        ShareA(p, MPOkAOrQuitA(m.cont, seller))
      }
    }
  }
}

case class MPOkAOrQuitA(alice: Out[binary.OkAOrQuitA], seller: Out[binary.OkSOrQuitS]) {
  def send(v: OkA) = {
    val cnt = alice ! binary.OkA(v.p)
    MPOkS(cnt, seller)
  }
  def send(v: QuitA) = {
    val cnt = alice ! binary.QuitA(v.p)
    MPQuitS(cnt, seller)
  }
}

case class MPOkS(alice: Unit, seller: Out[binary.OkSOrQuitS]) {
  def send(v: OkS) = {
    val cnt = seller !! binary.OkS(v.p)_
    MPAddress(alice, cnt)
  }
}

case class MPAddress(alice: Unit, seller: Out[binary.Address]) {
  def send(v: Address) = {
    val cnt = seller !! binary.Address(v.p)_
    MPDeliver(alice, cnt)
  }
}

case class MPDeliver(alice: Unit, seller: In[binary.Deliver]) {
  def receive(implicit timeout: Duration = Duration.Inf) = {
    seller.receive(timeout) match {
      case m @ binary.Deliver(p) => {
        Deliver(p)
      }
    }
  }
}

case class MPQuitS(alice: Unit, seller: Out[binary.OkSOrQuitS]) {
  def send(v: QuitS) = {
    val cnt = seller ! binary.QuitS(v.p)
    ()
  }
}

// Local type for role Bob (in Delegation protocol):
//    Carol⊕{Contrib(Int).Carol⊕{Delegate(alice⊕{OkA(Unit).seller⊕{OkS(Unit).seller⊕{Address(String).seller&{Deliver(ZonedDateTime).end}}}, QuitA(Unit).seller⊕{QuitS(Unit).end}}).Carol&{OkC(Unit).end, QuitC(Unit).end}}}

// Input message types for multiparty sessions (autogenerated by Scribble)
sealed abstract class MsgMPOkCOrQuitC
case class OkC(p: Unit) extends MsgMPOkCOrQuitC
case class QuitC(p: Unit) extends MsgMPOkCOrQuitC

// Output message types for multiparty sessions (autogenerated by Scribble)
case class Contrib(p: Int)
case class Delegate(p: MPOkAOrQuitA)

// Multiparty session classes
case class MPContrib(carol: Out[binary.Contrib]) {
  def send(v: Contrib) = {
    val cnt = carol !! binary.Contrib(v.p)_
    MPDelegate(cnt)
  }
}

case class MPDelegate(carol: Out[binary.Delegate]) {
  def send(v: Delegate) = {
    val cnt = carol !! binary.Delegate(v.p)_
    MPOkCOrQuitC(cnt)
  }
}

case class MPOkCOrQuitC(carol: In[binary.OkCOrQuitC]) {
  def receive(implicit timeout: Duration = Duration.Inf) = {
    carol.receive(timeout) match {
      case m @ binary.OkC(p) => {
        OkC(p)
      }
      case m @ binary.QuitC(p) => {
        QuitC(p)
      }
    }
  }
}
